package com.KociApp.RateGame.user;

import com.KociApp.RateGame.exception.user.UserNotFoundException;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/byId/{id}")
    public Optional<User> getUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException("User with id-" + id + " Not Found");
        }
        return user;
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }
}
