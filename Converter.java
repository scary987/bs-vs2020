import java.io.*;
import java.nio.Buffer.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.*;
import java.lang.reflect.Array;  
public class Converter {

	
	
	
	public static byte[] xdr( Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month =(byte) (cal.get(Calendar.MONTH)+1);
		int year = cal.get(Calendar.YEAR);
		//int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		String time = ""+year+month+day+'\0';
		
		System.out.printf("%d%d%d\n",year,month,day);
		System.out.printf(time);
		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		for (byte b : bytes) {
   			System.out.format("0x%x ", b);
		}
		byte[] a;
		a = time.getBytes();
		byte[] c = new byte[a.length + bytes.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(bytes, 0, c, a.length, bytes.length);
		
		System.out.println(c);
		return c;
	}
	public static String xdrtostring(byte[] s ) {
			int x= 0;
		for(int i = 0 ; i<17; i++) {
			x+=Math.pow(s[i],17-i );
		}
		
		
		return "";
	}
	
	public static byte[] xdr() {
		return xdr (new Date());
	}
	public static void main(String[] args) {
		byte[] b =xdr();
	}

}
