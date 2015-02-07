package utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class util {
	
	public  double[] arraylist_to_array(ArrayList<Double> arrayList){
		
		int size=arrayList.size();
		
		double[] array=new double[size];
		
		for(int i=0;i<size;i++)
			array[i]=arrayList.get(i);
			

		return array;
	}
	
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Date strToDatetime(String str) {
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

	public static String utf8ToBig5(String inputStr) throws Exception {
		StringBuffer result = new StringBuffer();
		CharsetEncoder enc = Charset.forName("Big5").newEncoder();

		for (int i = 0; i < inputStr.length(); i++) {
			char c = inputStr.charAt(i);
			String utf8 = String.valueOf(c);

			int ansi = (int) c;
			if (!enc.canEncode(c)) {
				utf8 = "&#" + ansi + ";";
			}
			result.append(utf8);
		}

		return result.toString();
	}

}
