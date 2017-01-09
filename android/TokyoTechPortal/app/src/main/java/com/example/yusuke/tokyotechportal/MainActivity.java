package com.example.yusuke.tokyotechportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String username = "";
    String password = "";
    String matrix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TokyoTechPortal", "onCreate");

        WebView webview = (WebView) findViewById(R.id.webView);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        map.put("matrix", matrix);
        LoginTask task = new LoginTask(this,webview);
        task.execute(map);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TokyoTechPortal", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TokyoTechPortal", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TokyoTechPortal", "onPause");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("TokyoTechPortal", "onRestart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TokyoTechPortal", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TokyoTechPortal", "onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView webview = (WebView) findViewById(R.id.webView);
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
