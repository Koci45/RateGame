package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import com.KociApp.RateGame.user.UserManagement.UserBanRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserBanRepositoryTests {

    @Autowired
    UserBanRepository userBanRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void UserBanRepository_findByUser_returnsByUser(){

        User[] users = new User[3];

        for(int i = 0; i < 3; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
            entityManager.merge(users[i]);
        }

        UserBan userBan = new UserBan();
        userBan.setDuration(new Date());
        userBan.setUser(users[1]);

        userBanRepository.save(userBan);

        Assertions.assertThat(userBanRepository.findByUser(users[1]).isPresent()).isEqualTo(true);
        Assertions.assertThat(userBanRepository.findByUser(users[2]).isPresent()).isEqualTo(false);
        Assertions.assertThat(userBanRepository.findByUser(users[1]).get().getUser()).isEqualTo(users[1]);
    }

    @Test
    public void UserBanRepository_deleteAllByUser_Id_deletesAllByUserId(){

        User[] users = new User[3];

        for(int i = 0; i < 3; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
            entityManager.merge(users[i]);
        }

        UserBan[] userBans = new UserBan[3];

        for(int i = 0; i < 3; i++){
            userBans[i] = new UserBan();
            userBans[i].setUser(users[0]);
            userBans[i].setDuration(new Date());
            userBanRepository.save(userBans[i]);
        }

        UserBan userBan = new UserBan();
        userBan.setDuration(new Date());
        userBan.setUser(users[1]);

        userBanRepository.save(userBan);

        userBanRepository.deleteAllByUser_Id(users[0].getId());

        Assertions.assertThat(userBanRepository.findByUser(users[0]).isPresent()).isEqualTo(false);
        Assertions.assertThat(userBanRepository.findByUser(users[1]).isPresent()).isEqualTo(true);
    }
}
