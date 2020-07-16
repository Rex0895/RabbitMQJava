package services;

import javax.jws.WebService;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

@WebService(endpointInterface = "services.WebServiceInterface")
public class SenderService implements WebServiceInterface {
	//private final static String QUEUE_NAME = "wsqueue";

	@Override
	public String getWSname() {
		return "Отправитель";
	}

	public void messageAction(String message) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setAutomaticRecoveryEnabled(true);
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare("exchangeA", "fanout");
			//channel.confirmSelect();
				StringBuilder sb = new StringBuilder();
				sb.append(message);
				channel.basicPublish("exchangeA", "",null, sb.toString().getBytes());
		} catch (IOException e) {
			System.out.println("Ошибка подключения");
		}
		catch (TimeoutException e) {
			System.out.println("Превышено время ожидания");
		}
	}
}
