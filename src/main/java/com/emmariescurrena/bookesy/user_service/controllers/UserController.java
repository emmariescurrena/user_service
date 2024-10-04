package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.UserDto;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserService userService;

    @GetMapping
    public User getUser() {
        User user = new User();
        user.setName("Robert");

        return user;
    }

    @PostMapping
    public ResponseEntity<?> createUser(User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return Optional
            .ofNullable(userService.getUserById(id))
            .map( user -> ResponseEntity.ok(new UserDto(user.get())) )          //200 OK
            .orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return Optional
            .ofNullable(userService.getUserByEmail(email))
            .map( user -> ResponseEntity.ok(new UserDto(user.get())) )          //200 OK
            .orElseGet( () -> ResponseEntity.notFound().build() );
    }

}
