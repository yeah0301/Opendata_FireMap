package edu.nchu.cs.dmlab.firemap.view;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.nchu.cs.dmlab.firemap.MainRecordActivity;
import edu.nchu.cs.dmlab.firemap.util.ImgUploader;
import edu.nchu.cs.dmlab.firemap.util.util;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback, AutoFocusCallback  {
	
	private int PreviewSizeWidth=0, PreviewSizeHeight=0;
	private boolean takeScreenshot = false;
	private boolean screenshotTaking = false;
	
	private AutoFocusCallback myAutoFocusCallback = null;
	private Camera mCamera = null;

//	private PictureCallback jpegPictureCallback = null;
	
	private TakeAsyncScreenshot myTask;
	private MainRecordActivity activity = null;
	
	private boolean afSupported = false;
	private boolean bIfPreview = false;
	public CameraPreview(MainRecordActivity activity, int PreviewlayoutWidth, int PreviewlayoutHeight) {
		this.activity = activity;
		PreviewSizeWidth = PreviewlayoutWidth;
		PreviewSizeHeight = PreviewlayoutHeight;

//		jpegPictureCallback = new PictureCallback() {
		new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera arg1) {
				// Save the picture.
				try {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
					FileOutputStream out = new FileOutputStream("test.jpg");
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		afSupported = activity.getPackageManager().hasSystemFeature("android.hardware.camera.autofocus");
		if( afSupported ){
			myAutoFocusCallback = new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean arg0, Camera NowCamera) {
				}
			};
		}
	}

	public Camera getCamera(){
		return mCamera;
	}
	public boolean isCameraInUse() {
	    Camera c = null;
	    try {
	        c = Camera.open();
	    } catch (RuntimeException e) {
	        return true;
	    } finally {
	        if (c != null) c.release();
	    }
	    return false;
	}
	public void stopPreviewAndFreeCamera() {
		Log.i(util.LOG_TAG, "Camer Used " + isCameraInUse());
	    if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			activity.getCamview().getHolder().removeCallback(this);
			mCamera.stopPreview();
			bIfPreview = false; 
			mCamera.release();
			mCamera = null;
	    }
	}

	private Bitmap bitmap;
	@Override
	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		// At preview mode, the frame data will push to here.
		// But we do not want these data.
		
