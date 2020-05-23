import java.io.*;
import java.nio.Buffer.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.*;
public class Converter {

	
	
	
	public static byte[] xdr( Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month =(byte) (cal.get(Calendar.MONTH)+1);
		int year = cal.get(Calendar.YEAR);
		//int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		
		
		System.out.printf("%d%d%d",year,month,day);
		
		
		
		
		return new byte[1];
	}
	public static String tostring(byte[] s ) {
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
