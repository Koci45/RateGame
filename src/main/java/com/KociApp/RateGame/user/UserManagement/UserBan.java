package com.KociApp.RateGame.user.UserManagement;

import com.KociApp.RateGame.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "duration")
    Date duration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
