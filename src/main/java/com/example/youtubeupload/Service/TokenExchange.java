package com.example.youtubeupload.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Service
public class TokenExchange {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String YOUR_CLIENT_ID = "581068937963-m8605c744a3bm0e2g7fb0p9f90eht4h3.apps.googleusercontent.com";
    private static final String YOUR_CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";
    private static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";


    public  String exchangeCodeForTokens(String authorizationCode) throws Exception {
        OkHttpClient client = new OkHttpClient();

        HttpUrl urlBuilder = Objects.requireNonNull(HttpUrl.parse(GOOGLE_TOKEN_URL))
                .newBuilder()
                .build();

        RequestBody formBody = new FormBody.Builder()
                .addEncoded("client_id", YOUR_CLIENT_ID)
                .addEncoded("client_secret", YOUR_CLIENT_SECRET)
                .addEncoded("redirect_uri", REDIRECT_URI)
                .addEncoded("grant_type", AUTHORIZATION_CODE_GRANT_TYPE)
                .addEncoded("code", authorizationCode)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String jsonResponse = response.body().string();

        Gson gson = new Gson();
        Map responseMap = gson.fromJson(jsonResponse, Map.class);

        String accessToken = (String) responseMap.get("access_token");
        String refreshToken = (String) responseMap.get("refresh_token");

        getChannelInfo(accessToken);
        return "Successfully obtained tokens: access_token = " + accessToken + ", refresh_token = " + refreshToken;
    }
    public static  void getChannelInfo(String accessToken) throws IOException {
        String apiKey = "AIzaSyBoYPeqZ3q-fpslHQ4xhAGy72EnmbJ9Yxw";

        String urlString = "https://www.googleapis.com/youtube/v3/channels?part=snippet&mine=true&key=" + apiKey;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Set Authorization header
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP Error: " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\A");
        String responseJson = scanner.next();
        scanner.close();

        Map<String, Object> channelInfo = parseJson(responseJson);

        if (channelInfo != null) {
            System.out.println("Channel ID: " + channelInfo.get("id"));
            System.out.println("Channel Title: " + ((Map<String, Object>) channelInfo.get("snippet")).get("title"));
        } else {
            System.out.println("Channel information not found.");
        }
    }

    private static Map<String, Object> parseJson(String json) {
        Gson gson = new Gson();
        Map responseMap = gson.fromJson(json, Map.class);
        List<Map<String, Object>> items = (List<Map<String, Object>>) responseMap.get("items");
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }
}