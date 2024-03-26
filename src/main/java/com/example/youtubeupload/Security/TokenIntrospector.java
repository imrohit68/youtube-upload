//package com.example.youtubeupload.Security;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//
//@Component
//public class TokenIntrospector {
//
//    public boolean validateToken(String accessToken) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?access_token=" + accessToken;
//
//        Request request = new Request.Builder()
//                .url(tokenInfoUrl)
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    @Getter
//    @Setter
//    static
//    class TokenInfoResponse {
//        private String error;
//        private String errorDescription;
//    }
//
//}
