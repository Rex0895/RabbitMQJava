package rpc_model;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Main {

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		int msgNumbers =25;
		int clientNumber = 5;
		for (int i = 1; i <= clientNumber; i++) {
				Thread t = new Thread(new RPCClient(i,msgNumbers));
				t.start();
			}
	}
}
