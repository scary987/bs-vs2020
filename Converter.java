import java.io.*;
import java.nio.Buffer.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.*;
import java.lang.reflect.Array;  
public class Converter

 {
	static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	
	public static byte[] cdr( Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month =(byte) (cal.get(Calendar.MONTH)+1);
		int year = cal.get(Calendar.YEAR);
		//int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		//String time = ""+year+month+day+'\0';
		Date now = cal.getTime();
		long timePortion = now.getTime() % MILLIS_PER_DAY;
		String time = ""+year+"//"+month+"//"+day+"//"+timePortion+'\0';
		//System.out.printf("%d%d%d%d\n",year,month,day,timePortion);
	//	System.out.printf(time);
	//	System.out.println("\n");
	//	System.out.println(time.length());
		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		/*for (byte b : bytes) {
   			System.out.format("0x%x ", b);
		}*/
		byte[] a;
		
		a = time.getBytes();
		//System.out.println(a+" "+bytes);
		byte[] c = concat(bytes,a);
		
		//for(byte b:c)System.out.println(b);
		return c;
	}
	public static byte[] xdr() {
		return xdr(new Date());
	}
	public static byte[] xdr( Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month =(byte) (cal.get(Calendar.MONTH)+1);
		int year = cal.get(Calendar.YEAR);
		//int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		//String time = ""+year+month+day+'\0';
		Date now = cal.getTime();
		long timePortion = now.getTime() % MILLIS_PER_DAY;
		String time = ""+year+"//"+month+"//"+day+"//"+timePortion+'\0';
		//System.out.printf("%d%d%d%d\n",year,month,day,timePortion);
	//	System.out.printf(time);
	//	System.out.println("\n");
	//	System.out.println(time.length());
		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		/*for (byte b : bytes) {
   			System.out.format("0x%x ", b);
		}*/
		byte[] a;
		
		a = time.getBytes();
		//System.out.println(a+" "+bytes);
		byte[] c = concat(bytes,a);
		if (c.length % 4 != 0) {
			a = new byte[c.length%4];
			for(byte byt:a) {
				byt=0;
			}
			return concat(c,a);
		}
		
		//for(byte b:c)System.out.println(b);
		return c;
	}
	
	public static String xcdrtostring(byte[] b ) {
		
		int a = byteArrayToInt(b);
		//System.out.println("This is a: "+a);
		byte[] c = new byte[b.length-4];
		System.arraycopy(b, 4, c, 0, a);
		
		return new String(c);
	}
	
	public static  byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}
	public static int byteArrayToInt(byte[] b) 
	{
		//Big Endian in both cases
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}

	public static byte[] intToByteArray(int a)
	{
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),   
	        (byte) ((a >> 8) & 0xFF),   
	        (byte) (a & 0xFF)
	    };
	}
	public static byte[] cdr() {
		return cdr (new Date());
	}
	public static byte indentifier(int id,boolean primary) {
		
		byte a = (byte) 0xFF;
		String s1 = String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0');
		 a&=3; // sets first 2 bits 2 11, private mode in ans
		System.out.println(s1);
		return 0;
	}
	public static void main(String[] args) {
		Date date = new Date();
		byte[] cdr =cdr(date);
		byte[] xdr = xdr(date);
		//System.out.println("This is a: "+a);
		//byte[] c = new byte[b.length-4];
		System.out.print("CDR: ");
		for(byte c:cdr)System.out.print(c+" ");
		System.out.print("\n");
		
		System.out.print("XDR: ");
		for(byte c:xdr)System.out.print(c+" ");
		System.out.print("\n");
		System.out.println(xcdrtostring(cdr));
		System.out.println(xcdrtostring(xdr));
		indentifier(0,false);
	}

}
