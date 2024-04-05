package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Exceptions.ResourceNotFoundException;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Repository.ChannelOwnerRepo;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class YoutubeAPIService {
    @Value("${google.client.id}")
    private String CLIENT_ID;
    @Value("${google.client.secret}")
    private String CLIENT_SECRET;
    private final TokenEncryptionService tokenEncryptionService;
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final ChannelOwnerRepo channelOwnerRepo;

    public String generateAccessToken(String email) throws Exception {
        ChannelOwner channelOwner = channelOwnerRepo.findById(email).orElseThrow(() -> new ResourceNotFoundException("Channel", "Email", email));
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("refresh_token", tokenEncryptionService.decryptToken(channelOwner.getRefreshToken()))
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
                return tokenResponse.getAccessToken();
            } else {
                throw new RuntimeException(response.message());
            }
        }
    }
    public SuccessResponse upload(MultipartFile file,String channelEmail,String videoTitle, String videoDescription, String videoTags, MultipartFile thumbnailFile) throws Exception {
        String[] tagsArray = videoTags.split(",");
        VideoSnippet videoSnippet = new VideoSnippet();
        videoSnippet.setTitle(videoTitle);
        videoSnippet.setDescription(videoDescription);
        videoSnippet.setTags(Arrays.stream(tagsArray).toList());

        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("unlisted");

        Video video = new Video();
        video.setSnippet(videoSnippet);
        video.setStatus(status);

        InputStreamContent mediaContent = new InputStreamContent("video/*",file.getInputStream());

        GoogleCredential credential = new GoogleCredential().setAccessToken(generateAccessToken(channelEmail));
        YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("youtube-upload")
                .build();
        YouTube.Videos.Insert videoInsert = youtube.videos().insert("snippet,status", video, mediaContent);
        MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        Video uploadedVideo = videoInsert.execute();

        String endpoint = "https://www.googleapis.com/upload/youtube/v3/thumbnails/set?videoId=" + uploadedVideo.getId();

        OkHttpClient client = new OkHttpClient();

        InputStream inputStream = thumbnailFile.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        RequestBody requestBody = RequestBody.create(MediaType.parse(thumbnailFile.getContentType()), bytes);

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", "Bearer " + generateAccessToken(channelEmail))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Thumbnail uploaded successfully.");
            } else {
                System.err.println("Failed to upload thumbnail. Status code: " + response.code());
            }
        }

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Video uploaded: " + uploadedVideo.getId());
        successResponse.setStatus(true);
        return successResponse;
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
