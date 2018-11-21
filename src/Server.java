import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {

		int default_port = 2345;
		int port;
		
		
		try {
			if (args.length == 0) {
				port = default_port;
			} else
				port = Integer.parseInt(args[0]);

			
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("....Server started on port "+port+" ...");
			Socket client;
			
			boolean moreClient = true;
			
			while (moreClient) {
				client = serverSocket.accept();
				new Thread(new ServiceThread(client)).start();
			}
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
	}

}