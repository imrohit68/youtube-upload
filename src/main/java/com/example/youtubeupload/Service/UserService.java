package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.User;
import com.example.youtubeupload.Exceptions.ResourceNotFoundException;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public SuccessResponse createUser(User user, String role){
        if(role.equals("Owner")){
            String encoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(encoded);
            user.setRole(User.ROLE.OWNER);
            userRepo.save(user);
            return new SuccessResponse("User Created Successfully",true);
        }
        else{
            String encoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(encoded);
            user.setRole(User.ROLE.EDITOR);
            userRepo.save(user);
            return new SuccessResponse("User Created Successfully",true);
        }

    }
    public SuccessResponse changePassword(String email, String password){
        User user = userRepo.findById(email)
                .orElseThrow(()-> new ResourceNotFoundException("User","email",email));
        String pass = passwordEncoder.encode(password);
        user.setPassword(pass);
        userRepo.save(user);
        return new SuccessResponse("Password Updated Successfully",true);
    }
}
