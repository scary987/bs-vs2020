
// Java program to illustrate Client side 
// Implementation using DatagramSocket 
import java.io.*;
import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		int port = 0;
		// Step 1:Create the socket object for
		// carrying the data.

		if (args.length == 0) {
			System.out.println("Usage: [<protocol>:]<server>[:<port>]\n");
			return;
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
		ds.setSoTimeout(100);

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
				String inp = "REQUEST TIME" + i;

				// convert the String input into the byte array.
				System.out.println(inp);
				buf = inp.getBytes();

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
				System.out.println("Server(UDP):-" + data(DpReceive.getData()));

				// break the loop if user enters "bye"
				// if (inp.equals("bye"));
			}
			Date date2 = new Date();
			String result = "Overall packages:" + i + failed + "\nFailures " + failed + "\nTime started: " + time0
					+ "\nTime ended " + formatter.format(date2);
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
				OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "US-ASCII");
				out.write("REQUEST TIME" + i);
				System.out.println("REQUEST TIME" +i);
				out.flush();

				System.out.print("Server(TCP):-");
				for (int read = in.read(); read != -1; read = in.read()) {
					System.out.print((char) read);
				}
				System.out.println();
				// in.flush();
				out.close();
				in.close();
				socket.close();
				// System.out.print("End of Message");
			}
			Date date2 = new Date();
			String result = "Overall messages:" + i + failed + "\nFailures " + failed + "\nTime started: "
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
