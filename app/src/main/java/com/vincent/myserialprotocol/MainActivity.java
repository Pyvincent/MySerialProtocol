package com.vincent.myserialprotocol;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity { // implements ServiceConnection {

    private Intent intent;
    //    private MainService.Binder myBinder = null;
    private TextView textView_read, show;
    private ReciveData receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, MainService.class);
        startService(intent);

        textView_read = (TextView) findViewById(R.id.readata);
        show = (TextView) findViewById(R.id.show);

        textView_read.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_read.append("说明：<本测试程序用来接受串口ttyO2收到的数据，数据格式为十六进制，长度为30位,以0x77 0x77开头0x88 0x88结尾>" +
                "\n" + "eg:<77 77 44 36 23 35 35 56 75 89 23 01 66 11 22 33 44 55 66 77 99 12 23 34 34 56 67 78 88 88 >" +
                "\n" + "也可使用adb logcat -v time 查看数据" + "\n");

        receiver = new ReciveData();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vincent.serial.data");
        MainActivity.this.registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }

    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public class ReciveData extends BroadcastReceiver {
        int count = 1;


        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String readData = bundle.getString("readdata");
            Log.i("[收到合法数据] ", readData);
            if (count == 1) {
                String startTime = getTime();
                textView_read.append("\n\n" + "[ 开始收帧时间 ]  " + startTime);
            }
            String curtime = getTime();
            show.setText("[ " + curtime + " ] " + readData + "\n" +
                    "[ 收到帧长度 ]  " + 30 * count + "\n" + "[ 收到帧数量 ]  " + count + "\n" +
                    "[ 结束收帧时间 ]  "+curtime);
            count++;
        }
    }

}


