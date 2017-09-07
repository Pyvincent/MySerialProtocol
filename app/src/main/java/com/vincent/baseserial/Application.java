package com.vincent.baseserial;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android_serialport_api.SerialPort;

public class Application extends android.app.Application {

	private SerialPort mSerialPort = null;

	public SerialPort getSerialPort(String serialName) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			String path = "/dev/" + serialName;
			int baudrate = 9600;

			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
