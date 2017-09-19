import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
	ArrayList<PrintWriter> clientWritersList;

	public static void main(String[] args) {
		Server server = new Server();
		server.go(args[0]);
	}

	public void go(String ip) {
		clientWritersList = new ArrayList<PrintWriter>();
		try {
			ServerSocket serverSocket = new ServerSocket(5000, 50, InetAddress.getByName(ip));
			System.out.println(serverSocket.toString());

			while(true) {
				System.out.println("Server is ready...");
				Socket socket = serverSocket.accept();

				Thread thread = new Thread(new ClientHandler(socket));
				thread.start();
				System.out.println("Got a connection from " + socket.getInetAddress().getHostName() + "/" + socket.getInetAddress().getHostAddress());
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public class ClientHandler implements Runnable {
		Socket socket;
		PrintWriter writer;
		BufferedReader reader;

		ClientHandler(Socket socket) {
			try {
				this.socket = socket;

				writer = new PrintWriter(socket.getOutputStream());
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				reader = new BufferedReader(isReader);

				clientWritersList.add(writer);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			try {
				String msg;
				while((msg = reader.readLine()) != null) {
					System.out.println("Read: " + msg);
					broadCast(msg);
				}
				socket.close();
				System.out.println("Lost a connection.");
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		public void broadCast(String msg) {
			Iterator<PrintWriter> it = clientWritersList.iterator();
			while(it.hasNext()) {
				try {
					PrintWriter writer = it.next();
					writer.println(msg);
					writer.flush();
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}




