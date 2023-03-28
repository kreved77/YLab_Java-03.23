package io.ylab.intensive.lesson04.eventsourcing.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import io.ylab.intensive.lesson04.eventsourcing.api.PersonApi;
import io.ylab.intensive.lesson04.eventsourcing.api.PersonApiImpl;

public class DbApp {
  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();

    // тут пишем создание и запуск приложения работы с БД
    String queueName = "queue";
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {
      while (!Thread.currentThread().isInterrupted()) {
        GetResponse message = channel.basicGet(queueName, true);

        if (message != null) {
          String received = new String(message.getBody());
          parseMessage(received, dataSource);
        }
      }
    }
  }

  private static void parseMessage(String received, DataSource dataSource) {
    System.out.println("\n NEXT: " + received);
    if ("sav".equals(received.substring(0,3))) {
      String[] strArr = received.substring(4).split(";");
//      PersonApi personApi = new PersonApiImpl(dataSource);
//      personApi.savePerson(Long.valueOf(strArr[0]), strArr[1], strArr[2], strArr[3]);
      savePerson(Long.valueOf(strArr[0]), strArr[1], strArr[2], strArr[3], dataSource);
    }
    if ("del".equals(received.substring(0,3))) {
      String str = received.substring(4);
//      PersonApi personApi = new PersonApiImpl(dataSource);
//      personApi.deletePerson(Long.valueOf(str));
      deletePerson(Long.valueOf(str), dataSource);
    }
  }


  private static void deletePerson(Long personId, DataSource dataSource) {
    String removeQuery = "DELETE FROM person WHERE person_id=?";
    try (java.sql.Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
      preparedStatement.setLong(1, personId);

      if (preparedStatement.executeUpdate() == 1) {
        System.out.println("Person with personId=" + personId + " record deleted");
      } else {
        System.err.println("Warning (at 'deletePerson')! No Person with personId=" + personId);
      }
    } catch (SQLException e) {
      System.err.println(e);
    }
  }

  private static void savePerson(Long personId, String firstName, String lastName, String middleName, DataSource dataSource) {
    PersonApi personApi = new PersonApiImpl(dataSource);
    Person person = personApi.findPerson(personId);
    if (person == null) {
      String insertQuery = "insert into person (person_id, first_name, last_name, middle_name) values (?, ?, ?, ?)";
      try (java.sql.Connection connection = dataSource.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        preparedStatement.setLong(1, personId);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, middleName);

        if (preparedStatement.executeUpdate() == 1) {
          System.out.println("Person with personId=" + personId + " record saved");
        } else {
          System.err.println("Warning (at 'savePerson')! For Person with personId=" + personId);
        }
//      } catch (NullPointerException nullPointerException) {
//        System.err.println("Warning (at 'savePerson')! Some Person data is null.");
      } catch (SQLException e) {
        System.err.println(e);
      }
    } else {
      updatePerson(person, personId, firstName, lastName, middleName, dataSource);
    }
  }


  private static void updatePerson(Person person, Long personId, String firstName, String lastName, String middleName, DataSource dataSource) {
    String updateQuery = "UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?";
    try (java.sql.Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
      preparedStatement.setString(1, firstName.equals("") ? person.getName() : firstName);
      preparedStatement.setString(2, lastName.equals("") ? person.getLastName() : lastName);
      preparedStatement.setString(3, middleName.equals("") ? person.getMiddleName() : middleName);
      preparedStatement.setLong(4, personId);

      if (preparedStatement.executeUpdate() == 1) {
        System.out.println("Person with personId=" + personId + " record updated");
      } else {
        System.err.println("Warning (at 'savePerson - updatePerson')! For Person with personId=" + personId);
      }
    } catch (SQLException e) {
      System.err.println(e);
    }
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  
  private static DataSource initDb() throws SQLException {
    String ddl = ""
                     + "drop table if exists person;"
                     + "CREATE TABLE if not exists person (\n"
                     + "person_id bigint primary key,\n"
                     + "first_name varchar,\n"
                     + "last_name varchar,\n"
                     + "middle_name varchar\n"
                     + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
