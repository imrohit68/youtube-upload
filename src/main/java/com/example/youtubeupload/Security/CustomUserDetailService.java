package com.example.youtubeupload.Security;

import com.example.youtubeupload.Entity.User;
import com.example.youtubeupload.Exceptions.ResourceNotFoundException;
import com.example.youtubeupload.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepo.findById(username)
                .orElseThrow(()->new ResourceNotFoundException("User","email",0));
    }
}
