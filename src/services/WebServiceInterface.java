package services;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

//��������� ����� �������� ��� ���-������
@WebService
//���-������ ����� �������������� ��� ������ �������
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WebServiceInterface {
	//����� ����� �������� ��������
    @WebMethod
    public String getWSname();
    @WebMethod
    public void messageAction(String message)throws IOException, TimeoutException;
}