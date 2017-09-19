import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
	JTextArea incomingTextArea;
	JTextField outgoingTextField;
	JTextField serverIPTextField;

	Socket socket;
	PrintWriter writer;
	BufferedReader reader;

	public static void main(String[] args) {
		Client client = new Client();
		client.go();
	}

	public void go() {

		/* building GUI */
		/* dealing with frame */
		JFrame frame = new JFrame("Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* finish */


		/* dealing with mainPanel */
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//mainPanel.addMouseListener(new MyMouseListener());
		//mainPanel.addMouseMotionListener(new MyMouseMotionListener());
		frame.getContentPane().add(mainPanel);
		/* finish */


		/* dealing with serverIPTextField */
		serverIPTextField = new JTextField(15);
		serverIPTextField.addActionListener(new NetworkConnectListener());
		mainPanel.add(serverIPTextField);
		/* finish */


		/* dealing with connectButton */
		JButton connectButton = new JButton("Connect");
		connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		connectButton.addActionListener(new NetworkConnectListener());
		mainPanel.add(connectButton);
		/* finish */


		/* dealing with incomingTextArea */
		incomingTextArea = new JTextArea(15, 30);
		incomingTextArea.setLineWrap(true);
		incomingTextArea.setWrapStyleWord(true);
		incomingTextArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(incomingTextArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scroller);
		/* finish */


		/* dealing with outgoingTextField */
		outgoingTextField = new JTextField(20);
		outgoingTextField.addActionListener(new SendListener());
		mainPanel.add(outgoingTextField);
		/* finish */


		/* dealing with sendButton */
		JButton sendButton = new JButton("Send");
		sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		sendButton.addActionListener(new SendListener());
		mainPanel.add(sendButton);
		/* finish */
		/* GUI finish */


		//setUpNetworking();


		frame.setBounds(70, 70, 390, 350);
		frame.setVisible(true);
	}

	public void setUpNetworking(String serverIP) {
		try {
			socket = new Socket(InetAddress.getByName(serverIP), 5000);
			System.out.println(socket.toString());
			writer = new PrintWriter(socket.getOutputStream());
			InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isReader);
			System.out.println("Networking established.");
			incomingTextArea.setForeground(Color.GREEN);
			incomingTextArea.append("Networking established.\n");
			incomingTextArea.append("**********************************\n");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/* inner class */
	public class NetworkConnectListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setUpNetworking(serverIPTextField.getText());
			Thread thread = new Thread(new ReadBroadCast());
			thread.start();
		}
	}

	/* inner class */
	public class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				writer.println(outgoingTextField.getText());
				writer.flush();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			outgoingTextField.setText("");
			outgoingTextField.requestFocus();
		}
	}

	/* inner class */
	/*
	public class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent event) {
			System.out.println("Mouse clicked: " + event.getButton());
		}

		public void mouseEntered(MouseEvent event) {
			System.out.println("Mouse entered");
		}

		public void mouseExited(MouseEvent event) {
			System.out.println("Mouse exited");
		}

		public void mousePressed(MouseEvent event) {
			System.out.println("Mouse pressed");
		}

		public void mouseReleased(MouseEvent event) {
			System.out.println("Mouse released");
		}
	}
	*/

	/* inner class */
	/*
	public class MyMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent event) {
			event.translatePoint(10, 0);
			System.out.println("Mouse dragged: " + "x = " + event.getX() + " y = " + event.getY());
		}

		public void mouseMoved(MouseEvent event) {
			System.out.println("Mouse moved: " + "x = " + event.getX() + " y = " + event.getY());
		}
	}
	*/

	/* inner class */
	public class ReadBroadCast implements Runnable {
		public void run() {
			try {
				String msg;
				incomingTextArea.setForeground(Color.BLACK);
				while((msg = reader.readLine()) != null) {
					System.out.println("Read: " + msg);
					incomingTextArea.append(msg + "\n");
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}

