package com.KociApp.RateGame.registration.token;

import com.KociApp.RateGame.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VeryficationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration_time")
    private Date expirationTime;

    private static final int tokenExpirationTime = 15;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VeryficationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, tokenExpirationTime);

        return new Date(calendar.getTime().getTime());
    }
}
