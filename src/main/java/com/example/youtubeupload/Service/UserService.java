package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.User;
import com.example.youtubeupload.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public boolean createUser(User user){
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        userRepo.save(user);
        return true;
    }
}
