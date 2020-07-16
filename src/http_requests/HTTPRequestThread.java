package http_requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class HTTPRequestThread implements Runnable{
	private int id;
	private int msgCount;
	private int interval;
	
	public HTTPRequestThread(int id, int count,int interval) {
		this.id=id;
		this.msgCount=count;
		this.interval=interval;
	}
	
	public void run()
	{
		publishMsg(id,msgCount,interval);
	}
	
	public void publishMsg(int id, int count, int interval) {
		try {
			String msg;
			for(int i =1;i<=count;i++) {
				msg="Отправитель №"+id+" Сообщение №" +i+";";
				postMsg(msg);
				if(interval!=0)
					Thread.sleep(interval);	
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void postMsg(String text) throws IOException {
		URL url = new URL ("http://localhost:15672/api/exchanges/testvh/testex/publish");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		String data="{\"properties\":{},\"routing_key\":\"my_key\",\"payload\":\""+text+"\",\"payload_encoding\":\"string\"}";
		
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode("guest:guest".getBytes()));
		con.setRequestMethod("POST");
		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");//text/plain
		con.setRequestProperty("charset", "utf-8");
		con.setDoOutput(true);
		//запись параметров запроса в поток
		 OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(),"utf-8");//,"utf-8"
	     wr.write(data);
	     wr.flush();
	     wr.close();
	     //чтение ответа
	   
		try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    System.out.println(response.toString()+": "+text);
				    br.close();
				}
		con.disconnect();
	}
	
	public void getMsg(int count) throws IOException {
		URL url = new URL ("http://localhost:15672/api/queues/testvh/testq/get");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		String data ="{\"count\":"+count+", \"requeue\":true, \"ackmode\": \"ack_requeue_true\", \"encoding\":\"auto\"}";
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode("guest:guest".getBytes()));
		
		con.setRequestMethod("POST");
		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestProperty("Content-Type","application/json");
		con.setRequestProperty("charset", "utf-8");
		con.setDoOutput(true);
		//запись параметров запроса в поток
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(),"utf-8");//,"utf-8"
	     wr.write(data);
	     wr.flush();
	     wr.close();
//	     //чтение ответа
		try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim().replace(',', '\n'));
				    }
				  
				    String result = response.toString().replace("}", "}\n");
				    
				    System.out.println("____________GET "+count+" MESSAGES_______________\n"+result);
				    br.close();
				}
		
	     con.disconnect();
	}
}
