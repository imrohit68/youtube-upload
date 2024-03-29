package com.example.youtubeupload.Service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OAuthFlowInitiation {
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    private static final String GOOGLE_REQUIRED_SCOPES_CHANNEL = "https://www.googleapis.com/auth/youtube.upload"  +  " " + "https://www.googleapis.com/auth/youtube.readonly"+ " " +"https://www.googleapis.com/auth/userinfo.email"+ " " + "https://www.googleapis.com/auth/userinfo.profile";
    private static final String GOOGLE_REQUIRED_SCOPES_EDITOR = "https://www.googleapis.com/auth/userinfo.email"+ " " + "https://www.googleapis.com/auth/userinfo.profile";

    public  String getAuthorizationUrl(String scope) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String requestedScope = null;
        if(Objects.equals(scope, "Owner")){
            requestedScope = GOOGLE_REQUIRED_SCOPES_CHANNEL;
        }else{
            requestedScope  = GOOGLE_REQUIRED_SCOPES_EDITOR;
        }

        HttpUrl urlBuilder = Objects.requireNonNull(HttpUrl.parse(GOOGLE_AUTH_URL))
                .newBuilder()
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("redirect_uri", redirectUri)
                .addQueryParameter("scope", requestedScope)
                .addQueryParameter("response_type", "code")
                .addQueryParameter("access_type", "offline")
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder)
                .build();

        return request.url().toString();
    }
}
