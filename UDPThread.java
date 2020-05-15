
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



public class UDPThread extends Thread {

    //private UDPThread t;
    private String Threadname;
    private int port;


    public UDPThread(String Threadname,int port){
        this.Threadname = Threadname;
        this.port = port ; 
    }

    public void start(){

        System.out.println("Starting: "+Threadname);

    }

    public void run(){
        
        DatagramSocket serverUDP =null;
        
        try{
        ip = InetAddress.getLocalHost(); }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try {
           serverUDP = new DatagramSocket(port,ip);
           serverUDP.setSoTimeout(100000);
           //binds
           
       } catch (SocketException e1) {
           e1.printStackTrace();
       }   

       while(true)serveUDP(serverUDP);


    }

    public void serveUDP(DatagramSocket serverUDP) {
		byte buf[] = null;
		String time = java.time.LocalTime.now().toString();
         System.out.println(time);
         // convert the String input into the byte array. 
         buf = time.getBytes(); 
         // Step 2 : Create the datagramPacket for sending 
         // the data. 
         DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port); 
         // Step 3 : invoke the send call to actually send 
         // the data. 
         try {
        	 serverUDP.setSoTimeout(10);
			serverUDP.send(DpSend);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	} 

    
}