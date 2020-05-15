
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.*;
import java.lang.*;



public class TimeServer  {
	
	private static InetAddress ip;
	private static int port;
	
	private	static String protocol;
	
	public static void main(String[] args){
		
		
		if (args.length == 0) {
			port=2345; //Defaultport
			//System.out.println("Listening on port "+portTCP+" for TCP and "+portUDP+" for UDP");
		}
		 else {
			try{ port = Integer.parseInt(args[0]);}
			catch(Exception e){e.printStackTrace();}
			}
			
			DatagramSocket serverUDP =null;
        
			try{
			ip = InetAddress.getLocalHost(); }
			catch(Exception e){
				e.printStackTrace();
			}
			
			try {
			   serverUDP = new DatagramSocket(port,ip);
			  
			   //binds
			   
		   } catch (SocketException e1) {
			   e1.printStackTrace();
		   }
		   ServerSocket serverTCP = null;
		   try 
		   {
			   serverTCP = new ServerSocket(port);
			   //binds
			   
			   //
		   } catch (SocketException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   serve(serverTCP, serverUDP);
	}
	
	public static void serve(ServerSocket serverTCP, DatagramSocket serverUDP) {
			try{
			Thread t = new Thread(new Runnable(){
			
				@Override
				public void run()  {
					// TODO Auto-generated method stub
					try{
					serveTCP(serverTCP);}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			Thread u = new Thread(new Runnable(){
			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					serveUDP(serverUDP);
				}
			});
			t.start();
			System.out.println("Started TCP Listener Thread");
			//t.sleep(2);
			u.start();
			System.out.println("Started UDP Listener Thread");
			//u.sleep(2);
			 }
			catch(Exception e){
				e.printStackTrace();
			}
		
	}
	
	
	public static void serveUDP(DatagramSocket serverUDP) {
		while(true){
		byte buf[] = null;
		byte[] buf1 = new byte[1024];
		DatagramPacket q = new DatagramPacket(buf1, buf1.length);
		try{
			System.out.println("Waiting for UDPPacket");
			serverUDP.receive(q);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		String time = java.time.LocalTime.now().toString();
         System.out.println(time);
         // convert the String input into the byte array. 
         buf = time.getBytes(); 
         // Step 2 : Create the datagramPacket for sending 
		 // the data. 
		 
         DatagramPacket DpSend = new DatagramPacket(buf, buf.length,q.getAddress(), q.getPort()); 
         // Step 3 : invoke the send call to actually send 
         // the data. 
         try {
			serverUDP.send(DpSend);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} }
	} 
	
	public static void serveTCP(ServerSocket serverTCP) throws Exception  {
		
			while(true){
			System.out.println(serverTCP.getInetAddress());
			System.out.println("TCP Waiting for client " + serverTCP.getLocalPort());
			System.out.println("Waiting for TCPPacket");
			Socket socket =serverTCP.accept();

			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			//hier stoppt es. 
			System.out.println("punkt a");
			//System.out.println(serverTCP.getRemoteSocketAddress());
			
			out.write(("Server "+java.time.LocalTime.now().toString()).getBytes());
			System.out.println("punkt b");
			
			in.close(); out.flush(); out.close(); 
			socket.close();}
		
			//System.out.println("c");
	}
	



}
