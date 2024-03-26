package com.example.youtubeupload.Service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OAuthFlowInitiation {
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String YOUR_CLIENT_ID = "581068937963-m8605c744a3bm0e2g7fb0p9f90eht4h3.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";
    private static final String GOOGLE_REQUIRED_SCOPES = "https://www.googleapis.com/auth/youtube.upload"  +  " " + "https://www.googleapis.com/auth/youtube.readonly"+ " " +"https://www.googleapis.com/auth/userinfo.email"+ " " + "https://www.googleapis.com/auth/userinfo.profile";

    public  String getAuthorizationUrl() throws Exception {
        OkHttpClient client = new OkHttpClient();

        HttpUrl urlBuilder = Objects.requireNonNull(HttpUrl.parse(GOOGLE_AUTH_URL))
                .newBuilder()
                .addQueryParameter("client_id", YOUR_CLIENT_ID)
                .addQueryParameter("redirect_uri", REDIRECT_URI)
                .addQueryParameter("scope", GOOGLE_REQUIRED_SCOPES)
                .addQueryParameter("response_type", "code")
                .addQueryParameter("access_type", "offline")
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder)
                .build();

        return request.url().toString();
    }
}
