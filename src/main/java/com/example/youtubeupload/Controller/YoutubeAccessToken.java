package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Payloads.UploadRequest;
import com.example.youtubeupload.Payloads.UserInfo;
import com.example.youtubeupload.Service.OAuthFlowInitiation;
import com.example.youtubeupload.Service.TokenExchange;
import com.example.youtubeupload.Service.YoutubeAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
public class YoutubeAccessToken {


    private final OAuthFlowInitiation oAuthFlowInitiation;

    private final TokenExchange tokenExchange;
    private final YoutubeAPI youtubeAPI;

    @GetMapping("/getAccess/{scope}")
    public void initiateOAuth2Flow(HttpServletResponse response, @PathVariable String scope) throws Exception {
        String authorizationUrl = oAuthFlowInitiation.getAuthorizationUrl(scope);
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<UserInfo> handleRedirect(@RequestParam("code") String code) throws Exception {
        return new ResponseEntity<>
                (tokenExchange.exchangeCodeForTokens(code),
                        HttpStatus.OK);
    }

    @PostMapping("/upload/{email}")
    public String uploadVideo(@PathVariable String email,@RequestParam("file") MultipartFile file,@RequestParam("image") MultipartFile image) throws IOException {
       youtubeAPI.upload(file, youtubeAPI.generateAccessToken(email),"Hello");
       return "Done";
    }
}
