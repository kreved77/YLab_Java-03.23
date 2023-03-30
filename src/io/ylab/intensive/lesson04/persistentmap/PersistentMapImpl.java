package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {

  private String mapName;
  private DataSource dataSource;

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    this.mapName = name;
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    String containsKeyQuery = "SELECT key FROM persistent_map WHERE map_name=? AND key=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(containsKeyQuery)) {
      preparedStatement.setString(1, mapName);
      preparedStatement.setString(2, key);
      return preparedStatement.executeQuery().next();
    }
  }

  @Override
  public List<String> getKeys() throws SQLException {
    List<String> listKeys = new ArrayList<>();
    String getKeysQuery = "SELECT key FROM persistent_map WHERE map_name=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getKeysQuery)) {
      preparedStatement.setString(1, mapName);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String str = resultSet.getString(1);
        listKeys.add(str);
      }
    }
    return listKeys;
  }

  @Override
  public String get(String key) throws SQLException {
    String getValueQuery = "SELECT value FROM persistent_map WHERE map_name=? AND key=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getValueQuery)) {
      preparedStatement.setString(1, mapName);
      preparedStatement.setString(2, key);

      ResultSet resultSet = preparedStatement.executeQuery();
      return (resultSet.next()) ? resultSet.getString(1) : null;
    }
  }

  @Override
  public void remove(String key) throws SQLException {
    String removeQuery = "DELETE FROM persistent_map WHERE map_name=? AND key=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
      preparedStatement.setString(1,mapName);
      preparedStatement.setString(2,key);

      int i = preparedStatement.executeUpdate();
      System.out.println(i + " record deleted");
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    if (containsKey(key)) {
      remove(key);
    }
    String insertQuery = "insert into persistent_map (map_name, KEY, value) values (?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
      preparedStatement.setString(1, mapName);
      preparedStatement.setString(2, key);
      preparedStatement.setString(3, value);
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public void clear() throws SQLException {
    String clearQuery = "DELETE FROM persistent_map WHERE map_name=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(clearQuery)) {
      preparedStatement.setString(1,mapName);

      int i = preparedStatement.executeUpdate();
      System.out.println(i + " records deleted. Cleared mapName=" + mapName);
    }
  }

}
