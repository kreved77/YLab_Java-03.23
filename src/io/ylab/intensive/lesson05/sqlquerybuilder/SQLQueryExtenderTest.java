package io.ylab.intensive.lesson05.sqlquerybuilder;

import java.sql.SQLException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SQLQueryExtenderTest {
  public static void main(String[] args) throws SQLException {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilder.class);
    List<String> tables = queryBuilder.getTables();
    // вот так сгенерируем запросы для всех таблиц что есть в БД
    for (String tableName : tables) {
      System.out.println(queryBuilder.queryForTable(tableName));
    }


    // ДОПОЛНИТЕЛЬНО вот так сгенерируем запросы ТОЛЬКО для ПОЛЬЗОВАТЕЛЬСКИХ таблиц, что есть в БД
    System.out.println("\nTEST-method: get only User's tables");
    tables = SQLQueryExtender.getUserTablesOnly();
    for (String tableName : tables) {
      System.out.println(queryBuilder.queryForTable(tableName));
    }

//    System.out.println("\nFixed test tables:");
//    System.out.println(queryBuilder.queryForTable("nullTableName"));  // null
//    System.out.println(queryBuilder.queryForTable("person"));         // SELECT person_id, first_name, last_name, middle_name FROM person;
  }
}
