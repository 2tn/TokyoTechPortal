package com.example.yusuke.tokyotechportal;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;

/**
 * Created by Yusuke on 2016/01/03.
 */
public class MyClass {

    static class TokyoTechPortal {
        String username = "";
        String password = "";
        String matrix = "";
        String referer = "";
        CookieManager cookies;

        public void Login() throws Exception {
            String result;
            cookies = new CookieManager();
            //パスワード認証フォームの取得
            try {
                HttpGet get = new HttpGet("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login?Template=userpass_key&AUTHMETHOD=UserPassword");
                get.cookie = cookies;
                get.referer = referer;
                get.start();
                get.join();
                referer = get.referer;
                result = get.result;
                if (get.errMsg != null) {
                    throw new Exception(get.errMsg);
                }
            } catch (Exception ex) {
                throw ex;
            }

            //必要なデータの取得
            Pattern pattern = Pattern.compile("<input type=('|\")hidden('|\") name=('|\")(.+?)('|\") value=('|\")(.+?)('|\")>(\n|<)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(result);
            HashMap<String, String> map = new HashMap<String, String>();
            while (matcher.find()) {
                map.put(matcher.group(4), matcher.group(7));
            }
            map.put("usr_name", username);
            map.put("usr_password", password);
            map.put("AUTHTYPE", "");
            //パスワード認証の送信

            try {
                HttpPost post = new HttpPost("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login", map);
                post.referer = referer;
                post.cookie = cookies;
                post.start();
                post.join();
                referer = post.referer;
                result = post.result;
                if (post.errMsg != null) {
                    throw new Exception(post.errMsg);
                }
                //TODO:認証情報が正しくない時の例外処理
                if (result.equals("")) {
                    throw new Exception("ユーザー名かパスワードが間違っています");
                }
            } catch (Exception ex) {
                throw ex;
            }

            //必要なデータの取得
            matcher = pattern.matcher(result);
            map = new HashMap<String, String>();
            while (matcher.find()) {
                map.put(matcher.group(4), matcher.group(7));
            }
            map.put("AUTHTYPE", "");
            pattern = Pattern.compile("\\[(.),(.)\\]", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(result);
            int count = 0;
            while (matcher.find()) {
                int row = (int) (matcher.group(1).charAt(0) - 65);
                int column = Integer.parseInt(matcher.group(2));
                map.put(String.format("message%d", count + 3), String.format("%c", matrix.charAt(row * 7 + column - 1)));
                count++;
            }
            //マトリクス認証の送信
            try {
                HttpPost post = new HttpPost("https://portal.nap.gsic.titech.ac.jp/GetAccess/Login", map);
                post.referer = referer;
                post.cookie = cookies;
                post.start();
                post.join();
                referer = post.referer;
                result = post.result;
                if (post.errMsg != null) {
                    throw new Exception(post.errMsg);
                }
                //TODO:認証情報が正しくない時の例外処理
                if (result.indexOf("The response provided was incorrect. Please try again.") != -1) {
                    throw new Exception("マトリクスが間違っています");
                }
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    public static class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username = "";
            String password = "";

            Bundle bundle = intent.getExtras();
            NetworkInfo networkInfo = (NetworkInfo) bundle.get("networkInfo");
            WifiInfo wifiInfo = (WifiInfo) bundle.get("wifiInfo");
            String bssid = (String) bundle.get("bssid");
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);


            if (networkInfo != null) {
                if ((networkInfo.getState() == NetworkInfo.State.CONNECTED) &&
                        (networkInfo.getExtraInfo().equals("\"TokyoTech\"")) &&
                        (bssid != null) &&
                        (!cm.getActiveNetworkInfo().getTypeName().equals("WIFI"))
                        ) {
                    try {
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                        Toast.makeText(context, "connected TokyoTech", Toast.LENGTH_SHORT).show();
                        Log.d("TokyoTechPortal", networkInfo.toString());
                        Log.d("TokyoTechPortal", wifiInfo.toString());
                        //ログイン処理開始
                        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

                        //ネットワーク接続
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //API21以上の場合
                            Network[] allNetwork = cm.getAllNetworks();
                            SocketFactory socketFactory = SocketFactory.getDefault();
                            for (Network network : allNetwork) {
                                NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
                                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    boolean a = ConnectivityManager.setProcessDefaultNetwork(network);
                                }
                            }
                        } else {
                            //API20以下の場合
                        }

                        //ログイン処理
                        try {
                            MyClass.TokyoTech(username, password);
                        } catch (Exception ex) {

                        }

                        //まつ
                        int id = wifiInfo.getNetworkId();
                        wm.disableNetwork(id);
                        wm.enableNetwork(id, false);
                        wm.enableNetwork(id, true);
                        while (!cm.getActiveNetworkInfo().getTypeName().equals("WIFI")) ;
                        String a = cm.getActiveNetworkInfo().getTypeName();
                        Log.d("TokyoTechPortal", "authenticated");
                        Toast.makeText(context, "TokyoTechにログインしました", Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        Intent i= new Intent(context,AlertDialogActivity.class);
                        i.putExtra("TITLE","ログイン失敗");
                        i.putExtra("MESSAGE",ex.getMessage());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }

                }
            }
        }
    }

