package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Payloads.LoginRequest;
import com.example.youtubeupload.Security.CustomUserDetailService;
import com.example.youtubeupload.Security.JwtTokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenHelper jwtTokenHelper;
    private final CustomUserDetailService customUserDetailService;
    private final AuthenticationManager authenticationManager;
    
    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        this.authenticate(loginRequest.getEmail(),loginRequest.getPassword());
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(loginRequest.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
