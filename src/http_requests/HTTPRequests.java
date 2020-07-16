package http_requests;

import java.util.Random;

public class HTTPRequests {

	public static void main(String[] args) {
		Random rand = new Random();
		
		int interval=500;
		for(int i=1;i<=10;i++) {
			int msgCount=rand.nextInt(10000);
			Thread hrt=new Thread(new  HTTPRequestThread(i,msgCount,interval));
			System.out.println("Thread #"+i+" created with "+msgCount+" messages and "+interval+"ms interval");
			hrt.start();
		}

	}

}
