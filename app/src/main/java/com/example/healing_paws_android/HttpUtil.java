package com.example.healing_paws_android;
//this is a class to realize specific interaction with server
//reference https://www.cnblogs.com/HenuAJY/p/10884055.html
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class HttpUtil {
    //login
    static void loginWithOkHttp(String address,String username,String password,Boolean remember_me, okhttp3.Callback callback){
        final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .add("remember_me",remember_me.toString())
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        System.out.println(request);
        System.out.println(body.toString());
        client.newCall(request).enqueue(callback);
    }
    //this is a connection test
//    static void getUrl(String url){
//        OkHttpClient httpClient = new OkHttpClient.Builder().build();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        try {
//            Response response = httpClient.newCall(request).execute();
//            if (response.isSuccessful() && response.body() != null) {
//                System.out.println(response.body().string());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //register
//    static void registerWithOkHttp(String address,String account,String password,okhttp3.Callback callback){
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("registerAccount",account)
//                .add("registerPassword",password)
//                .build();
//        Request request = new Request.Builder()
//                .url(address)
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(callback);
//    }
}