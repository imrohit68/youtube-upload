package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Service.OAuthFlowInitiation;
import com.example.youtubeupload.Service.TokenExchange;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class YoutubeAccessToken {


    private final OAuthFlowInitiation oAuthFlowInitiation;

    private final TokenExchange tokenExchange;

    @GetMapping("/start-oauth2-flow")
    public void initiateOAuth2Flow(HttpServletResponse response) throws Exception {
        String authorizationUrl = oAuthFlowInitiation.getAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }
    @GetMapping("/login/oauth2/code/google")
    public String handleRedirect(@RequestParam("code") String code) throws Exception {
        return tokenExchange.exchangeCodeForTokens(code);
    }
    @GetMapping("/secured")
    @PreAuthorize("hasRole('OWNER')")
    public String secured(){
        return "This is a secured Endpoint";
    }
}