    static void TokyoTech(String username, String password) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        map.put("buttonClicked", "4");
        map.put("redirect_url", "");
        map.put("err_flag", "0");
        try {
            HttpPost post = new HttpPost("https://wlanauth.noc.titech.ac.jp/login.html", map);
            post.start();
            post.join();
            if (post.errMsg != null) {
                throw new Exception(post.errMsg);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    static class HttpGet extends Thread {
        private URL url;
        String referer = "";
        CookieManager cookie = null;
        boolean isBusy = true;
        String result = null;
        String encode = "UTF-8";
        String errMsg = null;

        public HttpGet(String urlString) throws Exception {
            try {
                this.url = new URL(urlString);
            } catch (Exception ex) {
                Log.e("TokyoTechPortal", "HttpGet" + ex.getMessage());
                throw ex;
            }
        }

        public void run() {
            try {
                CookieHandler.setDefault(this.cookie);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Referer", referer);
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.connect();
                InputStream in = con.getInputStream();
                StringBuilder src = new StringBuilder();
                while (true) {
                    byte[] line = new byte[1024];
                    int size = in.read(line);
                    if (size <= 0)
                        break;
                    src.append(new String(line, this.encode));
                }
                in.close();
                this.referer = url.toString();
                this.result = src.toString();
                isBusy = false;
            } catch (IOException ex) {
                Log.e("TokyoTechPortal", "HttpGet" + ex.getMessage());
                errMsg = ex.getMessage();
            }
        }
    }


    static class HttpPost extends Thread {
        private URL url;
        CookieManager cookie = null;
        String referer = "";
        boolean isBusy = true;
        String result = null;
        String encode = "UTF-8";
        StringBuilder postData = new StringBuilder();
        String errMsg = null;

        public HttpPost(String urlString, HashMap<String, String> map) throws Exception {
            try {
                this.url = new URL(urlString);
            } catch (Exception ex) {
                Log.e("TokyoTechPortal", "HttpPost" + ex.getMessage());
                throw ex;
            }
            for (Map.Entry<String, String> e : map.entrySet()) {
                postData.append(String.format("%s=%s&", e.getKey(), e.getValue()));
            }
        }

        public void run() {
            try {
                CookieHandler.setDefault(this.cookie);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Referer", referer);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.connect();
                OutputStream out = con.getOutputStream();
                String a = postData.deleteCharAt(postData.length() - 1).toString();
                out.write(a.getBytes());
                out.flush();
                out.close();

                InputStream in = con.getInputStream();
                StringBuilder src = new StringBuilder();
                while (true) {
                    byte[] line = new byte[1024];
                    int size = in.read(line);
                    if (size <= 0)
                        break;
                    src.append(new String(line, this.encode));
                }
                in.close();
                this.referer = url.toString();
                this.result = src.toString();
                isBusy = false;
            } catch (Exception ex) {
                Log.e("TokyoTechPortal", "HttpPost" + ex.getMessage());
                errMsg = ex.getMessage();
            }

        }
    }
}