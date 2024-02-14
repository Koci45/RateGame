package com.KociApp.RateGame.game;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "developer")
    private String developer;

    @Column(name = "platforms")
    @ElementCollection(targetClass = Platforms.class)
    @Enumerated(EnumType.STRING)
    private List<Platforms> platforms;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "genre")
    private String genre;
}
