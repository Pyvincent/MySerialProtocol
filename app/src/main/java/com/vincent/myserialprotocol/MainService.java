package com.vincent.myserialprotocol;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.vincent.baseserial.SerialPortService;
import com.vincent.utils.Utils;

public class MainService extends SerialPortService {
    private static final String TAG = MainService.class.getSimpleName();
    private boolean mSerialIsOpened = false;
    private String readData = null;

    public MainService() {
    }

    @Override
    protected void handleFrame() {
        while (true) {
            byte[] readBuffer = new byte[256];
            boolean rtn = false;
            rtn = mCircleQueue.prefetch(30, readBuffer);
            if (!rtn) {
                break;
            }
            if (0x77 == Utils.byteToInt(readBuffer[0]) &&
                    0x77 == Utils.byteToInt(readBuffer[1]) &&
                    0x88 == Utils.byteToInt(readBuffer[28]) &&
                    0x88 == Utils.byteToInt(readBuffer[29])) {

                if (!mCircleQueue.read(30, readBuffer)) {
                    break;
                }
                readData = Utils.bytesToString(readBuffer, 30);
                Intent tomain = new Intent();
                tomain.putExtra("readdata", readData);
                tomain.setAction("com.vincent.serial.data");
                sendBroadcast(tomain);

//                switch (Utils.byteToInt(readBuffer[2])) {
//                    case 0x01:
//                        Log.d("open:", readData);
//                        break;
//                    case 0x02:
//                        Log.d("close:", readData);
//                        break;
//                    case 0x03:
//                        Log.d("hold:", readData);
//                        break;
//                    default:
//                        Log.d("other:", readData);
//                        break;
//                }
            } else {
                mCircleQueue.read(1, readBuffer);
                Log.i(TAG, "skip: " + Utils.bytesToString(readBuffer, 1));
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        return new Binder();
        throw new UnsupportedOperationException("Not yet implemented");
    }
//
//    class Binder extends android.os.Binder {
//        public void setReadData(String data) {
//            MainService.this.readData = data;
//        }
//
//        public String getReadData() {
//            return MainService.this.readData;
//        }
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    public void init() {
        if (!mSerialIsOpened) {
            openSerialAndRead("ttyO2");
            mSerialIsOpened = true;
        }
//        super.closeSerialPortAndStopRead();
//        mSerialIsOpened = false;
    }
}
