package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.security.UserSecurityDetailsService;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSecurityDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSecurityDetailsService userSecurityDetailsService;

    @Test
    public void loadUserByUsernameWhenUserNameExists(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);
        User admin = new User(1L, "admin", "admin", "123456", "ADMIN", true);

        when(userRepository.findByEmail("test@test1")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("admin")).thenReturn(Optional.of(admin));

        UserDetails userDetails = userSecurityDetailsService.loadUserByUsername("test@test1");
        UserDetails adminDetails = userSecurityDetailsService.loadUserByUsername("admin");

        Assertions.assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(userDetails.getAuthorities().stream().toList().get(0).getAuthority()).isEqualTo(user.getRole());
        Assertions.assertThat(adminDetails.getAuthorities().stream().toList().get(0).getAuthority()).isEqualTo(admin.getRole());
    }

    @Test
    public void loadUserByUsernameWhenUserNameNotExists(){

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userSecurityDetailsService.loadUserByUsername("test")).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
