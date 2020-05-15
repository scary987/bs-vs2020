
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

public class TimeServer {

	static InetAddress ip;
	static int port = 0;
	static String protocol;
	static String dns;

	public static void main(String[] args) {
		// port = Integer.parseInt(args[0]);

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
			if (input[0].toLowerCase().equals( "udp") || input[0].toLowerCase().equals("tcp")) {
				protocol = input[0].toLowerCase();
				dns = input[1].toLowerCase();
			} else {
				dns = input[0].toLowerCase();
				port = Integer.parseInt(input[1]);

			}
		}
		if (protocol == null) {
			protocol = "udp";
			
		}
		if (port == 0) {
			if (protocol.equals("udp"))port=123; //ntp for udp
			else port=37; //ntp for tcp
		}

		//ServerSocket serverTCP = null;
		DatagramSocket serverUDP = null;
		// will need udp for dns either way
		try {
			serverUDP = new DatagramSocket();
			serverUDP.setSoTimeout(100000);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		System.out.println("Sending on Port: " +port+ " using " + "dns" + "\n");
		// Read DNS to ip
		if (ip == null) {
			try {
				// ip = InetAddress.getLocalHost();
				dnsclient(dns);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Sending on Port: " + port + "+ to IP-Adress: " + ip.toString() + " using " + protocol + "\n");
		if(protocol=="tcp") {
			try {
				runTCP();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else {
			
			runUDP(serverUDP);
		}

		// running(serverTCP, serverUDP);

	}

	public static void runTCP() throws Exception  {
		
		//System.out.println("TCP Waiting for client " + serverTCP.getLocalPort());
		Socket socket = new Socket(dns,port);
		InputStream in = socket.getInputStream();
		OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(),"US_ASCII");
		out.write("GET / HTTP/1.0\r\n\r\n");
		out.flush();

		for( int read=in.read(); read!=-1; read=in.read() ) {
			System. out .print((char)read);
			}

		in.close();
		out.close();
		socket.close();
		


	}

	public static void runUDP(DatagramSocket serverUDP) {
		byte buf[] = null;

		String time = java.time.LocalTime.now().toString();
		//System.out.println(time);

		// convert the String input into the byte array.
		buf = time.getBytes();

		// Step 2 : Create the datagramPacket for sending
		// the data.
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port);

		// Step 3 : invoke the send call to actually send
		// the data.
		DatagramPacket DpReceive = new DatagramPacket(new byte[32], 32, ip, port);
		try {
			System.out.println("Send UDP Packet:");
			serverUDP.send(DpSend);
			System.out.println("Waiting for Answer:");
			serverUDP.receive(DpReceive);
			String output = DpReceive.getData().toString();
			System.out.print("Time of received Package: " + output);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		serverUDP.close();
	}

	/*
	 * public static void running(ServerSocket serverTCP, DatagramSocket serverUDP)
	 * {
	 * 
	 * ip = null; try { ip = InetAddress.getLocalHost(); } catch
	 * (UnknownHostException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); }
	 * 
	 * while (true) { System.out.println("Running a tcp client"); runTCP(serverTCP);
	 * System.out.println("Running a udp client"); runUDP(serverUDP); } // }
	 */

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

	public static void dnsclient(String dns) throws Exception {
		InetAddress google = InetAddress.getByName("8.8.8.8");
		// byte[] buf = new byte[1024];
		// buf = dns.getBytes();
		// query (args[0],buf);

		DatagramSocket s = new DatagramSocket();
		byte[] buf = new byte[1024];
		try {
			DatagramPacket p = new DatagramPacket(buf, query(dns,buf), google, 53);
			System.out.println("sending message to google's dns server");
			s.send(p); //Exception thrown right here 
			} 
		catch(Exception e ) {
			
			e.printStackTrace();
			s.close();
			return;
			
		}
		System.out.println("Receive");

		DatagramPacket q = new DatagramPacket(buf, buf.length);

		s.receive(q);
		ip = InetAddress.getByAddress(Arrays.copyOfRange(q.getData(), q.getLength() - 4, q.getLength()));
		System.out.println(ip);
		
		s.close();
	}

	public static int query(String name, byte[] buf) throws Exception {
		byte[] header = { 0x08, 0x15, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		byte[] trailer = { 0x00, 0x00, 0x01, 0x00, 0x01 };
		ByteBuffer query = ByteBuffer.wrap(buf);
		query.put(header);
		// encode each label of the domain name
		
			for (String s : name.split("\\.")) {
				query.put((byte)s.length()).put(s.getBytes("ascii"));
			}	
		
	
		// write trailer
		query.put(trailer);
		// return length of query
		return query.position();
	}
}
