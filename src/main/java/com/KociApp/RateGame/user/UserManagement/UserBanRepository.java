package com.KociApp.RateGame.user.UserManagement;

import com.KociApp.RateGame.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {

    Optional<UserBan> findByUser(User user);

    void deleteAllByUser_Id(Long id);

}
