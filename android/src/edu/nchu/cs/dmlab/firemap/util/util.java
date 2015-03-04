package edu.nchu.cs.dmlab.firemap.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import edu.nchu.cs.dmlab.firemap.mjpeg.RecMicToMp3;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class util {
	public static final String LOG_TAG = "wra";
	public static String deviceID,simID;
	public static boolean checkNetwork(Context cnt) {
		ConnectivityManager connMgr = (ConnectivityManager) cnt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
		    return true;
		}
		return false;
	}
	
	public static String getLineNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
	
	public static String getSIMNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		util.simID = tm.getSimSerialNumber();
		return tm.getSimSerialNumber();
	}
	public static String getMobileID(){
		return simID+":"+deviceID;
	}
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		util.deviceID = tm.getDeviceId();
		return tm.getDeviceId();
	}
	public static String loginDB(String url, String sim, String device){
		URL loginUrl;
		//Log.i("gov.wra.sensor", "Err URL " +url + "?sim=" + sim + "&device=" + device);
		StringBuilder repStr = new StringBuilder();
		try {
			loginUrl = new URL(url + "?sim=" + sim + "&device=" + device);
//			Log.i("LOGIN", url + "?sim=" + sim + "&device=" + device);
			BufferedReader in = new BufferedReader(new InputStreamReader(loginUrl.openStream()));
			int byteRead;
	        while ((byteRead = in.read()) != -1) {
	            repStr.append((char) byteRead);
//	            Log.i("Url", String.valueOf(byteRead));
	        }
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.e("URL", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("URL", e.getMessage());
		}
		return repStr.toString().trim();
	}
	public static GPSPoint Cal_TWD97_To_lonlat(double x, double y) {
    	double a = 6378137.0;
    	double b = 6356752.314245;
    	double lon0;
    	double k0 = 0.9999;
    	double dx = 250000;
    
    	lon0 = 121 * Math.PI/180;
    	double dy = 0;
        double e = Math.pow((1- Math.pow(b,2)/Math.pow(a,2)), 0.5);
        x -= dx;
        y -= dy;
        // Calculate the Meridional Arc
        double M = y/k0;
        // Calculate Footprint Latitude
        double mu = M/(a*(1.0 - Math.pow(e, 2)/4.0 - 3*Math.pow(e, 4)/64.0 - 5*Math.pow(e, 6)/256.0));
        double e1 = (1.0 - Math.pow((1.0 - Math.pow(e, 2)), 0.5)) / (1.0 + Math.pow((1.0 - Math.pow(e, 2)), 0.5));
        double J1 = (3*e1/2 - 27*Math.pow(e1, 3)/32.0);
        double J2 = (21*Math.pow(e1, 2)/16 - 55*Math.pow(e1, 4)/32.0);
        double J3 = (151*Math.pow(e1, 3)/96.0);
        double J4 = (1097*Math.pow(e1, 4)/512.0);
        double fp = mu + J1*Math.sin(2*mu) + J2*Math.sin(4*mu) + J3*Math.sin(6*mu) + J4*Math.sin(8*mu);
        // Calculate Latitude and Longitude
        double e2 = Math.pow((e*a/b), 2);
        double C1 = Math.pow(e2*Math.cos(fp), 2);
        double T1 = Math.pow(Math.tan(fp), 2);
        double R1 = a*(1-Math.pow(e, 2))/Math.pow((1-Math.pow(e, 2)*Math.pow(Math.sin(fp), 2)), (3.0/2.0));
        double N1 = a/Math.pow((1-Math.pow(e, 2)*Math.pow(Math.sin(fp), 2)), 0.5);
        double D = x/(N1*k0);
        // 計算緯度
        double Q1 = N1*Math.tan(fp)/R1;
        double Q2 = (Math.pow(D, 2)/2.0);
        double Q3 = (5 + 3*T1 + 10*C1 - 4*Math.pow(C1, 2) - 9*e2)*Math.pow(D, 4)/24.0;
        double Q4 = (61 + 90*T1 + 298*C1 + 45*Math.pow(T1, 2) - 3*Math.pow(C1, 2) - 252*e2)*Math.pow(D, 6)/720.0;
        double lat = fp - Q1*(Q2 - Q3 + Q4);
        // 計算經度
        double Q5 = D;
        double Q6 = (1 + 2*T1 + C1)*Math.pow(D, 3)/6;
        double Q7 = (5 - 2*C1 + 28*T1 - 3*Math.pow(C1, 2) + 8*e2 + 24*Math.pow(T1, 2))*Math.pow(D, 5)/120.0;
        double lon = lon0 + (Q5 - Q6 + Q7)/Math.cos(fp);
        lat = (lat * 180) / Math.PI;
        lon = (lon * 180) / Math.PI;
        return new GPSPoint(lon,lat);
    }
	public static String STORAGE_DIR = "dmlab.firemap";
	public static File getTempFile(Context context, String url) {
		File file=null;
		try {
			String fileName = Uri.parse(url).getLastPathSegment();
//			file = File.createTempFile(fileName, ".jpg", context.getCacheDir());
			file = File.createTempFile(fileName, ".jpg", util.getAlbumStorageDir(context, util.STORAGE_DIR));
		} catch (IOException e) {
			// Error while creating file
		}
		return file;
	}
	public static File getAudioTempPath(Context context) {
		File file=null;
		try {
			String fileName = RecMicToMp3.MP3TempFileName;
			int dot = fileName.lastIndexOf('.');
			String base = (dot == -1) ? fileName : fileName.substring(0, dot);
			String extension = (dot == -1) ? "" : fileName.substring(dot+1);
			file = File.createTempFile(base, "."+extension, util.getAudioStorageDir(context));
		} catch (IOException e) {
			// Error while creating file
		}
		return file;
	}
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	public static File getAlbumStorageDir(Context context, String albumName) {
	    // Get the directory for the app's private pictures directory.
		File dir = new File(Environment.getExternalStorageDirectory() + "/albumName");
		if( !dir.isDirectory() ){
			dir.mkdir();
			Log.i(util.LOG_TAG, "Make DIR " + dir.getAbsolutePath());
		}
	    return dir;
	}
	public static File getAudioStorageDir(Context context) {
	    // Get the directory for the app's private pictures directory.
		File dir = new File(Environment.getExternalStorageDirectory() + "/voiceRec");
		if( !dir.isDirectory() ){
			dir.mkdir();
			Log.i(util.LOG_TAG, "Make DIR " + dir.getAbsolutePath());
		}
	    return dir;
	}
	public static Bitmap rotate(Bitmap bitmap, int degree) {
	    int w = bitmap.getWidth();
	    int h = bitmap.getHeight();

	    Matrix mtx = new Matrix();
	   //       mtx.postRotate(degree);
	    mtx.setRotate(degree);

	    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}
	public static String getTimestamp(){
		Long tsLong = System.currentTimeMillis()/1000;
		return tsLong.toString();
	}
	public static String getEventId(){
		return util.getTimestamp();
	}
}
