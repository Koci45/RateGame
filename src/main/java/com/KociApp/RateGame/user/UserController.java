package com.KociApp.RateGame.user;

import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public User getUser(@PathVariable Long id){

        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @GetMapping("/tokens")
    public List<VeryficationToken> getTokens(){
        return userService.getTokens();
    }

    @GetMapping("/tokens/{id}")
    public VeryficationToken getTokenByUserId(@PathVariable Long id){
        return userService.getTokenByUserId(id);
    }

    @PostMapping("/ban")
    public UserBan banUserById(
            @RequestParam(required = true) Long userId,
            @RequestParam(required = true) int duration
    ){
        return userService.banUserById(userId, duration);
    }
}
