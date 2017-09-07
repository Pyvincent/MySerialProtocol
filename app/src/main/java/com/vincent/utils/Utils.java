package com.vincent.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

@SuppressLint("NewApi")
public class Utils {
	public static long getSystemTimestamp() {
		return System.currentTimeMillis();
	}
	
	public static long getSystemTimestampForSecond() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static String getInboxSN() {
		return android.os.Build.SERIAL;
	}
	
	public static String getSysVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static boolean checkTextEmpty(String str) {
		if(null == str || "".equals(str.trim())) {
			return true;
		}
		
		return false;
	}
	
	/* such as 4.2.2 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static String getBuildVersion() {
		return android.os.Build.DISPLAY;
	}
	
	public static String getNetworkIP() {
		String ip = "";
		
		try { 
			for (Enumeration<NetworkInterface> en = NetworkInterface  
	                .getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf  
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement(); 
	                if (!inetAddress.isLoopbackAddress()) {  
	                	if(null != inetAddress.getHostAddress() && false == inetAddress.getHostAddress().contains("::"))
	                    return inetAddress.getHostAddress();  
	                }  
	            }  
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	public static String getVersionForPackageName(Context ctx, String packageName) {
		if(null == ctx) {
			return "unknow";
		}
		
		if(Utils.checkTextEmpty(packageName)) {
			return "unknow";
		}
		
		PackageManager manager = null;
		PackageInfo info = null;
		
		manager = ctx.getPackageManager();
		try {
			info = manager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
			info = null;
		}
		
		if(null == info) {
			return "unknow";
		}
		
		return info.versionName;
	}
	
	public static boolean checkAppExist(String packageName, Context ctx) {
		if (checkTextEmpty(packageName)) {
			return false;
		}

		if (null == ctx) {
			return false;
		}

		try {
			ApplicationInfo appInfo = ctx.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			if (null == appInfo) {
				return false;
			}

			return true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkDirExist(String path) {
		if (Utils.checkTextEmpty(path)) {
			return false;
		}

		File file = new File(path);
		if (false == file.exists()) {
			file.mkdir();
			return false;
		}

		return true;
	}
	
	public static boolean checkFileExist(String path) {
		if (Utils.checkTextEmpty(path)) {
			return false;
		}
		
		File file = new File(path);
		if(false == file.exists()) {
			return false;
		}
		
		return true;
	}
	
	public static boolean checkBitExist(int value, int bit) {
		if(0 == (value & bit)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean listIsEmpty(List list) {
		if(null == list || 0 == list.size()) {
			return true;
		}
		
		return false;
	}
	
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    public static boolean stringIsEmpty(String str) {
		if(null == str || "".equals(str.trim())) {
			return true;
		}
		
		return false;
	}
    
	public static byte intToByte(int value) {
		return (byte) (value & 0xff);
	}
	
	public static boolean equalsByteArray(byte[] a, byte[] b, int length) {
		if(a == null && b == null) {
			return true;
		}
		
		if(a.length < length || b.length < length) {
			return false;
		}
		
		for(int i = 0; i < length; i++) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	public static int byteToInt(byte[] data) {
		int length = data.length;
		int rtn = 0;
		if(length > 4) {
			length = 4;
		}
		switch(length) {
		case 1:
			rtn = (data[0] & 0x000000ff);
			break;
		case 2:
			rtn = ((data[0] & 0x000000ff) << 8) | ((data[1] & 0x000000ff)); 
			break;
		case 3:
			rtn = ((data[0] & 0x000000ff) << 16) | ((data[1] & 0x000000ff) << 8)
					| ((data[2] & 0x000000ff));
			break;
		case 4:
			rtn = ((data[0] & 0x000000ff) << 24) | ((data[1] & 0x000000ff) << 16)
			| ((data[2] & 0x000000ff) << 8) | ((data[3] & 0x000000ff));
			break;
		}
		
		return rtn;
	}
	
	public static int byteToInt(byte data) {
		return data & 0x000000ff;
	}
	
	public static String bytesToString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < data.length; i++ ) {
			if((data[i] & 0x000000ff) < 0x10) {
				sb.append("0" + Integer.toHexString(data[i] & 0x000000ff));
			} else {
				sb.append(Integer.toHexString(data[i] & 0x000000ff));
			}
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public static String bytesToString(byte[] data, int len) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < len; i++ ) {
			if((data[i] & 0x000000ff) < 0x10) {
				sb.append("0" + Integer.toHexString(data[i] & 0x000000ff));
			} else {
				sb.append(Integer.toHexString(data[i] & 0x000000ff));
			}
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public static String byteToString(byte data) {
		StringBuffer sb = new StringBuffer();
		
		if((data & 0x000000ff) < 0x10) {
			sb.append("0x0" + Integer.toHexString(data & 0x000000ff));
		} else {
			sb.append("0x" + Integer.toHexString(data & 0x000000ff));
		}
		
		return sb.toString();
	}
	public static boolean byteMemCopy(byte[] dest, byte[] src, int length) {
		if(dest.length < length || src.length < length) {
			return false;
		}
		
		for(int i = 0; i < length; i++) {
			dest[i] = src[i];
		}
		
		return true;
	}
	
	public static boolean byteMemCopy(byte[] dest, byte[] src, int offset, int length) {
		if(src.length < (offset + length) || dest.length < length) {
			return false;
		}
		
		for(int i = 0; i < length; i++) {
			dest[i] = src[i + offset];
		}
		
		return true;
	}
	
	public static boolean byteMemCopy(byte[] dest, byte[] src, int start, int offset, int length) {
		if(src.length < (offset + length) || dest.length < (start + length)) {
			return false;
		}
		
		for(int i = offset, j = start; i < (offset + length); i++, j++) {
			dest[j] = src[i];
		}
		
		return true;
	}
	
	public static int byteGetBit(byte data, int bit) {
		int rtn = 0;
		
		rtn = ((data & 0x000000ff) >> bit) & 0x00000001;
		
		return rtn;
	}
	
	public static byte[] intToBytes(int data, int length) {
		if(length == 0) {
			return null;
		}
		if(length > 4) {
			length = 4;
		}
		
		byte[] rtnData = new byte[length];
		
		if(length == 1) {
			rtnData[0] = (byte) (data & 0x000000ff);
			return rtnData;
		}
		if(length == 2) {
			rtnData[1] = (byte) (data & 0x000000ff);
			rtnData[0] = (byte) ((data >> 8) & 0x000000ff);
			return rtnData;
		}
		if(length == 3) {
			rtnData[2] = (byte) (data & 0x000000ff);
			rtnData[1] = (byte) ((data >> 8) & 0x000000ff);
			rtnData[0] = (byte) ((data >> 16) & 0x000000ff);
			return rtnData;
		}
		if(length == 4) {
			rtnData[3] = (byte) (data & 0x000000ff); 
			rtnData[2] = (byte) ((data >> 8) & 0x000000ff); 
			rtnData[1] = (byte) ((data >> 16) & 0x000000ff); 
			rtnData[0] = (byte) ((data >> 24) & 0x000000ff); 
			return rtnData;
		}
		
		return rtnData;
	}
	
	public static byte[] hexStringToByte(String hex) {   
	    int len = (hex.length() / 2);   
	    byte[] result = new byte[len];   
	    char[] achar = hex.toCharArray();   
	    for (int i = 0; i < len; i++) {   
	     int pos = i * 2;   
	     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));   
	    }   
	    return result;   
	}
	  
	private static byte toByte(char c) {   
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);   
	    return b;   
	}
	
	public static long byte4DataconverToLong(byte[] param) {
		long result = 0;
		
		if(param == null || param.length == 0) {
			return result;
		}
		
		for(int i = 0; i < param.length; i++) {
			result |= (param[i] << (i * 8)) & (0xFF << (i * 8));
		}
		
		return result & 0xFFFFFFFFL;
	}
	
	public static long byteToLong(byte[] data) {
		int length = data.length;
		long rtn = 0L;
		if(length > 4) {
			length = 4;
		}
		switch(length) {
		case 1:
			rtn = (data[0] & 0x000000ffL);
			break;
		case 2:
			rtn = ((data[0] & 0x000000ffL) << 8) | ((data[1] & 0x000000ffL)); 
			break;
		case 3:
			rtn = ((data[0] & 0x000000ffL) << 16) | ((data[1] & 0x000000ffL) << 8)
					| ((data[2] & 0x000000ffL));
			break;
		case 4:
			rtn = ((data[0] & 0x000000ffL) << 24) | ((data[1] & 0x000000ffL) << 16)
			| ((data[2] & 0x000000ffL) << 8) | ((data[3] & 0x000000ffL));
			break;
		}
		
		return rtn;
	}
}
