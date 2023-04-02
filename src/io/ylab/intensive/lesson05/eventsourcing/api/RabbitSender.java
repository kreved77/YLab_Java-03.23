package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitSender {
    static String exchangeName = "exc";
    static String queueName = "queue";
    static ConnectionFactory connectionFactory;

    @Autowired
    public RabbitSender(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        createExchangeQueueBind();
    }
    public static void sendMessageToQueue(String message) throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicPublish(exchangeName, queueName, null, message.getBytes());
        }
    }

    public static void createExchangeQueueBind() {
        try (Connection connection = connectionFactory.newConnection();   // createExchangeQueueBind
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "*");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
