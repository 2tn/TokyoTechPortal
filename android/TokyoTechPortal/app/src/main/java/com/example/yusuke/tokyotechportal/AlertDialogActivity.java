package com.example.yusuke.tokyotechportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Yusuke on 2016/04/17.
 */
public class AlertDialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        setContentView(R.layout.alertdialogview);
        new AlertDialog.Builder(this)
                .setTitle(intent.getStringExtra("TITLE"))
                .setMessage(intent.getStringExtra("MESSAGE"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }
}
