package app;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientA {
	JTextField outgoing;
	static JTextArea incomming;
	JOptionPane errordialog;
	DataOutputStream writer;
	DataInputStream inputstream;
	static BufferedReader br;
	static BufferedWriter Wclientd;
	Socket soce;
	JButton sendbutton;
	ByteArrayOutputStream byteArrayOutputStream;
	FileWriter fwrite;
	BufferedWriter bw;

	public void gui() { // GUI SET UP
		JFrame frame = new JFrame("BIgBUg Chat Client");
		JPanel mainpanel = new JPanel();
		outgoing = new JTextField(30);
		incomming = new JTextArea(20, 30);
		incomming.setLineWrap(true);
		incomming.setEditable(true);
		JScrollPane Scroll = new JScrollPane(incomming);
		Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sendbutton = new JButton("Send");
		JButton sendimage = new JButton("IMAGE");
		JButton recvoice = new JButton("VOICE");
		JButton sendpdf = new JButton("PDF");
		outgoing.addKeyListener(new KeyButtonListener());
		sendimage.addActionListener(new ImageButtonListener());
		recvoice.addActionListener(new SendButtonListener());
		sendpdf.addActionListener(new SendButtonListener());
		sendbutton.addActionListener(new SendButtonListener());
		incomming.setBackground(Color.BLUE);
		incomming.setForeground(Color.WHITE);
		mainpanel.setBackground(Color.ORANGE);
		mainpanel.setForeground(Color.WHITE);
		mainpanel.add(Scroll);
		mainpanel.add(outgoing);
		mainpanel.add(sendbutton);
		mainpanel.add(sendimage);
		mainpanel.add(recvoice);
		mainpanel.add(sendpdf);
		frame.getContentPane().add(BorderLayout.CENTER, mainpanel);
		frame.getContentPane().add(BorderLayout.SOUTH, sendbutton);
		frame.setResizable(false);
		frame.setSize(400, 440);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setnetwork() // network AND FILE TRANSFERING AND RETRIVING
	{
		try {
			soce = new Socket("127.0.0.1", 4809); // CONNECTING TO SERVER ON IP
													// AND PORT ADDRESS
			System.out.println("Network Established");
			bw = new BufferedWriter(new FileWriter("Client1.txt", true)); // take
																			// file
			Wclientd = new BufferedWriter(new FileWriter("Client1.txt", true));

			System.out.println("hello");
			writer = new DataOutputStream(soce.getOutputStream()); // send data
																	// to server
			inputstream = new DataInputStream(soce.getInputStream()); // take
																		// incomming
																		// data
																		// from
																		// server
			String sermsgin = inputstream.readUTF(); // conver to string income
														// data
			Wclientd.write("Server : ");
			Wclientd.write(sermsgin); // write data on client.txt file
			Wclientd.write("۵");
			Wclientd.flush();
			incomming.setText(incomming.getText().trim() + "\n Server :" + sermsgin + "\n"); // show
																								// income
																								// message
																								// on
																								// textarea

		} catch (IOException e) {
			//
		}
	}

	public class KeyButtonListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) { // ON ENTER DATA
														// TRANSFERS TO SERER
														// AND CLIENT.TXT
				try {

					String msgsave = outgoing.getText();
					bw.write(msgsave); // write data on client.txt file
					bw.write("۵");
					bw.flush(); // flushes data from ram to file parmetly
					System.out.println("Done Client.");
					writer.writeUTF(outgoing.getText()); // send input data to
															// server
					writer.flush(); // flush the input data from ram to server
					writer.close(); // closer writer object of datainputstream
					incomming.append(msgsave + "\n"); // append the outgoing
														// message on text area
														// of client
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

			// TODO Auto-generated method stub

		}
	}// write and send Text

	public class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				writer.writeUTF(outgoing.getText());
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				//
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // main method

	public class ImageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent av) {
			/*
			 * try{ FileOpenService fileOpenService = (FileOpenService)
			 * ServiceManager.lookup("javax.jnlp.FileOpenService");
			 * }catch(Exception e){}
			 */
		}
	}

	public static void main(String[] args) {
		ClientA omar = new ClientA();
		omar.gui();
		int count = 0;
		Boolean ran = true;
		try {
			br = new BufferedReader(new FileReader("Client1.txt"));
			String readclientsenddata = br.readLine();
			for (int i = 0; i < readclientsenddata.length(); i++) {
				if (readclientsenddata.charAt(i) == '۵') {
					incomming.append("\n");
				} else
					incomming.append(readclientsenddata.charAt(i) + "");
			}
		} catch (IOException e) {
		}
		while (ran) {
			omar.setnetwork();
			count++;
			System.out.println(count);
		}
	}
}
