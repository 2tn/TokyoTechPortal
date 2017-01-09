package com.example.yusuke.tokyotechportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.Field;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;

public class LoginTask extends AsyncTask<HashMap<String, String>, Integer, CookieManager> {
    private WebView webView;
    private Activity activity;
    private ProgressDialog progressDialog;
    String errMsg = null;

    public LoginTask(Activity activity, WebView webView) {
        super();
        this.webView = webView;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("東工大ポータルにログインしています");
        progressDialog.show();
    }

    @Override
    protected CookieManager doInBackground(HashMap<String, String>... map) {
        Log.d("TokyoTechPortal", "TokyoTech start doInBackground");
        MyClass.TokyoTechPortal portal = new MyClass.TokyoTechPortal();
        portal.username = map[0].get("username");
        portal.password = map[0].get("password");
        portal.matrix = map[0].get("matrix");
        try {
            portal.Login();
        } catch (Exception ex) {
            //ログインに失敗したときの例外処理
            errMsg = ex.getMessage();
            return null;
        }
        Log.d("TokyoTechPortal", "TokyoTech end doInBackground");
        return portal.cookies;

    }

    @Override
    protected void onPostExecute(CookieManager manager) {

        Log.d("TokyoTechPortal", "TokyoTech onPostExecute");

        if (manager == null) {
            //ログインに失敗したときの例外処理
            progressDialog.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle("ログインに失敗しました");
            alertDialogBuilder.setMessage(errMsg);
            alertDialogBuilder.setPositiveButton("OK", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            cancel(true);
            return;
        }

        //WebViewにCookieを追加
        android.webkit.CookieManager.getInstance().removeAllCookie();   //なんか前のCookieが残ってるみたいだから削除してあげないと認証情報が二重になってエラー起こす
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        List<HttpCookie> cookies = manager.getCookieStore().getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            android.webkit.CookieManager.getInstance().setCookie(cookies.get(i).getDomain(), cookies.get(i).toString());
        }

        //WebViewの初期設定
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //ズームコントロールを表示せずにマルチタッチズームを有効にする
        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ws.setDisplayZoomControls(false);
        } else {
            try {
                Field field = ws.getClass().getDeclaredField("mBuiltInZoomControls");
                field.setAccessible(true);
                field.set(ws, false);
            } catch (Exception e) {
                ws.setBuiltInZoomControls(false);
            }
        }
        //ズーム設定終了
        webView.setWebViewClient(new WebViewClient() {
        });
        webView.loadUrl("https://portal.nap.gsic.titech.ac.jp/GetAccess/ResourceList");
        progressDialog.dismiss();
    }
}
