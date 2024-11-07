package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.CreateUserDto;
import com.emmariescurrena.bookesy.user_service.dtos.UpdateUserDto;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserInfoService;
import com.emmariescurrena.bookesy.user_service.services.UserService;
import com.emmariescurrena.bookesy.user_service.util.ControllerHelper;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto userDto) {
        Optional<User> existingUser = userService.getUserByEmail(userDto.getEmail());
        
        if (existingUser.isPresent()) {
            return ResponseEntity.ok("User already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        return ResponseEntity.of(userService.getUserByEmail(email));
    }

    @PatchMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(
        @PathVariable String email,
        @Valid @RequestBody UpdateUserDto userDto,
        @RequestHeader("Authorization") String accessToken
    ) {
        User userToUpdate = ControllerHelper.getUserFromOptional(userService.getUserByEmail(email));

        User currentUser = ControllerHelper.getCurrentUser(accessToken);

        if (!ControllerHelper.hasPermission(userToUpdate, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to update this user");
        }

        userInfoService.updateUser(userToUpdate.getAuth0UserId(), userDto);
        return userService.updateUser(userToUpdate, userDto);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(
        @PathVariable String email,
        @RequestHeader("Authorization") String accessToken
    ) {
        User userToDelete = ControllerHelper.getUserFromOptional(userService.getUserByEmail(email));

        User currentUser = ControllerHelper.getCurrentUser(accessToken);

        if (!ControllerHelper.hasPermission(userToDelete, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to delete this user");
        }

        userInfoService.deleteUser(userToDelete.getAuth0UserId());
        userService.deleteUser(userToDelete);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

}
