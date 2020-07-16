package client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

public class SOAPSender {
	public static void main(String args[]) {

		try {

			//Cоздание соединение
			SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = soapConnFactory.createConnection();
			// Создание SOAP сообщения
			MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			for(int i =0;i<100;i++) {
				String mess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services\">"
						+"<soapenv:Header/>" + "<soapenv:Body>" + "<ser:messageAction>"
						+ "<ser:message>MSG #"+i+" "+java.time.LocalTime.now()+"</ser:message>" + "</ser:messageAction>" + "</soapenv:Body>"
						+ "</soapenv:Envelope>";

				InputStream is = new ByteArrayInputStream(mess.getBytes());
				SOAPMessage message= MessageFactory.newInstance().createMessage(null, is);
				MimeHeaders headers = message.getMimeHeaders();
				//Название метода веб-сервиса
				headers.addHeader("SOAPAction", "messageAction");
				message.saveChanges();
		        //Установка адресата
		        String destination = "http://localhost:8080/RabbitProjectV1/services/SenderService";
		        //Отправка
		        SOAPMessage reply = connection.call(message, destination);
		        Thread.sleep(3000);	
			}
			// Закрываем соединение
			connection.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
