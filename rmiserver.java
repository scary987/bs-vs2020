import java.io.*;
import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class rmiserver extends UnicastRemoteObject implements rmiinterface  {
	public static Registry r;
	public rmiserver() throws RemoteException {
		
	}
	public static void main(String[] args) throws IOException {
		// Step 1 : Create a socket to listen at port 1234
		/*
		 * DatagramSocket ds = new DatagramSocket(1234); byte[] receive = new
		 * byte[65535]; byte buf[] = null; DatagramPacket DpReceive = null;
		 * DatagramPacket DpSend = null;
		 */
		
		
		System.setSecurityManager(new SecurityManager());
		
		int port;
		if (args.length == 0) {
			port = 1234;
			System.out.println("Usage: <port>\nStandartport=1234");
		} 
		else if(args[0].contains("Djava")) {
			port = 1234;
			System.out.println("Usage: <port>\nStandartport=1234");
		} 
		
		else {
			System.out.println(args[0]);
			port = Integer.parseInt(args[0]);
		}

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					//ServerSocket server = new ServerSocket(1234);
					//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					try {
						rmiclass obj = new rmiclass();
						System.out.println("created rmiserver");
						r = LocateRegistry.createRegistry(port);
						Naming.rebind("//localhost/rmi", obj);
						
					}
						catch(Exception e) {
							System.out.print(e.getCause());
							e.printStackTrace();
							
						}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	public String FULL() throws RemoteException {

		return Converter.Full();
	}

	public String DATE() throws RemoteException{

		return Converter.DateString();
	}
	public long MILLIS() throws RemoteException {
		return Converter.MILLIS();
	}

}