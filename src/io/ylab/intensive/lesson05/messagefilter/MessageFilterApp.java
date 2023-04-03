package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Component
public class MessageFilterApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();

    String exchangeName = "exc";
    String queueInput = "input";
    String queueOutput = "output";
    String importFilePath = "src/io/ylab/intensive/lesson05/messagefilter/data/wordslist.txt";

    ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
    createExchangeQueueBind(connectionFactory, exchangeName, queueInput, queueOutput);

    StopWordValidator stopWordValidator = applicationContext.getBean(StopWordValidator.class);
    File file = new File(importFilePath);
    stopWordValidator.loadData(file);                               // Load file data to DB
//    MessageFilterAppTestData.runTest(stopWordValidator);                                     // run handmade tests (no RabbitMQ)
//    MessageFilterAppTestData.runTestToRabbit(connectionFactory, exchangeName, queueInput);   // run handmade tests (with RabbitMQ)

    runListener(connectionFactory, exchangeName, queueInput, queueOutput);
  }

  public static void createExchangeQueueBind(ConnectionFactory connectionFactory, String exchangeName, String queueInput, String queueOutput) {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

      channel.queueDeclare(queueInput, true, false, false, null);   // Input QueueBind
      channel.queueBind(queueInput, exchangeName, queueInput);

      channel.queueDeclare(queueOutput, true, false, false, null);  // Output QueueBind
      channel.queueBind(queueOutput, exchangeName, queueOutput);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  private static void runListener(ConnectionFactory connectionFactory, String exchangeName, String queueInput, String queueOutput) {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {
      System.out.println("(ServiceMessage: Listener of \"" + queueInput + "\" queue is started.)");
      while (!Thread.currentThread().isInterrupted()) {
        GetResponse message = channel.basicGet(queueInput, true);
//        Thread.sleep(123);

        if (message != null) {
          String received = new String(message.getBody());
          String resultMessage = new StopWordValidatorImpl().validateText(received);
          sendMessageToQueueOutput(resultMessage, connectionFactory, exchangeName, queueOutput);
          System.out.println("  input=" + received);
          System.out.println(" output=" + resultMessage);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void sendMessageToQueueInput(String message, ConnectionFactory connectionFactory, String exchangeName, String queueInput) throws Exception {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      channel.basicPublish(exchangeName, queueInput, null, message.getBytes());
    }
  }
  public static void sendMessageToQueueOutput(String message, ConnectionFactory connectionFactory, String exchangeName, String queueOutput) throws Exception {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      channel.basicPublish(exchangeName, queueOutput, null, message.getBytes());
    }
  }
}
