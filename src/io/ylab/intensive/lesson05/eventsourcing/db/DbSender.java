package io.ylab.intensive.lesson05.eventsourcing.db;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class DbSender {

    static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    static void deletePerson(Long personId) {
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

    static void savePerson(Long personId, String firstName, String lastName, String middleName) {
        Person person = findPerson(personId);
        if (person == null) {
            addPerson(personId, firstName, lastName, middleName);
        } else {
            updatePerson(person, personId, firstName, lastName, middleName);
        }
    }


    private static void addPerson(Long personId, String firstName, String lastName, String middleName) {
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
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private static void updatePerson(Person person, Long personId, String firstName, String lastName, String middleName) {
        String updateQuery = "UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?";
        try (Connection connection = dataSource.getConnection();
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

    private static Person findPerson(Long personId) {
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

}
