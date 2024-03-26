package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Entity.User;
import com.example.youtubeupload.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/createUser")
    public ResponseEntity<Boolean> createUser(@RequestBody User user){
        boolean val =  userService.createUser(user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
