package net.moznion.rabbitmq.work_queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class NewTask {
    private static final String QUEUE_NAME = "task_queue";

    public static void main(String... argv) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        final String message = getMessage(Arrays.asList(argv));
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("[x] Sent: " + message);

        channel.close();
        connection.close();
    }

    private static String getMessage(List<String> strings) {
        if (strings.isEmpty()) {
            return "Hello World";
        }
        return strings.stream().collect(Collectors.joining(" "));
    }
}
