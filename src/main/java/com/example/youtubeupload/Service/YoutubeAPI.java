package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Exceptions.ResourceNotFoundException;
import com.example.youtubeupload.Repository.ChannelOwnerRepo;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import okio.BufferedSink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class YoutubeAPI {
    @Value("${google.client.id}")
    private String CLIENT_ID;
    @Value("${google.client.secret}")
    private String CLIENT_SECRET;
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final ChannelOwnerRepo channelOwnerRepo;

    public String generateAccessToken(String email) throws IOException {
        ChannelOwner channelOwner = channelOwnerRepo.findById(email).orElseThrow(() -> new ResourceNotFoundException("Channel", "Email", email));
        OkHttpClient client = new OkHttpClient();
        System.out.println(channelOwner.getRefreshToken());

        RequestBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("refresh_token", channelOwner.getRefreshToken())
                .add("grant_type", "refresh_token")
                .add("scope", "https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube.upload")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                TokenResponse tokenResponse = new Gson().fromJson(responseBody, TokenResponse.class);
                System.out.println(tokenResponse.getAccessToken());
                return tokenResponse.getAccessToken();
            } else {
                throw new RuntimeException(response.message());
            }
        }
    }
    public  String upload(MultipartFile file , String accessToken, String videoTitle) throws IOException {
        OkHttpClient client = new OkHttpClient();

        if (file.isEmpty()) {
            return "File is empty";
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("video", file.getOriginalFilename(),
                        RequestBodyUtil.create(file.getInputStream(), "video/*"))
                .build();

        Request request = new Request.Builder()
                .url("https://www.googleapis.com/upload/youtube/v3/videos?part=snippet")
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            String responseBody = response.body().string();
            System.out.println(responseBody);
            return "Done";
        }

    }
    static class RequestBodyUtil {
        static RequestBody create(final InputStream inputStream, final String contentType) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse(contentType);
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        sink.write(buffer, 0, bytesRead);
                    }
                }
            };
        }
    }


    static class TokenResponse {
        private String access_token;
        private long expires_in;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccessToken(String accessToken) {
            this.access_token = accessToken;
        }

        public long getExpiresIn() {
            return expires_in;
        }

        public void setExpiresIn(long expiresIn) {
            this.expires_in = expiresIn;
        }
    }
}
