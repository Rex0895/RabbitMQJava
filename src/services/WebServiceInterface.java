package services;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

//интерфейс будет работать как веб-сервис
@WebService
//веб-сервис будет использоваться для вызова методов
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WebServiceInterface {
	//метод можно вызывать удаленно
    @WebMethod
    public String getWSname();
    @WebMethod
    public void messageAction(String message)throws IOException, TimeoutException;
}