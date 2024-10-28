package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.CreateUserDto;
import com.emmariescurrena.bookesy.user_service.dtos.UpdateUserDto;
import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody CreateUserDto userDto) {
        return userService.createUser(userDto);
    }


    @GetMapping("/byUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.of(userService.getUserByUsername(username));
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.of(userService.getUserByEmail(email));
    }


    @PatchMapping("/byUsername/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or #username == currentUser.getUsername()")
    public User updateUserByUsername(
        @PathVariable String username,
        @Valid @RequestBody UpdateUserDto userDto,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<User> optionalUserToUpdate = userService.getUserByUsername(username);
        User userToUpdate = getUserFromOptional(optionalUserToUpdate);
        return updateUser(userToUpdate, userDto);
    }

    @PatchMapping("/byEmail/{email}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or #email == currentUser.getEmail()")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserByEmail(
        @PathVariable String email,
        @Valid @RequestBody UpdateUserDto userDto,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<User> optionalUserToUpdate = userService.getUserByEmail(email);
        User userToUpdate = getUserFromOptional(optionalUserToUpdate);
        return updateUser(userToUpdate, userDto);
    }


    @DeleteMapping("/byUsername/{username}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or #username == currentUser.getUsername()")
    public ResponseEntity<String> deleteUserByUsername(
        @PathVariable String username,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<User> optionalUserToDelete = userService.getUserByUsername(username);
        User userToDelete = getUserFromOptional(optionalUserToDelete);
        return deleteUser(userToDelete);
    }

    @DeleteMapping("/byEmail/{email}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or #email == currentUser.getEmail()")
    public ResponseEntity<String> deleteUserByEmail(
        @PathVariable String email,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<User> optionalUserToDelete = userService.getUserByEmail(email);
        User userToDelete = getUserFromOptional(optionalUserToDelete);
        return deleteUser(userToDelete);
    }

    private User updateUser(User userToUpdate, UpdateUserDto userDto) {
        return userService.updateUser(userToUpdate, userDto);
    }

    private ResponseEntity<String> deleteUser(User userToDelete) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

    private User getUserFromOptional(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return optionalUser.get();
    }

}
