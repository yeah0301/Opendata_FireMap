package edu.nchu.cs.dmlab.firemap;

import edu.nchu.cs.dmlab.firemap.mjpeg.RecMicToMp3;
import edu.nchu.cs.dmlab.firemap.util.Message;
import edu.nchu.cs.dmlab.firemap.util.util;
import edu.nchu.cs.dmlab.firemap.view.CameraPreview;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainRecordActivity extends Activity{
	// Camera SurfaceView
	private CameraPreview camPreview;
	// 8KHz Micphone
	private RecMicToMp3 mRecMicToMp3;
	public ToggleButton btn_record;
	public ToggleButton btn_voice_record;
	private TextView txt_gps;
	boolean hasGPS = false;
	
	private boolean isRecord = false;
	private boolean isVoiceRecord = false;
//	private TakeAsyncScreenshot myTask;
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // in Milliseconds
    
    protected LocationManager locationManager;
    private MyLocationListener locListener;
    private Location location;
    private boolean isLocated = false;
    private String bestProvider;

    private String workerID = "";
    
    private AlertDialog alert;
	private ProgressDialog recordDialog;
    
    private SurfaceView camView;
    private String eventID, eventDesc;
    
    private MainRecordActivity recordMain;

    public static final int ORIENTATION_PORTRAIT_NORMAL =  1;
    public static final int ORIENTATION_PORTRAIT_INVERTED =  2;
    public static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
    public static final int ORIENTATION_LANDSCAPE_INVERTED =  4;
    private OrientationEventListener mOrientationEventListener;
    private final int mOrientation =  -1;
    
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Message.MSG_REC_STARTED:
				Toast.makeText(MainRecordActivity.this, "録音中", Toast.LENGTH_SHORT).show();
				Log.i("MP3", "録音中");
				break;
			case Message.MSG_REC_STOPPED:
				Toast.makeText(MainRecordActivity.this, "録音中止", Toast.LENGTH_SHORT).show();
				Log.i("MP3", "録音中止");
				break;
			case Message.MSG_ERROR_GET_MIN_BUFFERSIZE:
				Toast.makeText(MainRecordActivity.this, "錄音已開始，Min Buffersize過小。", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音已開始，Min Buffersize過小");
				break;
			case Message.MSG_ERROR_CREATE_FILE:
				Toast.makeText(MainRecordActivity.this, "產生暫存檔失敗", Toast.LENGTH_LONG).show();
				Log.i("MP3", "產生暫存檔失敗");
				break;
			case Message.MSG_ERROR_REC_START:
				Toast.makeText(MainRecordActivity.this, "開始錄音所發生錯誤!", Toast.LENGTH_LONG).show();
				Log.i("MP3", "開始錄音所發生錯誤!");
				break;
			case Message.MSG_ERROR_AUDIO_RECORD:
				Toast.makeText(MainRecordActivity.this, "錄音檔無法寫入", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音檔無法寫入");
				break;
			case Message.MSG_ERROR_AUDIO_ENCODE:
				Toast.makeText(MainRecordActivity.this, "錄音檔無法編碼", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音檔無法編碼");
				break;
			case Message.MSG_ERROR_WRITE_FILE:
				Toast.makeText(MainRecordActivity.this, "錄音檔寫入時發生錯誤", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音檔寫入時發生錯誤");
				break;
			case Message.MSG_ERROR_CLOSE_FILE:
				Toast.makeText(MainRecordActivity.this, "錄音檔結束時發生錯誤", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音檔結束時發生錯誤");
				break;
			case Message.MSG_UPLOAD_AUDIO:
				Toast.makeText(MainRecordActivity.this, "錄音檔上傳中...", Toast.LENGTH_LONG).show();
				Log.i("MP3", "錄音檔上傳中...");
				break;
			case Message.MSG_DIALOG_OPEN:

				recordDialog.show();
				break;
			case Message.MSG_DIALOG_CLOSE:
				recordDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		recordMain = this;
		
		// Set this SPK Full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.subitem_record);
		
		util.getSIMNumber(this);
		util.getDeviceID(this);
		// set workerID
		workerID = util.getMobileID();

		// set Event ID
		eventID = util.getEventId();
		
		// GPS Textview
		txt_gps = (TextView) findViewById(R.id.record_gps_text);
		
		camView = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder camHolder = camView.getHolder();
		camPreview = new CameraPreview(this,240, 180);

		camHolder.addCallback(camPreview);
		camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// GPS
		if( getGPSService() ){
			locListener = new MyLocationListener();
			locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATES,MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locListener);
			if( location != null ){
				showCurrentLocation();
			}
			hasGPS = true;
		}else{
			hasGPS= false;
			//Ask the user to enable GPS
		    AlertDialog.Builder gpsBuilder = new AlertDialog.Builder(this);
		    gpsBuilder.setTitle("Location Manager");
		    gpsBuilder.setMessage("Would you like to enable GPS?");
		    gpsBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //Launch settings, allowing user to make a change
		            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(i);
		        }
		    });
		    gpsBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //No location service, no Activity
		            finish();
		        }
		    });
		    gpsBuilder.create().show();
		}
		
		// [UI:Button] video button
		btn_record = (ToggleButton) findViewById(R.id.btn_record);
		btn_record.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if( isChecked ){
					if( !isRecord ){
						// Dialog
						//alert.show();
//						myTask = new TakeAsyncScreenshot();
//						myTask.execute();
						runRecord(true);
				 	}
				}else{
					isRecord = false;
					runRecord(false);
				}
			} 

		});
		
		// MP3 Record Ready
		mRecMicToMp3 = new RecMicToMp3(getMP3TempPath(), 8000, this);
		mRecMicToMp3.setHandle(handler);
		// [UI:Button] voice button
		btn_voice_record = (ToggleButton) findViewById(R.id.btn_voice_record);
		btn_voice_record.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if( isChecked ){
					if( !isVoiceRecord ){
						isVoiceRecord = true;
						mRecMicToMp3.start();
						Log.i("MP3", "start recording...");
					}
				}else{
					Log.i("MP3", "stop recording...");
					mRecMicToMp3.stop();
					if( isVoiceRecord ){
						// upload mp3 to server
					}
					isVoiceRecord = false;
				}
			}
			
		});
		
		// MP3 dialog
		recordDialog = new ProgressDialog(recordMain);
		recordDialog.setIcon(android.R.drawable.ic_btn_speak_now);
		recordDialog.setTitle("事件錄音");
		recordDialog.setMessage("正在錄音中...");
		recordDialog.setCanceledOnTouchOutside(false);
		recordDialog.setButton( "結束錄音", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if( btn_voice_record.isChecked() ){
					btn_voice_record.performClick();
				}
				recordDialog.dismiss();
			}
		});
		
	}
	
	public String getMP3TempPath(){
		return util.getAudioStorageDir(this) + "/" + RecMicToMp3.MP3TempFileName;
	}
	private void locationServiceInitial() {
		Criteria criteria = new Criteria(); // 資訊提供者選取標準
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        
		bestProvider = locationManager.getBestProvider(criteria, true);
	}
	private boolean getGPSService(){
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // 取得系統定位服務
		// 1. 選擇最佳提供器
		locationServiceInitial();
		
		if ( bestProvider != null) 
			// 1. 最佳提供器
			Log.i(util.LOG_TAG,"Location: Criteria");
		else{
			// 2. 選擇使用GPS提供器
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				bestProvider = LocationManager.GPS_PROVIDER;
				Log.i(util.LOG_TAG,"Location: GPS Provider");
			}else if( locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ){
				bestProvider = LocationManager.NETWORK_PROVIDER;
				Log.i(util.LOG_TAG, "Location: NETWORK Provider");
			}
		}
		
		if( bestProvider != null ){
			location = locationManager.getLastKnownLocation(bestProvider);
			if (location != null) {
				// nothing
			} else {
				Toast.makeText(this, "GPS目前無法搜尋到位置", Toast.LENGTH_SHORT).show();
			}			
			return true;
		}else {
			Button button = new Button(MainRecordActivity.this);
			button.setText(getString(R.string.word_ok));
			final Dialog dialog = new Dialog(MainRecordActivity.this);
			dialog.setTitle(getString(R.string.toast_gps_error));
			dialog.setContentView(button);
			dialog.show();
			button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					dialog.dismiss();
				}
			});
			return false;
		}
	}
	
	public void runRecord(boolean flag){
		if( flag ){
			isRecord = true;
			camPreview.record();
			//btn_voice_record.setEnabled(true);
		}else{
			isRecord = false;
			camPreview.cancelRecord();
			//btn_voice_record.setEnabled(false);
		}
	}
	
	@Override
	protected void onPause() {
//		Log.i(util.LOG_TAG, " onPause R");
		super.onPause();
		camPreview.stopPreviewAndFreeCamera();
		if( hasGPS )
			locationManager.removeUpdates(locListener);
		mRecMicToMp3.stop();
		Log.i("SensorMap", "Close GPS");
	}

	@Override
	protected void onResume() {
//		Log.i(util.LOG_TAG, "Records onResume");
		super.onResume();
		if( hasGPS ){
			locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locListener);
		}
		
		// Orientation
