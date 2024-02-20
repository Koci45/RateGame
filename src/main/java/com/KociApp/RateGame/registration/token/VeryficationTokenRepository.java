package com.KociApp.RateGame.registration.token;

import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeryficationTokenRepository extends JpaRepository<VeryficationToken, Long> {

    VeryficationToken findByToken(String token);

    void deleteByUserId(long userId);
}
