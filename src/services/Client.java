package services;
//нужно, чтобы получить wsdl описание и через него
//дотянуться до самого веб-сервиса
import java.net.URL;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
//классы, чтобы пропарсить xml-ку c wsdl описанием
//и дотянуться до тега service в нем
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
//интерфейс нашего веб-сервиса (нам больше и нужно)
public class Client {
	public static void main(String[] args)throws IOException, TimeoutException, InterruptedException {
		// создаем ссылку на wsdl описание
      URL urlSender = new URL("http://localhost:8080/RabbitMessages/webservices/SenderService?wsdl");
      URL urlReceiver = new URL("http://localhost:8080/RabbitMessages/weservices/ReceiverService?wsdl");
      // Параметры следующего конструктора смотрим в самом первом теге WSDL описания - definitions
      // 1-ый аргумент смотрим в атрибуте targetNamespace
      // 2-ой аргумент смотрим в атрибуте name
      QName nameSender= new QName("http://webservices", "SenderServiceService");
      QName nameReceiver= new QName("http://webservices", "ReceiverServiceService");

      // Теперь мы можем дотянуться до тега service в wsdl описании,
      Service wsSender = Service.create(urlSender, nameSender);
      Service wsReceiver= Service.create(urlReceiver, nameReceiver);
      // а далее и до вложенного в него тега port, чтобы
      // получить ссылку на удаленный от нас объект веб-сервиса
      WebServiceInterface  sender = wsSender.getPort(WebServiceInterface.class);
      WebServiceInterface  receiver= wsReceiver.getPort(WebServiceInterface.class);
      receiver.messageAction("");
      Thread.sleep(3000);
      sender.messageAction("Привет");
      Thread.sleep(3000);
      sender.messageAction("как");
      Thread.sleep(3000);
      sender.messageAction("дела?");
      //hello.getWSname()
      // Ура! Теперь можно вызывать удаленный метод
      
	}
}
