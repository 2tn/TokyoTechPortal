package com.example.yusuke.broadcastreceiversample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by Yusuke on 2016/01/07.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        Log.d("BroadcastReceiverTest","onCreate");
    }
}
