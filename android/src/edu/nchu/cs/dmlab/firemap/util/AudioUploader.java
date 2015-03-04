package edu.nchu.cs.dmlab.firemap.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;

import edu.nchu.cs.dmlab.firemap.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class AudioUploader {
	private final String TAG = "util";
	private final String path;
	private final Context context;

	public AudioUploader(Activity activity) {
		this.context = activity.getApplicationContext();
		this.path = context.getString(R.string.audio_upload_url);
	}


	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1 * 1024 * 1024;
	String HTTP_mp3UploadName = "wra_mp3";

	public String upload(File mFile, NameValuePair[] params) {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		FileInputStream fileInputStream = null;

		byte[] buffer;
		int maxBufferSize = 20 * 1024;
		try {
			// ------------------ CLIENT REQUEST
			Log.i("MP3", "get file " + mFile.getAbsolutePath());
			fileInputStream = new FileInputStream(mFile);

			// open a URL connection to the Servlet
			// Open a HTTP connection to the URL
			URL url = new URL(this.path);
			Log.i("MP3", path);
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + mFile.getName() + "\"" + lineEnd);
			dos.writeBytes("Content-Type: text/xml" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			buffer = new byte[Math.min((int) mFile.length(), maxBufferSize)];
			int length;
			// read file and write it into form...
			while ((length = fileInputStream.read(buffer)) != -1) {
				dos.write(buffer, 0, length);
			}

			//for (String name : parameters.keySet()) {
			for (NameValuePair param : params) {
				if( param == null ) continue;
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(param.getValue());
			}

			// send multipart form data necessary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.flush();
		} catch( IOException ex){
			Log.i("MP3", ex.getMessage());
		} finally {
			if (fileInputStream != null)
				try {
					fileInputStream.close();
					if (dos != null)
						dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		// ------------------ read the SERVER RESPONSE
		StringBuilder response = new StringBuilder();
		try {
			dis = new DataInputStream(conn.getInputStream());
			String line;
			while ((line = dis.readLine()) != null) {
				response.append(line).append('\n');
			}
		} catch( IOException ex){
			Log.i("MP3", ex.getMessage());
		}finally {
			if (dis != null){
				try {
					dis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.i("MP3", e.getMessage());
				}
			}
		}
		Log.i("MP3", "response: " + response.toString());
		return response.toString();
	}

	public String getWebPage(InputStream is) {
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
