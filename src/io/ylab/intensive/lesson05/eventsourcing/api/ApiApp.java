package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    // пишем взаимодействие с PersonApi


    startGenerateAndSavePersonData(personApi);
    startTestFindPerson(personApi);
    startTestSaveAndUpdatePerson(personApi);
    startTestDeletePerson(personApi);
    startTestFindAllPerson(personApi);

//    personApi.savePerson(123L, "UFDihwa8-2", "", "4nzw$3w@&?55");
//    printPerson(personApi.findPerson(123L));
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
    System.out.println("findPerson: ");
    printPerson(personFind);

    Person personFind2 = personApi.findPerson(55L);   // NOT exist
    System.out.println("personFind2: ");
    printPerson(personFind2);
  }

  private static void startTestSaveAndUpdatePerson(PersonApi personApi) {
    personApi.savePerson(1L, "ADMIN", "ADMIN", "ADMIN");  // update
    personApi.savePerson(22L, "Iv~", "", "");             // add new
    personApi.savePerson(22L, "", "Iv~ov", "");           // update part
    personApi.savePerson(22L, "Ivan", "", "Ivanovich"); // update full
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
      printPerson(p);
    }
  }

  private static void printPerson(Person person) {
    if (person != null) {
      System.out.print(person.getId() + ", " + person.getName() + ", " + person.getLastName() + ", " + person.getMiddleName() + "\n");
    }
  }
}
