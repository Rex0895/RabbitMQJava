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
		System.out.println("Получатель №: "+Integer.toString(this.hashCode()).substring(0, 3)+" запущен");
		executeTask();
	}

	public synchronized void executeTask() {
		try {
			//Создание соединения с RabbitMQ
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			factory.setAutomaticRecoveryEnabled(true);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			//Объявление обменника, в который отправляют нужные сообщения
			channel.exchangeDeclare("exchangeA", "fanout");
			//channel.confirmSelect();
			//Выделение из обменника уникальной временной очереди
			/*
			 * Для персистивности (аварийного сохранения) сообщений
			 * использовать durable queue и задать параметр
			 * AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
			 * channel.basicPublish(exchange, rootinKey, basicProperties, message body in bytes);
			 * messages: /var/lib/rabbitmq/mnesia/rabbit@rabbitmq-node1/msg_store_persistent
			 * directory: /var/lib/rabbitmq/mnesia/rabbit@rabbitmq-node1/msg_store_transient
			 */
			String queueName = channel.queueDeclare().getQueue();
			//Закрепление выделенной очереди в обменнике
		    channel.queueBind(queueName, "exchangeA", "");
			/*
			 *  Подкласс DeliverCallback предоставляет обратный вызов в виде объекта, 
			 *  который будет буферизировать сообщения и выводить на экран .
			 */
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String text=("Получатель №: "+Integer.toString(this.hashCode()).substring(0, 3));
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(text + " получил: '" + message + "'");
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
