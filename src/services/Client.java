package services;
//�����, ����� �������� wsdl �������� � ����� ����
//���������� �� ������ ���-�������
import java.net.URL;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
//������, ����� ���������� xml-�� c wsdl ���������
//� ���������� �� ���� service � ���
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
//��������� ������ ���-������� (��� ������ � �����)
public class Client {
	public static void main(String[] args)throws IOException, TimeoutException, InterruptedException {
		// ������� ������ �� wsdl ��������
      URL urlSender = new URL("http://localhost:8080/RabbitMessages/webservices/SenderService?wsdl");
      URL urlReceiver = new URL("http://localhost:8080/RabbitMessages/weservices/ReceiverService?wsdl");
      // ��������� ���������� ������������ ������� � ����� ������ ���� WSDL �������� - definitions
      // 1-�� �������� ������� � �������� targetNamespace
      // 2-�� �������� ������� � �������� name
      QName nameSender= new QName("http://webservices", "SenderServiceService");
      QName nameReceiver= new QName("http://webservices", "ReceiverServiceService");

      // ������ �� ����� ���������� �� ���� service � wsdl ��������,
      Service wsSender = Service.create(urlSender, nameSender);
      Service wsReceiver= Service.create(urlReceiver, nameReceiver);
      // � ����� � �� ���������� � ���� ���� port, �����
      // �������� ������ �� ��������� �� ��� ������ ���-�������
      WebServiceInterface  sender = wsSender.getPort(WebServiceInterface.class);
      WebServiceInterface  receiver= wsReceiver.getPort(WebServiceInterface.class);
      receiver.messageAction("");
      Thread.sleep(3000);
      sender.messageAction("������");
      Thread.sleep(3000);
      sender.messageAction("���");
      Thread.sleep(3000);
      sender.messageAction("����?");
      //hello.getWSname()
      // ���! ������ ����� �������� ��������� �����
      
	}
}