//		Log.e(util.LOG_TAG, "PreviewFrame");
		if( takeScreenshot ){
			Camera.Parameters parameters = mCamera.getParameters();
			int format = parameters.getPreviewFormat();
			if (format == ImageFormat.NV21 || format == ImageFormat.YUY2 || format == ImageFormat.NV16){
				try{
//					int rotate = 0;
//					switch (activity.getOrientation()) {
//					case MainRecordActivity.ORIENTATION_PORTRAIT_NORMAL:
//						rotate = 90;
//						break;
//					case MainRecordActivity.ORIENTATION_LANDSCAPE_NORMAL:
//						rotate = 0;
//						break;
//					case MainRecordActivity.ORIENTATION_PORTRAIT_INVERTED:
//						rotate = 270;
//						break;
//					case MainRecordActivity.ORIENTATION_LANDSCAPE_INVERTED:
//						rotate = 180;
//						break;
//					}
//					Log.i(util.LOG_TAG, "Rotation : " + rotate);
		            Size size = parameters.getPreviewSize();
		            YuvImage image = new YuvImage(arg0, ImageFormat.NV21,size.width, size.height, null);
		            
		            Rect rectangle = new Rect();
		            rectangle.bottom = size.height;
		            rectangle.top = 0;
		            rectangle.left = 0;
		            rectangle.right = size.width;
		            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		            image.compressToJpeg(rectangle, 100, out2);
					// Convert from Jpeg to Bitmap
					bitmap = BitmapFactory.decodeByteArray(out2.toByteArray(), 0, out2.size());
					
					// rotation
					util.rotate(bitmap, 90);
		            
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (format == ImageFormat.JPEG || format == ImageFormat.RGB_565){
				bitmap = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
//				Log.i("Preview","Format : JPEG or RGB");
			}
			
			if ( !screenshotTaking ){
				if( bitmap == null ){
					Log.i("Preview", "bitmap is null");
					takeScreenshot = false;
					screenshotTaking = false;
				}else{
					screenshotTaking = true;
					Log.i("Preview", "bitmap save");
					myTask = new TakeAsyncScreenshot();
					myTask.execute();
				}
			}
		}
	}

	// upload job
	private void uploadImage(Bitmap normalBitmap) {

		// TODO Auto-generated method stub

		// upload Image to web
		ImgUploader uploader = new ImgUploader(activity);
		NameValuePair[] params = new NameValuePair[5];
		// gps info : x,y
		Location loc = activity.getLocation();
		if( loc != null ){
			params[0] = new BasicNameValuePair("x", String.valueOf(loc.getLongitude()));
			params[1] = new BasicNameValuePair("y", String.valueOf(loc.getLatitude()));	
		}else{
			params[0] = new BasicNameValuePair("x", "0");
			params[1] = new BasicNameValuePair("y", "0");
		}
		
		// upload man
		params[2] = new BasicNameValuePair("worker", activity.getWorker());

		// upload eventid , eventdesc
		params[3] = new BasicNameValuePair("eventid", activity.getEventID());
		params[4] = new BasicNameValuePair("desc", activity.getDesc());
		
		// bitmap.compress(format, quality, stream)
		uploader.upload(normalBitmap, params);
//		distoryBitmap(normalBitmap);
		Log.i("Preview", "upload");
	}
	
//	public void distoryBitmap(Bitmap bm){     
//	    if(null != bm && !bm.isRecycled())     
//	        bm.recycle();     
//	}
	
	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {
		private Bitmap bitmap_tmp;
		@Override
		protected Void doInBackground(Void... params) {
			bitmap_tmp = bitmap;
			Log.i("Preview", "TakeAsyncScreenshot");
			while (takeScreenshot) {
				if( !bitmap_tmp.equals(bitmap)){
					bitmap_tmp = bitmap;
					uploadImage(bitmap);
				}
			}
			Log.i("Preview", "while loop stop");
			screenshotTaking = false;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.i(util.LOG_TAG, " surfaceChanged R");
		initCamera();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i(util.LOG_TAG, " surfaceCreated R");
		try{
			stopPreviewAndFreeCamera();
			mCamera = Camera.open();
	//		mCamera.setDisplayOrientation(90);
			try {
				// If did not set the SurfaceHolder, the preview area will be black.
				mCamera.setPreviewDisplay(activity.getCamview().getHolder());
				mCamera.setPreviewCallback(this);
			} catch (IOException e) {
				if (null != mCamera) {
					mCamera.release();
					mCamera = null;
				}
			}
			
			// rotation
//	        String currentversion = android.os.Build.VERSION.SDK;
	        int currentInt = android.os.Build.VERSION.SDK_INT;

	        Parameters parameters = mCamera.getParameters();
	        if (activity.getCamview().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	            if (currentInt != 7) {
	                mCamera.setDisplayOrientation(90);
	            } else {
	                Log.d(util.LOG_TAG, "Portrait " + currentInt);

	                parameters.setRotation(90);

	                /*
	                 * params.set("orientation", "portrait");
	                 * params.set("rotation",90);
	                 */
	                mCamera.setParameters(parameters);
	            }
	        }
	        if (activity.getCamview().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	            // camera.setDisplayOrientation(0);
	            if (currentInt != 7) {
	                mCamera.setDisplayOrientation(0);
	            } else {
	                Log.d(util.LOG_TAG, "Landscape " + currentInt);
	                parameters.set("orientation", "landscape");
	                parameters.set("rotation", 90);
	                mCamera.setParameters(parameters);
	            }
	        }
//	        mCamera.setPreviewDisplay(arg0);
	        mCamera.setPreviewDisplay(activity.getCamview().getHolder());
			
//			setCameraDisplayOrientation(activity, CameraInfo.CAMERA_FACING_BACK, mCamera);
		}catch( RuntimeException e ){
			mCamera = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initCamera(){
		if (bIfPreview){
			mCamera.stopPreview();//stopCamera();
		}
		
		if(null != mCamera){
			try {
				Parameters parameters = mCamera.getParameters();

				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
				
				// Set the camera preview size				
				Camera.Size size = getBestViewResolution(parameters, PreviewSizeWidth, PreviewSizeHeight);
				Camera.Size psize = getBestPictureViewResolution(parameters, PreviewSizeWidth, PreviewSizeHeight);

				parameters.setPreviewSize(size.width, size.height);
				parameters.setPictureSize(psize.width, psize.height);
				
				// parameters.setPreviewFormat(ImageFormat.JPEG);
				// parameters.setPictureFormat(PixelFormat.JPEG);
				// Turn on the camera flash.
				// String NowFlashMode = parameters.getFlashMode();
				// if (NowFlashMode != null)
				// parameters.setFlashMode(Parameters.FLASH_MODE_ON);
				// Set the auto-focus.
				if (afSupported) {
					String NowFocusMode = parameters.getFocusMode();
					if (NowFocusMode != null)
						parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				}
				
//				parameters.set("orientation", "portrait"); //
//				parameters.set("rotation", 270); // rotation 0 degree
//				mCamera.setDisplayOrientation(90); // v2.2 above
				
				mCamera.setParameters(parameters);
				mCamera.startPreview();
				bIfPreview = true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i(util.LOG_TAG, "surfaceDestroyed R");
//		if (null != mCamera) {
			stopPreviewAndFreeCamera();
//			mCamera.setPreviewCallback(null);
//			mCamera.stopPreview();
//			bIfPreview = false; 
//			mCamera.release();
//			mCamera = null;
//		}
	}


	// Take picture interface
	public void CameraTakePicture(String FileName) {
		if( afSupported ){
			mCamera.autoFocus(myAutoFocusCallback);
			bIsAutoFcoused = true;
		}
	}

	public void record(){
//		screenshotTaken = false;
		takeScreenshot = true;
	}
	public void cancelRecord(){
		takeScreenshot = false;
	}
	private Camera.Size getBestPictureViewResolution(Camera.Parameters params, int pixel_width, int pixel_height){
		/* the size that camera allow */
		List<Camera.Size> sizeList = params.getSupportedPictureSizes();
		Comparator<Camera.Size> cmp = new myComparator<Camera.Size>();
		Collections.sort(sizeList,cmp);
		Camera.Size size = sizeList.get(0);
		Size bestSize = null;
//		Log.i("CameraPicture", "Picture Size : " + size.width +","+size.height);
		for( int i=0; i < sizeList.size() ; i++ ){
			size = sizeList.get(i);
			if( size.width > size.height ){
				if( size.width >= pixel_width ){
					bestSize = size;
					Log.i("wra", "Best picture Size : " + size.width +","+size.height);
					break;
				}
			}
		}
//		Log.i("size", "choose V : " + size.width+"x"+size.height);
		return bestSize;
	}
	private Camera.Size getBestViewResolution(Camera.Parameters params, int pixel_width, int pixel_height){
		/* the size that camera allow */
		List<Camera.Size> sizeList = params.getSupportedPreviewSizes();
		Comparator<Camera.Size> cmp = new myComparator<Camera.Size>();
		Collections.sort(sizeList,cmp);
		Camera.Size size = sizeList.get(0);
		Size bestSize = null;
		for( int i=0; i < sizeList.size() ; i++ ){
			size = sizeList.get(i);
//			Log.i("wra", "View Size : " + size.width +","+size.height);
			if( size.width > size.height ){
				if( size.width >= pixel_width ){
					bestSize = size;
					Log.i("CameraPreview", "Best preview Size : " + size.width +","+size.height);
//					break;
				}
			}
		}
//		Log.i("size", "choose V : " + size.width+"x"+size.height);
		return bestSize;
	}
	class myComparator<T> implements Comparator<Camera.Size>{
		@Override
		public int compare(Camera.Size size1, Camera.Size size2) {
			// TODO Auto-generated method stub
			if ( size1.width < size2.width ){
				return -1;
			}else if( size1.width > size2.width ){
				return 1;
			}else {
				// equal
				if( size1.height < size2.height ){
					return -1;
				}else if( size1.height > size2.height ){
					return 1;
				}else{
					return 0;
				}
			}
		}
	}
	
	public void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     // SDK v.10
	     if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		
	}
	
	
	/* ****************************
	 * Focus function
	 * ****************************/
	private boolean bIsAutoFcoused = false;
	// Set auto-focus interface
	public void CameraStartAutoFocus() {
		if( afSupported ){
			mCamera.autoFocus(myAutoFocusCallback);
			bIsAutoFcoused = true;
		}
	}
	public boolean isSupportFocus(){
		return afSupported;
	}
}