package com.KociApp.RateGame.importGames;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class GameResponseHolder {
    //this class is used to store json response from igdb in object
    private int id;
    private int cover;
    private long created_at;
    private long first_release_date;
    private List<Integer> genres;
    private String name;
    private List<Integer> platforms;
}
