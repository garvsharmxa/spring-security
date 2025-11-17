package com.garv.SpringSecEx.Controller;

import com.garv.SpringSecEx.Entity.Users;
import com.garv.SpringSecEx.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Users user) {
        return userService.login(user);
    }
}
