package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_findByEmail_ReturnsByEmail(){

        User user = new User();
        user.setEmail("test@email.com");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setUsername("test");
        user.setRole("TEST");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("test@email.com");
        Assertions.assertThat(foundUser.isPresent()).isEqualTo(true);
        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo("test@email.com");
    }
}
