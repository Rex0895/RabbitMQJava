package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("")
public class ReceiverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static final Logger logger = LogManager.getLogger(ReceiverServlet.class);
	//private final static String QUEUE_NAME = "wsqueue";
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=Windows-1251");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        PrintWriter writer = response.getWriter();
		writer.println("<h2>"+" [*] Waiting for messages. To exit press CTRL+C"+"</h2>");
		try {
			for (int i = 0; i < 10; i++) {
				Thread t = new Thread(new Receiver());
				t.start();
			}
		}
		finally {
			writer.close();
		}
//	
	}
}
