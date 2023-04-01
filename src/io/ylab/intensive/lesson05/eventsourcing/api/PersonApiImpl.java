package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonApiImpl implements PersonApi {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RabbitSender rabbitSender;

//    public PersonApiImpl() {
//    }
//
//    @Autowired
//    public PersonApiImpl(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Autowired
//    public PersonApiImpl(DataSource dataSource, RabbitSender rabbitSender) {
//        this.dataSource = dataSource;
//        this.rabbitSender = rabbitSender;
//    }

    @Override
    public void deletePerson(Long personId) {
        System.out.println(deletePersonQueueMessageGenerate(personId));
    }

    public String deletePersonQueueMessageGenerate(Long personId) {
        try {
            rabbitSender.sendMessageToQueue("del;" + personId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "personId=" + personId + " deletePerson queue message generated";
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        System.out.println(savePersonQueueMessageGenerate(personId, firstName, lastName, middleName));
    }

    public String savePersonQueueMessageGenerate(Long personId, String firstName, String lastName, String middleName) {
        try {
            rabbitSender.sendMessageToQueue("sav;" + personId + ";" + firstName + ";" + lastName + ";" + middleName + "; ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "personId=" + personId + " savePerson queue message generated";
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
}

