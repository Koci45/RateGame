package com.KociApp.RateGame.game.cover;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cover")
public class Cover {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "game")
    private int game;

    @Column(name = "url")
    private String url;
}
