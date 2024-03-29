package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Entity.Editor;
import com.example.youtubeupload.Payloads.UserInfo;
import com.example.youtubeupload.Repository.ChannelOwnerRepo;
import com.example.youtubeupload.Repository.EditorRepo;
import com.example.youtubeupload.Security.TokenIntrospector;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenExchange {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    private static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";

    private final ChannelOwnerRepo channelOwnerRepo;
    private final EditorRepo editorRepo;
    private final TokenIntrospector tokenIntrospector;


    public UserInfo exchangeCodeForTokens(String authorizationCode) throws Exception {
        OkHttpClient client = new OkHttpClient();

        HttpUrl urlBuilder = Objects.requireNonNull(HttpUrl.parse(GOOGLE_TOKEN_URL))
                .newBuilder()
                .build();

        RequestBody formBody = new FormBody.Builder()
                .addEncoded("client_id", clientId)
                .addEncoded("client_secret", clientSecret)
                .addEncoded("redirect_uri", redirectUri)
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
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        Request userInfoRequest = new Request.Builder()
                .url(userInfoUrl)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response userInfoResponse = client.newCall(userInfoRequest).execute();
        String userInfoJsonResponse = userInfoResponse.body().string();
        Map userInfoMap = gson.fromJson(userInfoJsonResponse, Map.class);

        String userEmail = (String) userInfoMap.get("email");
        String userName = (String) userInfoMap.get("name");
        String scope = tokenIntrospector.getTokenScope(accessToken);
        System.out.println(scope);
        System.out.println(scope.length());
        if(scope.length()>90&&!channelOwnerRepo.existsAllByEmail(userEmail)&&!editorRepo.existsAllByEmail(userEmail)){
            ChannelOwner channelOwner = getChannelInfo(accessToken);
            channelOwner.setEmail(userEmail);
            channelOwner.setRefreshToken(refreshToken);
            channelOwnerRepo.save(channelOwner);
        }else if(scope.length()>90&&!editorRepo.existsAllByEmail(userEmail)&&!channelOwnerRepo.existsAllByEmail(userEmail)){
            Editor editor = new Editor(userEmail,null);
            editorRepo.save(editor);
        }
        return new UserInfo(userEmail,userName);
    }
    public static  ChannelOwner getChannelInfo(String accessToken) throws IOException {
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
        String id = (String) channelInfo.get("id");
        String channelName = (String)((Map<String, Object>) channelInfo.get("snippet")).get("title");
        return new ChannelOwner(null,id,channelName,null,null);
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