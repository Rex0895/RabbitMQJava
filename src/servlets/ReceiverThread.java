package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiverThread implements Runnable {
	//private final static String QUEUE_NAME = "wsqueue";
	private static ArrayList<String> messages = new ArrayList<>();
	public void run() {
		System.out.println("���������� �: "+Integer.toString(this.hashCode()).substring(0, 3)+" �������");
		executeTask();
	}

	public synchronized void executeTask() {
		try {
			//�������� ���������� � RabbitMQ
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			factory.setAutomaticRecoveryEnabled(true);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			//���������� ���������, � ������� ���������� ������ ���������
			channel.exchangeDeclare("exchangeA", "fanout");
			//channel.confirmSelect();
			//��������� �� ��������� ���������� ��������� �������
			/*
			 * ��� �������������� (���������� ����������) ���������
			 * ������������ durable queue � ������ ��������
			 * AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
			 * channel.basicPublish(exchange, rootinKey, basicProperties, message body in bytes);
			 * messages: /var/lib/rabbitmq/mnesia/rabbit@rabbitmq-node1/msg_store_persistent
			 * directory: /var/lib/rabbitmq/mnesia/rabbit@rabbitmq-node1/msg_store_transient
			 */
			String queueName = channel.queueDeclare().getQueue();
			//����������� ���������� ������� � ���������
		    channel.queueBind(queueName, "exchangeA", "");
			/*
			 *  �������� DeliverCallback ������������� �������� ����� � ���� �������, 
			 *  ������� ����� �������������� ��������� � �������� �� ����� .
			 */
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String text=("���������� �: "+Integer.toString(this.hashCode()).substring(0, 3));
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(text + " �������: '" + message + "'");
				messages.add(message);
			};
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
			});

//		}
//		catch(InterruptedException e) {
//			System.out.println("Interrupted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			System.out.println("TimeoutException");
		}	
	}
	
}
