
// Java program to illustrate Client side 
// Implementation using DatagramSocket 
import java.io.*;
import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.Buffer.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client_new {
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		boolean udp = true;
		boolean tcp = true;
		String dns = null;
		String protocol = null;
		String format = "";
		int port = 0;
		// Step 1:Create the socket object for
		// carrying the data.

		if (args.length == 0) {
			System.out.println("Usage: [<protocol>:]<server>[:<port>] [-<format>] \n");
			System.out.println("supported protocols: tcp, udp ");
			System.out.println("supported formats (in strings): cdr,xdr, asn");
			return;
		}
		String temp;
		for (int i = 1; i < args.length; i++) {
			temp = args[i].toLowerCase();
			if (temp.contains("-")) {
				if (temp.contains("cdr")) {
					format = "CDR";
				}
				if (temp.contains("xdr")) {
					format = "XDR";
				}
				if (temp.contains("ans")) {
					format = "ANS";
				}
				if (temp.contains("object")) {
					format = "OBJECT";
				}
			}
		}
		String[] input = args[0].split(":");
		if (input.length == 3) {
			protocol = input[0];
			dns = input[1].toLowerCase();
			port = Integer.parseInt(input[2]);
		}
		if (input.length == 1) {
			dns = input[0].toLowerCase();

		} else { // input is 2 long
			if (input[0].toLowerCase().equals("udp") || input[0].toLowerCase().equals("tcp")) {
				protocol = input[0].toLowerCase();
				dns = input[1].toLowerCase();
			} else {
				dns = input[0].toLowerCase();
				port = Integer.parseInt(input[1]);

			}
		}

		if (port == 0)
			port = 1234;
		if (protocol == null)
			protocol = "udp";

		DatagramSocket ds = new DatagramSocket();
		ds.setSoTimeout(1000);

		InetAddress ip = InetAddress.getByName(dns);
		byte buf[] = null;
		byte[] receive = new byte[65535];
		DatagramPacket DpReceive = null;
		// loop while user not enters "bye"

		if (protocol.equals("udp")) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			FileOutputStream outudp = new FileOutputStream("udpresults.txt");
			int failed = 0;
			int i;
			Date date = new Date();
			String time0 = formatter.format(date);
			for (i = 0; i < 100000; i++) {
				String inp = "REQUEST FULL" + i;

				// convert the String input into the byte array.
				if (!format.equals("")) {
					inp += "=" + format;
				}
				System.out.println(inp);
				buf = inp.getBytes();

				/*
				 * else if(format.equals("OBJECT")) {}
				 */
				// Step 2 : Create the datagramPacket for sending
				// the data.
				DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port);

				// Step 3 : invoke the send call to actually send
				// the data.
				ds.send(DpSend);
				DpReceive = new DatagramPacket(receive, receive.length);
				try {
					ds.receive(DpReceive);
				} catch (SocketTimeoutException e) {
					System.out.println("Package missing, Resend");
					i--;
					failed++;
				}
				byte[] array;
				int length = DpReceive.getLength();
				array = DpReceive.getData();
				if(format.equals("OBJECT")) {
					
					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(array));
					try{
						date = (Date) iStream.readObject();
						System.out.println("Server(UDP):-" + formatter.format(date));
						
						}
					catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
					iStream.close();
					continue;
					
				}
				System.out.println("Server(UDP):-" + new String(array) + " " + length);
				if (format.equals("ANS")) {
					Converter.printbyte(array[0]);
				}

				for (int j = 0; j < 24; j++) {
					System.out.printf("0x%h ", array[j]);
				}
				System.out.println("");

				// break the loop if user enters "bye"
				// if (inp.equals("bye"));
			}
			Date date2 = new Date();
			String result = "Overall packages:" + ((int) i + failed) + "\nFailures " + failed + "\nTime started: "
					+ time0 + "\nTime ended " + formatter.format(date2);
			long diffInMillies = Math.abs(date2.getTime() - date.getTime());
			result += "\nResult in Milliseconds" + diffInMillies;
			System.out.println(result);
			outudp.write(result.getBytes());

		}

		if (protocol.equals("tcp")) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			FileOutputStream outudp = new FileOutputStream("tcpresults.txt");
			int failed = 0;
			int i;
			Date date = new Date();

			for (i = 0; i < 100000; i++) {
				Socket socket = new Socket(dns, port);
				InputStream in = socket.getInputStream();
				if (format.equals("OBJECT")) {
					//System.out.println("send object request");
					
					OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "US-ASCII");
					String inp = "REQUEST FULL";
					inp += "=" + format;
					System.out.println(inp);
					out.write(inp + " " + i);
					out.flush();
					ObjectInputStream objectInput = new ObjectInputStream(in);
					try {
						try {
							Object full = (String) objectInput.readObject();
							String received = (String) full;
							full = (Date) objectInput.readObject();
							date = (Date) full;
							System.out.println(formatter.format(date)+" "+received);
						
							
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					} catch (IOException e) {
						e.printStackTrace();
						
					}
					objectInput.close();
					in.close();
					out.close();
					socket.close();
					continue;
					
				}
				OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "US-ASCII");
				String inp = "REQUEST FULL";
				if (!format.equals("")) {
					inp += "=" + format;
				}
				System.out.println(inp);
				out.write(inp + " " + i);
				out.flush();

				System.out.print("Server(TCP):-");
				/*
				 * BufferedReader reader = new BufferedReader(new InputStreamReader(in)); char[]
				 * a = new char[1024]; reader.read(a); System.out.println(a);
				 */
				byte[] array = new byte[1024];
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				for (int read = in.read(array, 0, array.length); read != -1; read = in.read()) {
					buffer.write(array, 0, read);
				}
				array = buffer.toByteArray();
				/*
				 * for (byte j: array) { System.out.printf("0x%h ", j); }
				 */
				System.out.println("");
				if (format.equals("ANS")) {
					Converter.printbyte(array[0]);
				}
				System.out.println(new String(array));
				// in.flush();
				// reader.close();
				out.close();
				in.close();
				socket.close();
				// System.out.print("End of Message");
			}
			Date date2 = new Date();
			String result = "Overall messages:" + ((int) i + failed) + "\nFailures " + failed + "\nTime started: "
					+ formatter.format(date) + "\nTime ended " + formatter.format(date2);
			long diffInMillies = Math.abs(date2.getTime() - date.getTime());
			result += "\nResult in Milliseconds " + diffInMillies;
			System.out.println(result);
		}
	}

	public static StringBuilder data(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}
}
