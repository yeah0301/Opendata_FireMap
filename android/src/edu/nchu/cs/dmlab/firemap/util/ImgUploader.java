package edu.nchu.cs.dmlab.firemap.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import edu.nchu.cs.dmlab.firemap.MainRecordActivity;
import edu.nchu.cs.dmlab.firemap.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class ImgUploader {
	private final String TAG = "util"; 
	private String path ="";
	public ImgUploader(Activity activity){
		Context context = activity.getApplicationContext();
		if( activity instanceof MainRecordActivity ){
			this.path = context.getString(R.string.image_upload_url);
		}
	}
	public void uploadMultiple(ArrayList<Bitmap> bitmapOrgArr, NameValuePair[] params) {
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		int i=1;
		for( Bitmap bitmapOrg : bitmapOrgArr){
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte[] ba = bao.toByteArray();
			String ba2Str = Base64.encodeToString(ba, Base64.DEFAULT);
			BasicNameValuePair pair = new BasicNameValuePair("image_" + i, ba2Str);
			nameValuePairs.add(pair);
			Log.i(util.LOG_TAG, "Bitmap:" + pair.getName());
			i++;
			bitmapOrg.recycle();
		}
		for( NameValuePair param : params ){
//			Log.i(util.LOG_TAG, param.getName());
			nameValuePairs.add(param);
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(path);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			//String ret = getWebPage(is);
			//Log.e(util.LOG_TAG, "Ret : " + ret);
		} catch (Exception e) {
			Log.e(util.LOG_TAG, "Error in http connection " + e.toString());
		}
		finally{
			try {
				if( is != null )
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			is = null;
		}
	}
	public void upload(Bitmap bitmapOrg, NameValuePair[] params) {
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		String ba2Str = Base64.encodeToString(ba, Base64.DEFAULT);
		
		nameValuePairs.add(new BasicNameValuePair("image", ba2Str));
		for( NameValuePair param : params ){
			nameValuePairs.add(param);
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(path);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
//			String ret = getWebPage(is);
//			Log.e(TAG, "Ret : " + ret);
			
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			is = null;
		}
	}
	
	public String getWebPage(InputStream is){
		try {
		    /* Read the response stream */
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);

		    /* Copy the response to StringBuilder */
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		      sb.append(line + "\n");
		    }
		    is.close();

		    return sb.toString();
		  } catch (Exception e) {
		    Log.e(TAG, "Error converting result " + e.toString());
		    return null;
		  }
	}
}
