package com.example.chopar_1.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SmsServerService {
    @Value("${sms.fly.uz.url}")
    private String url;
    @Value("${my.eskiz.uz.email}")
    private String email;
    @Value("${my.eskiz.uz.password}")
    private String password;
    public  void send (String phone, String text,String code){
sendSmsHTTP(phone,text+code);
    }
    private void sendSmsHTTP(String phone, String text) {
        String token = "Bearer " + getTokenWithAuthorization();

        OkHttpClient client = new OkHttpClient();

        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone", phone)
                .addFormDataPart("message", text)
                .addFormDataPart("from", "4546")
                .build();

        Request request = new Request.Builder()
                .url(url + "/api/message/sms/send")
                .method("POST", body)
                .header("Authorization", token)
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*Thread thread = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();*/
    }

    private String getTokenWithAuthorization() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url( url +"/api/auth/login")
                .method("POST", body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException();
            } else {
                JSONObject object = new JSONObject(response.body().string());
                JSONObject data = object.getJSONObject("data");
                Object token = data.get("token");
                return token.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}
