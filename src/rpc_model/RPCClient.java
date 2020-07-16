package rpc_model;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable, Runnable {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private int id,number;

	public RPCClient(int id,int number) throws IOException, TimeoutException {
		this.id = id;
		this.number=number;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	public void run() {
//    	 try (RPCClient timeRpc = new RPCClient()) {
		Random rand = new Random();
		String randNumberStr,str;
		String idStr=Integer.toString(id);
		for (int i = 1; i <= number; i++) {
			int randNumber = rand.nextInt(20);
			randNumberStr = Integer.toString(randNumber);
			str =  idStr+ " " + randNumberStr;
			System.out.println(
					" [x] Request from Client #" + idStr + ": 5 * " + randNumberStr + " = ");
			try {
				String response = call(str);
				System.out.println(response);
				Thread.sleep(2000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public String call(String message) throws IOException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();

		String replyQueueName = channel.queueDeclare().getQueue();
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
				.build();

		channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

		final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

		String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response.offer(new String(delivery.getBody(), "UTF-8"));
			}
		}, consumerTag -> {
		});

		String result = response.take();
		channel.basicCancel(ctag);
		return result;
	}

	public void close() throws IOException {
		connection.close();
	}
}
