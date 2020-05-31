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

	public static byte[] cdr(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month = (byte) (cal.get(Calendar.MONTH) + 1);
		int year = cal.get(Calendar.YEAR);
		// int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		// String time = ""+year+month+day+'\0';
		Date now = cal.getTime();
		long timePortion = now.getTime() % MILLIS_PER_DAY;
		String time = "" + year + "//" + month + "//" + day + "//" + timePortion + '\0';
		// System.out.printf("%d%d%d%d\n",year,month,day,timePortion);
		// System.out.printf(time);
		// System.out.println("\n");
		// System.out.println(time.length());
		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		/*
		 * for (byte b : bytes) { System.out.format("0x%x ", b); }
		 */
		byte[] a;

		a = time.getBytes();
		// System.out.println(a+" "+bytes);
		byte[] c = concat(bytes, a);

		// for(byte b:c)System.out.println(b);
		return c;
	}

	public static byte[] cdr(String s) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(s.length()).array();
		return concat(bytes, (s + "\0").getBytes());
	}

	public static byte[] xdr() {
		return xdr(new Date());
	}

	public static byte[] xdr(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month = (byte) (cal.get(Calendar.MONTH) + 1);
		int year = cal.get(Calendar.YEAR);
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		Date now = cal.getTime();
		long timePortion = now.getTime() % MILLIS_PER_DAY;
		String time = "" + year + "//" + month + "//" + day + "//" + timePortion + '\0';

		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		/*
		 * for (byte b : bytes) { System.out.format("0x%x ", b); }
		 */
		byte[] a;

		a = time.getBytes();
		// System.out.println(a+" "+bytes);
		byte[] c = concat(bytes, a);
		if (c.length % 4 != 0) {
			a = new byte[c.length % 4];
			for (byte byt : a) {
				byt = 0;
			}
			return concat(c, a);
		}

		// for(byte b:c)System.out.println(b);
		return c;
	}

	public static byte[] xdr(String s) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(s.length()).array();
		byte[] a = s.getBytes();
		a = concat(bytes, a);

		// appends the 0s
		if (a.length % 4 != 0) {
			byte[] c = new byte[a.length % 4];
			for (byte byt : c) {
				byt = 0;
			}
			return concat(a, c);
		}
		return a;
	}

	public static String xcdrtostring(byte[] b) {

		int a = byteArrayToInt(b);
		// System.out.println("This is a: "+a);
		byte[] c = new byte[b.length - 4];
		System.arraycopy(b, 4, c, 0, a);

		return new String(c);
	}

	public static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static int byteArrayToInt(byte[] b) {
		// Big Endian in both cases
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF) };
	}

	public static byte[] cdr() {
		return cdr(new Date());
	}

	public static byte identifier(int id, boolean constructed, boolean cl1, boolean cl2) {

		byte a = (byte) (id | 0);

		if (constructed)
			a |= 32; // 3rd byte, P/C

		/*
		 * 
		 * cl1 ^ cl2 = private cl1 ^ !cl2 = context specific !cl1 ^ cl2 = Application
		 * !cl1 ^ !cl2 = universal
		 */

		if (cl2)
			a |= 64; // 2nd bit
		if (cl1)
			a |= 128; // 1st bit

		return a;
	}

	public static byte idintbyte() {
		return identifier(2, false, false, false);
	}

	public static byte idutf8byte() {
		return identifier(19, true, false, false);
	}

	public static void printbyte(byte a) {
		String s1 = String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0');
		System.out.println(s1);
	}

	// Just for Debugging:
	public static void main(String[] args) {
		Date date = new Date();
		byte[] cdr = cdr(date);
		byte[] xdr = xdr(date);
		// System.out.println("This is a: "+a);
		// byte[] c = new byte[b.length-4];
		System.out.print("CDR: ");
		for (byte c : cdr)
			System.out.print(c + " ");
		System.out.print("\n");

		System.out.print("XDR: ");
		for (byte c : xdr)
			System.out.print(c + " ");
		System.out.print("\n");
		System.out.println(xcdrtostring(cdr));
		System.out.println(xcdrtostring(xdr));
		byte a = idintbyte();
		byte b = idutf8byte();
		byte c = identifier(15, false, false, true);
		printbyte(a);
		printbyte(b);
		printbyte(c);
	}

	public static byte[] asn() {
		return xdr(new Date());
	}

	// same as cdr, but need an extra
	public static byte[] asn(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte month = (byte) (cal.get(Calendar.MONTH) + 1);
		int year = cal.get(Calendar.YEAR);
		// int month = Year.now().
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		// String time = ""+year+month+day+'\0';
		Date now = cal.getTime();
		long timePortion = now.getTime() % MILLIS_PER_DAY;
		String time = "" + year + "//" + month + "//" + day + "//" + timePortion + '\0';
		// System.out.printf("%d%d%d%d\n",year,month,day,timePortion);
		// System.out.printf(time);
		// System.out.println("\n");
		// System.out.println(time.length());
		byte[] bytes = ByteBuffer.allocate(4).putInt(time.length()).array();

		/*
		 * for (byte b : bytes) { System.out.format("0x%x ", b); }
		 */
		byte[] a;

		a = time.getBytes();
		// System.out.println(a+" "+bytes);
		byte[] c = concat(bytes, a);

		// add identifierbyte
		a = new byte[1];
		a[0] = idutf8byte();
		// for(byte b:c)System.out.println(b);
		return concat(a, c);
	}

}
