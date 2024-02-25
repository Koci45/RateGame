package com.KociApp.RateGame.game;

import com.KociApp.RateGame.game.genre.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "cover")
    private int cover;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "genres")
    private String genres;

    @Column(name = "platforms", length = 800)
    private String platforms;
}
