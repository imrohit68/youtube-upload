package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Entity.User;
import com.example.youtubeupload.Payloads.LoginRequest;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/createUser/{role}")
    public ResponseEntity<SuccessResponse> createUser(@RequestBody User user, @PathVariable String role){
        return new ResponseEntity<>(
                userService.createUser(user,role),
                HttpStatus.OK);
    }
    @PostMapping("/changePassword")
    public ResponseEntity<SuccessResponse> changePassword(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(
                userService.changePassword(loginRequest.getEmail(),loginRequest.getPassword()),
                HttpStatus.OK);
    }
}
