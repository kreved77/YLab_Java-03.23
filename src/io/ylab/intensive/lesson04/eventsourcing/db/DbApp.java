package io.ylab.intensive.lesson04.eventsourcing.db;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

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

        try {
          Thread.sleep(1023);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis());
        if (message == null) {

        } else {
          String received = new String(message.getBody());
          System.out.println(received);
        }
      }
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
