package edu.nchu.cs.dmlab.firemap.util;

public class Message {
	/**
	 * record start
	 */
	public static final int MSG_REC_STARTED = 0;

	/**
	 * record stop
	 */
	public static final int MSG_REC_STOPPED = 1;

	/**
	 * not support the buffersize about sampling rate
	 */
	public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = 2;

	/**
	 * error for generate file
	 */
	public static final int MSG_ERROR_CREATE_FILE = 3;

	/**
	 * not start recording
	 */
	public static final int MSG_ERROR_REC_START = 4;
	
	/**
	 * can't record. alarm after start recording 
	 */
	public static final int MSG_ERROR_AUDIO_RECORD = 5;

	/**
	 * can't encode. alarm after start encoding
	 */
	public static final int MSG_ERROR_AUDIO_ENCODE = 6;

	/**
	 * can't write file. alarm after start writting
	 */
	public static final int MSG_ERROR_WRITE_FILE = 7;

	/**
	 * can't close file. alarm after start closing file
	 */
	public static final int MSG_ERROR_CLOSE_FILE = 8;

	public static final int MSG_UPLOAD_AUDIO = 9;
	public static final int MSG_ERROR_UPLOAD_AUDIO = 10;
	
	
	// Main Record Activity
	public static final int MSG_SPANNER_LIST = 101;
	
	// Main FullView Activity
	public static final int MSG_UPLOAD_DIALOG = 801;
	public static final int MSG_UPLOAD_DIALOG_CLOSE = 802;
	public static final int MSG_UPLOAD_EMPTY = 803;
	public static final int MSG_FULL_IMGNUM = 804;
	public static final int MSG_OPEN_ACTIVITY = 805;
	
	// Main Record Dialog
	public static final int MSG_DIALOG_OPEN = 901;
	public static final int MSG_DIALOG_CLOSE = 902;
}
