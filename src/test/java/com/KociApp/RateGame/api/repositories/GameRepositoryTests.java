package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void GameRepository_SaveGame(){

        //Arrange
        Game game = new Game();
        game.setId(0);
        game.setTitle("test");
        game.setPlatforms("tset");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());


        //Act
        Game savedGame = gameRepository.save(game);
        //Assert
        Assertions.assertThat(savedGame).isNotNull();
        Assertions.assertThat(savedGame.getTitle()).isEqualTo("test");
        Assertions.assertThat(savedGame.getId()).isEqualTo(0);
    }

    @Test
    public void GameRepository_findLatestCreatedAt(){

        Date currentDate = new Date();

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("first");
        gameOne.setCreatedAt(new Date(currentDate.getTime()));

        Game gameTwo = new Game();
        gameTwo.setId(2);
        gameTwo.setTitle("second");
        gameTwo.setCreatedAt(new Date(currentDate.getTime() + 86400000));

        Game gameThree = new Game();
        gameThree.setId(3);
        gameThree.setTitle("third");
        gameThree.setCreatedAt(new Date(currentDate.getTime() + 2 * 86400000));

        gameRepository.save(gameOne);
        gameRepository.save(gameTwo);
        gameRepository.save(gameThree);

        Date latestCreatedAt = gameRepository.findLatestCreatedAt();

        Assertions.assertThat(latestCreatedAt).hasSameTimeAs(new Date(currentDate.getTime() + 2 * 86400000));
    }

    @Test
    public void GameRepository_findByTitleContaining(){

        Game gameOne = new Game();
        gameOne.setId(4);
        gameOne.setTitle("The Witcher 1");

        Game gameTwo = new Game();
        gameTwo.setId(5);
        gameTwo.setTitle("The Witcher 2");


        Game gameThree = new Game();
        gameThree.setId(6);
        gameThree.setTitle("Grand theft auto");

        gameRepository.save(gameOne);
        gameRepository.save(gameTwo);
        gameRepository.save(gameThree);

        List<Game> foundGames = gameRepository.findByTitleContaining("Witcher");

        Assertions.assertThat(foundGames.size()).isEqualTo(2);
        Assertions.assertThat(foundGames.stream().allMatch(game -> game.getTitle().toLowerCase().contains("witcher"))).isEqualTo(true);

    }




}
