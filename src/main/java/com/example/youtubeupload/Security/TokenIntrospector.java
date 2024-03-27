package com.example.youtubeupload.Security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component

public class TokenIntrospector {

    public String getTokenScope(String accessToken) {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?access_token=" + accessToken;

        Request request = new Request.Builder()
                .url(tokenInfoUrl)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.has("scope") ? jsonNode.get("scope").asText() : null;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
