package com.KociApp.RateGame.registration;


public record RegistrationRequest(
        String username,
        String email,
        String password) {
}
