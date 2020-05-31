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

public class server_new {
	public static void main(String[] args) throws IOException {
		// Step 1 : Create a socket to listen at port 1234
		/*
		 * DatagramSocket ds = new DatagramSocket(1234); byte[] receive = new
		 * byte[65535]; byte buf[] = null; DatagramPacket DpReceive = null;
		 * DatagramPacket DpSend = null;
		 */
		int port;
		if (args.length == 0) {
			port = 1234;
			System.out.println("Usage: <port>\nStandartport=1234");
		} else {
			port = Integer.parseInt(args[0]);
		}
		Thread u = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket(1234);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				byte[] receive = new byte[65535];
				byte buf[] = null;
				DatagramPacket DpReceive = null;
				DatagramPacket DpSend = null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				while (true) {
					try {
						// Step 2 : create a DatgramPacket to receive the data.
						DpReceive = new DatagramPacket(receive, receive.length);

						// Step 3 : revieve the data in byte buffer.
						ds.receive(DpReceive);

						System.out.println("Client(UDP):-" + data(receive));

						// Exit the server if the client sends "bye"

						if (data(receive).toString().equals("bye")) {
							System.out.println("Client sent bye.....EXITING");
							break;
						}
						String received = data(receive).toString();
						
						if (received.contains("REQUEST")) {
							//System.out.println("Client requested");
							byte[] requesthead = "POST ".getBytes();
							byte[] temp = new byte[1024];
							if (received.contains("=")) {
								if (received.contains("XDR")) {
									temp = Converter.xdr();
								} else if (received.contains("CDR")) {
									temp = Converter.cdr();
								} else if (received.contains("ANS")) {
									temp = Converter.asn();
								} else if (received.contains("OBJECT")) {

								} 
							}
							else {
								temp = Converter.cdr();
							}
						/*	Date date = new Date();
							String time = formatter.format(date);
							time = "POST" + time; */
							buf = Converter.concat(requesthead, temp);
							System.out.println("buf: "+new String(buf));
							DpSend = new DatagramPacket(buf, buf.length, DpReceive.getAddress(), DpReceive.getPort());
							ds.send(DpSend);

						}

						// Clear the buffer after every message.
						receive = new byte[65535];
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		u.start();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ServerSocket server = new ServerSocket(1234);
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					while (true) {
						Socket socket = server.accept();
						InputStream in = socket.getInputStream();
						OutputStream out = socket.getOutputStream();
						System.out.print("Client(TCP):-");
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						char[] a=new char[1024];
						reader.read(a);
						//System.out.println(a);
						byte [] temp = null;
						String received = new String(a);
						System.out.println(received);
						if (received.contains("=")) {
							if (received.contains("XDR")) {
								temp = Converter.xdr();
							} else if (received.contains("CDR")) {
								temp = Converter.cdr();
							} else if (received.contains("ANS")) {
								temp = Converter.asn();
							} else if (received.contains("OBJECT")) {

							} 
							}
						else {
							temp = Converter.cdr();
						}
						//String read =""+ reader.read();
						
					/*	Date	 date = new Date();
						String time = formatter.format(date);
						System.out.println(time); */
						out.write(temp);

						// out.write("My reply".getBytes());
						// System.out.println("My reply");
						// DataOutputStream outData = new DataOutputStream(socket.getOutputStream())
						// outData.flush();
						out.flush();
						in.close();
						out.flush();
						out.close();
						socket.close();
						// System.out.print("Close Socket");
					}
					// server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	// A utility method to convert the byte array
	// data into a string representation.
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