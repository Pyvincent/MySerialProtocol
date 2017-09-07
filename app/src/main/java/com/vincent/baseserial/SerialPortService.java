package com.vincent.baseserial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.vincent.utils.CircleQueue;
import com.vincent.utils.Utils;

import android_serialport_api.SerialPort;

public abstract class SerialPortService extends Service {
	protected CircleQueue mCircleQueue = new CircleQueue();
	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread = null;
	protected Object mOutStreamLock = new Object();
	protected String mSerialName = null;
	
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size = 0;
				try {
					byte[] buffer = new byte[128];
					if (mInputStream == null) return;
					
					size = mInputStream.read(buffer);
					
					if (size > 0) {
						mCircleQueue.write(buffer, size);
						handleFrame();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	protected abstract void handleFrame();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	protected void openSerialAndRead(String serialName) {
		mSerialName = serialName;
		if(Utils.stringIsEmpty(mSerialName)) {
			return;
		}
		
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort(serialName);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
	
	protected void closeSerialPortAndStopRead() {
		if (mReadThread != null) {
			mReadThread.interrupt();
			mReadThread = null;
		}
		
		if(null != mSerialPort) {
			mApplication.closeSerialPort();
			mSerialPort = null;
		}
	}
	
	public String getMSerialName() {
		return mSerialName;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		closeSerialPortAndStopRead();
	}
}
