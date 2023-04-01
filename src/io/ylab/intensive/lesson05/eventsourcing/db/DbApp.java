package io.ylab.intensive.lesson05.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class DbApp {
  @Autowired
  static DbSender dbSender;

  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();

    // тут пишем создание и запуск приложения работы с БД

    ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
    String queueName = "queue";
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {
      while (!Thread.currentThread().isInterrupted()) {
        GetResponse message = channel.basicGet(queueName, true);

        if (message != null) {
          String received = new String(message.getBody());
          parseMessage(received);
        }
      }
    }
  }


  public static void parseMessage(String received) {
    System.out.println("\n NEXT: " + received);
    if ("sav".equals(received.substring(0,3))) {
      String[] strArr = received.substring(4).split(";");
      dbSender.savePerson(Long.valueOf(strArr[0]), strArr[1], strArr[2], strArr[3]);
    }
    if ("del".equals(received.substring(0,3))) {
      String str = received.substring(4);
      dbSender.deletePerson(Long.valueOf(str));
    }
  }
}
