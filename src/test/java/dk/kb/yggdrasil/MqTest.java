package dk.kb.yggdrasil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import dk.kb.yggdrasil.exceptions.YggdrasilException;
import dk.kb.yggdrasil.messaging.MQ;
import dk.kb.yggdrasil.messaging.MqResponse;

/**
 * These tests require an rabbitmq to be present on localhost
 * using port 5673.
 * You can have several rabbitmq brokers running on the same server.
 * Each though must have their own port, and nodename which can be set
 * using export RABBITMQ_NODENAME=bunny
 * export RABBITMQ_NODE_PORT=5673
 * See manpage for rabbitmq-server for more details.
 *
 * These tests assume the rabbitmq on localhost, if not override RABBITMQ_HOSTNAME
 * (e.g. export RABBITMQ_HOSTNAME=dia-prod-udv-01.kb.dk or DRABBITMQ_HOSTNAME=dia-prod-udv-01.kb.dk,)
 * and another port than 5672 by setting the RABBITMQ_PORT (
 * (e.g. export RABBITMQ_PORT=5673 or DRABBITMQ_PORT=5673);
 */
@RunWith(JUnit4.class)
public class MqTest {

    public static String RABBITMQ_CONF_FILE = "src/test/resources/config/rabbitmq.yml";

    @Test
    public void finalTest() throws YggdrasilException, IOException {
        RabbitMqSettings settings = fetchMqSettings();
        System.out.println("using brokerUri: " + settings.getBrokerUri());
        MQ mq = new MQ(settings);
        assertTrue(settings.equals(mq.getSettings()));
        String message = "Hello world";
        String queueName = settings.getPreservationDestination();
        mq.publishOnQueue(queueName, message.getBytes(), MQ.PRESERVATIONREQUEST_MESSAGE_TYPE);
        MqResponse messageReceived = mq.receiveMessageFromQueue(queueName); 
 

        Assert.assertArrayEquals(message.getBytes(), messageReceived.getPayload());
        message = "Hello X";
        mq.publishOnQueue(queueName, message.getBytes(), MQ.PRESERVATIONREQUEST_MESSAGE_TYPE);
        messageReceived = mq.receiveMessageFromQueue(queueName);

        Assert.assertArrayEquals(message.getBytes(), messageReceived.getPayload());
        mq.cleanup();
    }

    @Test
    public void testReceived() throws KeyManagementException,
    NoSuchAlgorithmException, URISyntaxException, IOException, YggdrasilException {
        RabbitMqSettings settings = fetchMqSettings();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(settings.getBrokerUri());
        Connection conn = factory.newConnection();

        final Channel channel = conn.createChannel();

        String exchangeName = "exchange";
        String queueName = settings.getPreservationDestination();
        String routingKey = "routing";

        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        byte[] messageBodyBytes = "hello world".getBytes();
        // .userId("bob")
        channel.basicPublish(exchangeName, routingKey,
                new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2)
                .priority(1).build(), messageBodyBytes);

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
        }

        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, "myConsumerTag",
                new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body)
                            throws IOException {
                //String routingKey = envelope.getRoutingKey();
                //String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                // (process the message components here ...)
                //System.out.println(new String(body));
                channel.basicAck(deliveryTag, false);
            }
        });


        // .userId("bob")
        channel.basicPublish(exchangeName, routingKey,
                new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).priority(1)
                .build(),messageBodyBytes);

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
        }

        channel.basicCancel("myConsumerTag");

        channel.close();
        conn.close();
        //System.out.println(channel.getCloseReason());
        
    }

    @Test
    public void testRabbitMqSettingsAlternateConstructor() throws FileNotFoundException, YggdrasilException {
        File f = new File(RABBITMQ_CONF_FILE);
        RabbitMqSettings settings = new RabbitMqSettings(f);
        String brokerUri = settings.getBrokerUri();
        String dissDest = settings.getDisseminationDestination();
        String presDest = settings.getPreservationDestination();
        RabbitMqSettings settingsCopy = new RabbitMqSettings(brokerUri, presDest, dissDest);
        assertEquals(brokerUri, settingsCopy.getBrokerUri());
        assertEquals(presDest, settingsCopy.getPreservationDestination());
        assertEquals(dissDest, settingsCopy.getDisseminationDestination());
    }

 
    
    
    private RabbitMqSettings fetchMqSettings() throws FileNotFoundException, YggdrasilException {
        File f = new File(RABBITMQ_CONF_FILE);
        RabbitMqSettings settings = new RabbitMqSettings(f);
        return settings;
    }

}