//		mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
//			@Override
//			public void onOrientationChanged(int orientation) {
//				// determine our orientation based on sensor response
////				int lastOrientation = mOrientation;
//				if (orientation == ORIENTATION_UNKNOWN) return;
//				//Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//				int rotation = getWindowManager().getDefaultDisplay().getRotation();
//				switch (rotation) {
//				case Surface.ROTATION_0:
//					mOrientation = 0;
//					break;
//				case Surface.ROTATION_90:
//					mOrientation = 90;
//					break;
//				case Surface.ROTATION_180:
//					mOrientation = 180;
//					break;
//				case Surface.ROTATION_270:
//					mOrientation = 270;
//					break;
//				}
//				Parameters p = camPreview.getCamera().getParameters();
//				p.setRotation(mOrientation);
//				camPreview.getCamera().setParameters(p);
//			}
//		};
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN ) {
			camPreview.CameraStartAutoFocus();
		}
		return true;
	};
	private void showCurrentLocation(){
		txt_gps.setText("GPS : " + location.getLatitude() + " , " + location.getLongitude());
	}
	private void setLocation(Location location){
		if( !isLocated ){
			txt_gps.setTextColor(Color.RED);
			isLocated = true;
		}
		this.location = location;
		showCurrentLocation();
	}
	public Location getLocation(){
		return location;
	}
	public String getWorker(){
		return this.workerID;
	}

	public void setEventID(String id){
		this.eventID = id;
	}
	public String getEventID(){
		return this.eventID;
	}

	public String getDesc(){
		return eventDesc;
	}

	class MyLocationListener implements LocationListener {
        @Override
		public void onLocationChanged(Location location) {
        	setLocation(location);
        }

        @Override
		public void onStatusChanged(String s, int i, Bundle b) {
        }

        @Override
		public void onProviderDisabled(String s) {
        }

        @Override
		public void onProviderEnabled(String s) {
        }

    }
	public SurfaceView getCamview(){
		return this.camView;
	}
	public int getOrientation(){
		return mOrientation;
	}
}