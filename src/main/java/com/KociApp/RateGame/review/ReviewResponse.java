package com.KociApp.RateGame.review;

import com.KociApp.RateGame.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class ReviewResponse {

    private Long id;
    private String content;
    private Date creationDate;
    private Game game;
    private String username;
    private byte rating;
}
