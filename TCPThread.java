
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


public class TCPThread extends Thread {


    //private TCPThread t;
    public int port;
    public String Threadname;

    public TCPThread(String Threadname, int port){
        this.Threadname = Threadname;
        this.port = port;
    }

    public void run(){

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
        


            while(true)
			serveTCP(serverTCP);
		
		
    }
    public void start(){

        System.out.println("Starting: "+Threadname);

    }
    public  void serveTCP(ServerSocket serverTCP) throws Exception  {
		
		
        System.out.println(serverTCP.getInetAddress());
        System.out.println("TCP Waiting for client " + serverTCP.getLocalPort());
        Socket socket =serverTCP.accept();
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        //hier stoppt es. 
        System.out.println("punkt a");
        //System.out.println(serverTCP.getRemoteSocketAddress());
        
        out.write(("Server "+java.time.LocalTime.now().toString()).getBytes());
        System.out.println("punkt b");
        
        in.close(); out.flush(); out.close(); 
        socket.close();
    
        System.out.println("c");
    }





}