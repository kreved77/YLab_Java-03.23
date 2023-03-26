package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;
  private final String resultFilePath =
          "result.txt";
//          "src/io/ylab/intensive/lesson04/filesort/data/result.txt";

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    File resultFile;
    try {
      readSourceAndSplit(data);
      resultFile = readDescFromDBAndSave(dataSource);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return resultFile;
  }

  private void readSourceAndSplit(File data) throws IOException, SQLException {
    int currentCount = 0;
    int partSize = 500_000;           // split file to parts for big sizes
    List<Long> listOfNums = new ArrayList<>();
    try (Scanner scanner = new Scanner(data)) {
      while (scanner.hasNextLong()) {
        listOfNums.add(scanner.nextLong());
        currentCount++;

        if (currentCount == partSize) {
          saveToDB(listOfNums, dataSource);
          listOfNums = new ArrayList<>();
          currentCount = 0;
        }
      }

      if (currentCount != 0) {
        saveToDB(listOfNums, dataSource);
      }
    }
  }

  private void saveToDB(List<Long> listOfNums, DataSource dataSource) throws SQLException {
    String sqlSave = "INSERT INTO numbers (val) VALUES (?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sqlSave)) {
      for (int i = 0; i < listOfNums.size(); i++) {
        preparedStatement.setLong(1, listOfNums.get(i));
        preparedStatement.addBatch();
      }
      preparedStatement.executeBatch();
    }
  }

  private File readDescFromDBAndSave(DataSource dataSource) throws IOException, SQLException {
    String getNumsQuery = "SELECT val FROM numbers ORDER BY val DESC";
    File resultFile = new File(resultFilePath);
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(getNumsQuery);
        PrintWriter printWriter = new PrintWriter(resultFile)) {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        printWriter.println(resultSet.getString(1));
      }
      printWriter.flush();
    }
    return resultFile;
  }

}
