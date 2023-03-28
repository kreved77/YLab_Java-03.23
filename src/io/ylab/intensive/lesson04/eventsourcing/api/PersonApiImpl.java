package io.ylab.intensive.lesson04.eventsourcing.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
  private DataSource dataSource;

  public PersonApiImpl() {
//    try {
//      this.dataSource = initDb();
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
  }
  public PersonApiImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void deletePerson(Long personId) {
    System.out.println(deletePersonQueueMessageGenerate(personId));
//    deletePersonQueueMessageGenerate(personId);
  }

  public String deletePersonQueueMessageGenerate(Long personId) {
    try {
      ApiApp.sendMessageToQueue("del;" + personId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return "deletePerson queue message generated";
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    System.out.println(savePersonQueueMessageGenerate(personId, firstName, lastName, middleName));
//    savePersonQueueMessageGenerate(personId, firstName, lastName, middleName);
  }

  public String savePersonQueueMessageGenerate(Long personId, String firstName, String lastName, String middleName) {
    try {
      ApiApp.sendMessageToQueue("sav;" + personId + ";" + firstName + ";" + lastName + ";" + middleName + "; ");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return "savePerson queue message generated";
  }

  @Override
  public Person findPerson(Long personId) {
    String findPersonQuery = "SELECT * FROM person WHERE person_id=?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(findPersonQuery)) {
      preparedStatement.setLong(1, personId);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        Person person = new Person();
        person.setId(personId);
        person.setName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setMiddleName(resultSet.getString(4));
        return person;
      } else {
        System.out.println("Check personId=" + personId + " : record not found");
      }
    } catch (SQLException e) {
      System.err.println(e);
    }
    return null;
  }

  @Override
  public List<Person> findAll() {
    List<Person> personList = new ArrayList<>();
    String findAllQuery = "SELECT * FROM person ORDER BY person_id ASC";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Person person = new Person();
        person.setId(Long.valueOf(resultSet.getString(1)));
        person.setName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setMiddleName(resultSet.getString(4));
        personList.add(person);
      }
    } catch (SQLException e) {
      System.err.println(e);
    }
    return personList;
  }


//  public void deletePerson(Long personId, DataSource dataSource) {
////    if (dataSource == null) {
////      System.out.println(deletePersonQueueMessageGenerate(personId));
//////      deletePersonQueueMessageGenerate(personId);
////    } else {
//      if (findPerson(personId) == null) {
//        System.err.println("Warning (at 'deletePerson')! No Person with personId=" + personId);
//      } else {
//        String removeQuery = "DELETE FROM person WHERE person_id=?";
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
//          preparedStatement.setLong(1, personId);
//          preparedStatement.executeUpdate();
//          System.out.println("Person with personId=" + personId + " record deleted");
//        } catch (SQLException e) {
//          System.err.println(e);
//        }
//      }
////    }
//  }

//  @Override
//  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
//    if (dataSource == null) {
//      System.out.println(savePersonQueueMessageGenerate(personId, firstName, lastName, middleName));
//      savePersonQueueMessageGenerate(personId, firstName, lastName, middleName);
//    } else {
//      Person person = findPerson(personId);
//      if (person == null) {
//        String insertQuery = "insert into person (person_id, first_name, last_name, middle_name) values (?, ?, ?, ?)";
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
//          preparedStatement.setLong(1, personId);
//          preparedStatement.setString(2, firstName);
//          preparedStatement.setString(3, lastName);
//          preparedStatement.setString(4, middleName);
//          preparedStatement.executeUpdate();
//        } catch (NullPointerException nullPointerException) {
//          System.err.println("Warning (at 'savePerson')! Some Person data is null.");
//        } catch (SQLException e) {
//          System.err.println(e);
//        }
//      } else {
//        updatePerson(person, personId, firstName, lastName, middleName);
//      }
//    }
//  }

//  private void updatePerson(Person person, Long personId, String firstName, String lastName, String middleName) {
//    String updateQuery = "UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?";
//    try (Connection connection = dataSource.getConnection();
//         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
//      preparedStatement.setString(1, firstName.equals("") ? person.getName() : firstName);
//      preparedStatement.setString(2, lastName.equals("") ? person.getLastName() : lastName);
//      preparedStatement.setString(3, middleName.equals("") ? person.getMiddleName() : middleName);
//      preparedStatement.setLong(4, personId);
//      preparedStatement.executeUpdate();
//    } catch (NullPointerException nullPointerException) {
//      System.err.println("Warning (at 'savePerson -> updatePerson')! Some Person data is null.");
//    } catch (SQLException e) {
//      System.err.println(e);
//    }
//  }

//  public static DataSource initDb() throws SQLException {
//    DataSource dataSource = DbUtil.buildDataSource();
//    return dataSource;
//  }
}
