package com.KociApp.RateGame.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class APISecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/users").hasAuthority("ADMIN")
                        .requestMatchers("/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reports").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/reports").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reports/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reviews").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/reviewLikes").hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().permitAll());


        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
