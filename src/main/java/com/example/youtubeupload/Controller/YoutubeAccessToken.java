package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Payloads.UserInfo;
import com.example.youtubeupload.Service.OAuthFlowInitiation;
import com.example.youtubeupload.Service.TokenExchange;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class YoutubeAccessToken {


    private final OAuthFlowInitiation oAuthFlowInitiation;

    private final TokenExchange tokenExchange;

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
}
