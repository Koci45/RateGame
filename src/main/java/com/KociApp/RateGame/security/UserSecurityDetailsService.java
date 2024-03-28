package com.KociApp.RateGame.security;

import com.KociApp.RateGame.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSecurityDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws EntityNotFoundException{

        return userRepository.findByEmail(email).map(UserSecurityDetails::new).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
}
