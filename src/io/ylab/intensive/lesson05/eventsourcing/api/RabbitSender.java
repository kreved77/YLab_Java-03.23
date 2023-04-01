package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {
    static String exchangeName = "exc";
    static String queueName = "queue";
    static ConnectionFactory connectionFactory;

    @Autowired
    public RabbitSender(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    public static void sendMessageToQueue(String message) throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicPublish(exchangeName, queueName, null, message.getBytes());
        }
    }
}
