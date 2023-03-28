package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ApiApp {
  static String exchangeName = "exc";
  static String queueName = "queue";

  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    DataSource dataSource = initDb();
    // Тут пишем создание PersonApi, запуск и демонстрацию работы

//    String exchangeName = "exc";
//    String queueName = "queue";
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
      channel.queueDeclare(queueName, true, false, false, null);
      channel.queueBind(queueName, exchangeName, "*");

//      channel.basicPublish(exchangeName, "*", null, "Hello World".getBytes());
//      channel.basicPublish(exchangeName, queueName, null, "Hello World".getBytes());
    }

    PersonApi personApi = new PersonApiImpl(dataSource);
    startGenerateAndSavePersonData(personApi);
    startTestFindPerson(personApi);
    startTestSaveAndUpdatePerson(personApi);
    startTestDeletePerson(personApi);
    startTestFindAllPerson(personApi);

//    Person person1 = new Person(23L, "UFDihwa8-2", "", "4nzw$3w@&?55");
//    personApi.savePerson(person1.getId(), person1.getName(), person1.getLastName(), person1.getMiddleName());
  }

  private static void startGenerateAndSavePersonData(PersonApi personApi) {
    List<String> firstNameList = new ArrayList<>(Arrays.asList("Ivan","Petr","Roman","Fedor","Maxim"));
    List<String> lastNameList = new ArrayList<>(Arrays.asList("Ivanov","Petrov","Romanov","Fedorov","Maximov"));
    List<String> middleNameList = new ArrayList<>(Arrays.asList("Ivanovich","Petrovich","Romanovich","Fedorovich","Maximovich"));
    Random rand = new Random();
    for (Long i = 1L; i < 15; i++) {
      Person person = new Person(
              i,
              firstNameList.get(rand.nextInt(firstNameList.size())),
              lastNameList.get(rand.nextInt(lastNameList.size())),
              middleNameList.get(rand.nextInt(middleNameList.size())));

      personApi.savePerson(person.getId(), person.getName(), person.getLastName(), person.getMiddleName());
    }
  }

  private static void startTestFindPerson(PersonApi personApi) {
    Person personFind = personApi.findPerson(5L);     // exist
    if (personFind != null) {
      System.out.println("findPerson: " + personFind.getId() + ", " + personFind.getName() + ", " + personFind.getLastName() + ", " + personFind.getMiddleName());
    }

    Person personFind2 = personApi.findPerson(55L);   // NOT exist
    if (personFind2 != null) {
      System.out.println("personFind2: " + personFind2.getId() + ", " + personFind2.getName() + ", " + personFind2.getLastName() + ", " + personFind2.getMiddleName());
    }
  }

  private static void startTestSaveAndUpdatePerson(PersonApi personApi) {
    personApi.savePerson(1L, "ADMIN", "ADMIN", "ADMIN");  // update
    personApi.savePerson(22L, "Iv~", "", "");             // add new
    personApi.savePerson(22L, "", "Iv~ov", "");           // update part
//    try {
//      System.out.println("Update DB to check personId=22 current state.");
//      Thread.sleep(2023);                                                                // pause to check DB
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
    personApi.savePerson(22L, "Ivan", "Ivanov", "Ivanovich"); // update full
  }

  private static void startTestDeletePerson(PersonApi personApi) {
    try {
      System.out.println(" TEST_Delete_pause=5sec");
      Thread.sleep(5023);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    personApi.deletePerson(7L);
    personApi.deletePerson(9L);
    personApi.deletePerson(11L);
    personApi.deletePerson(13L);
  }

  private static void startTestFindAllPerson(PersonApi personApi) {
    List<Person> personList = personApi.findAll();
    for (Person p : personList) {
      System.out.println(p.getId() + " " + p.getName() + " " + p.getLastName() + " " + p.getMiddleName());
    }
  }

  public static void sendMessage(String message) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      channel.basicPublish(exchangeName, "*", null, message.getBytes());
    }
  }


  public static DataSource initDb() throws SQLException {
    DataSource dataSource = DbUtil.buildDataSource();
    return dataSource;
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
