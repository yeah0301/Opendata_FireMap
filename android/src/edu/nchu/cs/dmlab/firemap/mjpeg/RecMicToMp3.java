/* 
 * Copyright (c) 2011-2012 Yuichi Hirano
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package edu.nchu.cs.dmlab.firemap.mjpeg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.uraroji.garage.android.lame.SimpleLame;

import edu.nchu.cs.dmlab.firemap.MainRecordActivity;
import edu.nchu.cs.dmlab.firemap.util.AudioUploader;
import edu.nchu.cs.dmlab.firemap.util.Message;

/**
 * Save the audio from Mic to save the MP3 File
 * 
 * */
public class RecMicToMp3 {

	static {
		System.loadLibrary("mp3lame");
	}

	/**
	 * the path of mp3 file
	 */
//	private final String mFilePath;
	private final File mFile;

	private final int mSampleRate;

	/**
	 * recording status 
	 */
	private boolean mIsRecording = false;

	/**
	 * 處理通知中記錄的狀態變化
	 * 
	 * @see RecMicToMp3#MSG_REC_STARTED
	 * @see RecMicToMp3#MSG_REC_STOPPED
	 * @see RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
	 * @see RecMicToMp3#MSG_ERROR_CREATE_FILE
	 * @see RecMicToMp3#MSG_ERROR_REC_START
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_RECORD
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
	 * @see RecMicToMp3#MSG_ERROR_WRITE_FILE
	 * @see RecMicToMp3#MSG_ERROR_CLOSE_FILE
	 */
	private Handler mHandler;

	private final MainRecordActivity activity;
	public static String MP3TempFileName = "wra_temp.mp3";
	/**
	 * 
	 * @param filePath
	 *            save mp3 file
	 * @param sampleRate
	 */
	public RecMicToMp3(String filePath, int sampleRate, MainRecordActivity activity) {
		if (sampleRate <= 0) {
			throw new InvalidParameterException("Invalid sample rate specified.");
		}
		this.mFile = new File(filePath);
		this.mSampleRate = sampleRate;
		this.activity = activity;
	}

	/**
	 * Start recording
	 */
	public void start() {
		// just skip if recording has happened
		if (mIsRecording) {
			return;
		}

		new Thread() {
			@Override
			public void run() {
				if( mHandler != null )
					mHandler.sendEmptyMessage(Message.MSG_DIALOG_OPEN);
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				// caculate minimax buffersize
				final int minBufferSize = AudioRecord.getMinBufferSize( mSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
				if (minBufferSize < 0) {
					if (mHandler != null) {
						mHandler.sendEmptyMessage(Message.MSG_ERROR_GET_MIN_BUFFERSIZE);
					}
					return;
				}
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, mSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 2);
				// PCM buffer size (5sec)
				short[] buffer = new short[mSampleRate * (16 / 8) * 1 * 5]; // SampleRate[Hz] * 16bit * Mono * 5sec
				byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

				FileOutputStream output = null;
				try {
					output = new FileOutputStream(mFile);
				} catch (FileNotFoundException e) {
					if (mHandler != null) {
						mHandler.sendEmptyMessage(Message.MSG_ERROR_CREATE_FILE);
					}
					return;
				}

				// Lame init
				SimpleLame.init(mSampleRate, 1, mSampleRate, 32);
				
				try {
					try {
						audioRecord.startRecording();
						mIsRecording = true;
					} catch (IllegalStateException e) {
						if (mHandler != null) {
							mHandler.sendEmptyMessage(Message.MSG_ERROR_REC_START);
						}
						return;
					}

					try {
						if (mHandler != null) {
							mHandler.sendEmptyMessage(Message.MSG_REC_STARTED);
						}

						int readSize = 0;
						while (mIsRecording) {
							readSize = audioRecord.read(buffer, 0, minBufferSize);
							if (readSize < 0) {
								if (mHandler != null) {
									mHandler.sendEmptyMessage(Message.MSG_ERROR_AUDIO_RECORD);
								}
								break;
							// no data
							}else if (readSize == 0) {
								;
							// fetch data
							}else {
								int encResult = SimpleLame.encode(buffer, buffer, readSize, mp3buffer);
								if (encResult < 0) {
									if (mHandler != null) {
										mHandler.sendEmptyMessage(Message.MSG_ERROR_AUDIO_ENCODE);
									}
									break;
								}
								if (encResult != 0) {
									try {
										output.write(mp3buffer, 0, encResult);
									} catch (IOException e) {
										if (mHandler != null) {
											mHandler.sendEmptyMessage(Message.MSG_ERROR_WRITE_FILE);
										}
										break;
									}
								}
							}
						}
						int flushResult = SimpleLame.flush(mp3buffer);
						if (flushResult < 0) {
							if (mHandler != null) {
								mHandler.sendEmptyMessage(Message.MSG_ERROR_AUDIO_ENCODE);
							}
						}
						if (flushResult != 0) {
							try {
								output.write(mp3buffer, 0, flushResult);
							} catch (IOException e) {
								if (mHandler != null) {
									mHandler.sendEmptyMessage(Message.MSG_ERROR_WRITE_FILE);
								}
							}
						}

						try {
							output.close();
						} catch (IOException e) {
							if (mHandler != null) {
								mHandler.sendEmptyMessage(Message.MSG_ERROR_CLOSE_FILE);
							}
						}
					} finally {
						audioRecord.stop();
						audioRecord.release();
					}
				} finally {
					SimpleLame.close();
					mIsRecording = false;
				}

				if (mHandler != null) {
					mHandler.sendEmptyMessage(Message.MSG_REC_STOPPED);
				}
				
				// Upload Audio
				uploadVoice();
				if( mHandler != null )
					mHandler.sendEmptyMessage(Message.MSG_DIALOG_CLOSE);
			}
		}.start();
	}

	public void stop() {
		mIsRecording = false;
	}

	public boolean isRecording() {
		return mIsRecording;
	}

	public void setHandle(Handler handler) {
		this.mHandler = handler;
	}
	/**
	 * upload task
	 */
//	class TakeAsyncRecordAudio extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... params) {
//			Log.i("RecMic", "TakeAsyncVoiceRecord");
//			if ( !mIsRecording ) {
//				uploadVoice();
//			}
//			return null;
//		}
//		@Override
//		protected void onPostExecute(Void result) {
//		}
//	}

	// upload job
	private void uploadVoice() {

		// TODO Auto-generated method stub

		// upload Image to web
		AudioUploader uploader = new AudioUploader(activity);
		NameValuePair[] params = new NameValuePair[5];
		// gps info : x,y
		Location loc = activity.getLocation();
		if (loc != null) {
			params[0] = new BasicNameValuePair("x", String.valueOf(loc.getLongitude()));
			params[1] = new BasicNameValuePair("y", String.valueOf(loc.getLatitude()));
		} else {
			params[0] = new BasicNameValuePair("x", "0");
			params[1] = new BasicNameValuePair("y", "0");
		}

		// upload man
		params[2] = new BasicNameValuePair("worker", activity.getWorker());

		// upload eventid , eventdesc
		params[3] = new BasicNameValuePair("eventid", activity.getEventID());
//		params[4] = new BasicNameValuePair("desc", activity.getDesc());

		// MP3 file path
		Log.i("MP3", "upload audio");
		String reps = uploader.upload(mFile, params);
//		Log.i("MP3", "Response: " + reps);
	}
}
