package app;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

public class Socketssend implements Runnable {
	Thread t;
	JTextField outgoing;
	static JTextArea incomming;
	static DataOutputStream writer;
	static DataInputStream streamreader;
	FileWriter fwrite;
	static Socket sock;
	static BufferedReader br;
	static BufferedWriter bws;
	BufferedWriter WServerd;
	Socket threadName;
	static BufferedReader brs;
	static ServerSocket serversock;
	public final static int port = 4809;
	static int count = 0;
	AudioClip currentSound;
	static ArrayList<Socket> connections;

	Socketssend() {

	}

	public void gui() {
		JFrame frame = new JFrame("BIgBUg Chat SERVER");
		JPanel mainpanel = new JPanel();
		outgoing = new JTextField(30);

		incomming = new JTextArea(20, 30);
		incomming.setLineWrap(true);
		incomming.setEditable(true);
		JScrollPane Scroll = new JScrollPane(incomming);
		Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JButton sendimage = new JButton("IMAGE");
		JButton recvoice = new JButton("VOICE");
		JButton sendpdf = new JButton("PDF");
		JButton sendbutton = new JButton("Send");
		outgoing.addKeyListener(new KeysButtonListener());
		sendbutton.addActionListener(new SentButtonListener());
		sendimage.addActionListener(new ImageButtonListener());
		recvoice.addActionListener(new SentButtonListener());
		sendpdf.addActionListener(new SentButtonListener());
		incomming.setBackground(Color.darkGray);
		incomming.setForeground(Color.WHITE);
		mainpanel.setBackground(Color.LIGHT_GRAY);
		mainpanel.setForeground(Color.WHITE);
		mainpanel.add(Scroll);
		mainpanel.add(outgoing);
		mainpanel.add(sendbutton);
		mainpanel.add(sendimage);
		mainpanel.add(recvoice);
		mainpanel.add(sendpdf);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, mainpanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainpanel);
		frame.getContentPane().add(BorderLayout.SOUTH, sendbutton);
		frame.setResizable(false);

		frame.setSize(400, 440);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // SET NETWORK
	}

	Socketssend(Socket name) {
		threadName = name;
		System.out.println("Creating " + threadName);

	}

	public void run() {
		try {
			while (sock.isConnected()) {
				bws = new BufferedWriter(new FileWriter("Server.txt", true));
				WServerd = new BufferedWriter(new FileWriter("Server.txt", true));
				writer = new DataOutputStream(sock.getOutputStream());
				streamreader = new DataInputStream(sock.getInputStream());
				String income = streamreader.readUTF(); // convert to string
														// income data
				WServerd.write("Client : ");
				WServerd.write(income); // write data on client.txt file
				WServerd.write("۵");
				System.out.println(income);
				incomming.setText(incomming.getText().trim() + "\n Client A :" + income + "\n");

				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // FLUSH WRITe

	public void start() {
		int count = 0;
		System.out.println("Starting " + threadName);
		if (t == null){
			count++;
			t = new Thread(this, "" + count);
			System.out.println("Client: " + t);
			t.setName("Client" + count);
			t.setPriority(8);
			t.start();
		}
	}

	public class KeysButtonListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					// message to save in Servr.txt file
					String msgsav = outgoing.getText();
					bws.write(msgsav);
					bws.write("۵");
					bws.flush();
					System.out.println("Done Server.");
					// message to send out to client
					writer.writeUTF(outgoing.getText());
					writer.flush();
					writer.close();
					incomming.append(msgsav + "\n");
				} catch (IOException ex) {
					//
				}
				outgoing.setText("");
				outgoing.requestFocus();
			}
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}// write and send Text

	public class SentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				writer.writeUTF(outgoing.getText());
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				//
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // main method

	public class ImageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent av) {

		}
	}

	public static void main(String args[]) {
		Socketssend g = new Socketssend();
		g.gui();
		connections = new ArrayList<Socket>();
		try {
			br = new BufferedReader(new FileReader("Server.txt"));
			String readServersenddata = br.readLine();
			for (int i = 0; i < readServersenddata.length(); i++) {
				if (readServersenddata.charAt(i) == '۵') {
					incomming.append("\n");
				} else
					incomming.append(readServersenddata.charAt(i) + "");
			}
		} catch (Exception e) {
		}
		try {
			serversock = new ServerSocket(port);
			boolean run = true;
			while (run) {
				System.out.println("Connecting to the client....");
				sock = serversock.accept();
				Socketssend r1 = new Socketssend(sock);

				count++;
				r1.start();
				System.out.println("Connection Established with :" + sock.getRemoteSocketAddress());
				System.out.println("Connected Clients : " + count);
				connections.add(sock);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
				int count = 0;

				System.out.println(
						"__________________________________________________________________________________________");
				System.out.println(
						"|CLIENT ID |       IP ADDRESS      |   PORT   |       SERVER PORT   |   Date   |   Time  |");
				System.out.println(
						"__________________________________________________________________________________________");
				for (Socket obj : connections) {

					if (obj != null) {
						count++;
						System.out.println("|    " + count + ".    | " + obj + "      |"
								+ dateFormat.format(cal.getTime()) + " |");
			  System.out.println(
						"__________________________________________________________________________________________");
					}
				}

			}
		} catch (IOException t) {
		}

		/*
		 * int random = 1; while(random == 1){
		 * System.out.println("before set."); sook.setnetwork();
		 * System.out.println("After set."); }
		 */

	}
}