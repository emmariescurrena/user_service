package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserInfoService;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
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
        Optional<User> optionalUserToUpdate = userService.getUserByEmail(email);
        User userToUpdate = getUserFromOptional(optionalUserToUpdate);

        User currentUser = getCurrentUser(accessToken);

        if (!havePermission(userToUpdate, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to update this user");
        }

        return userService.updateUser(userToUpdate, userDto);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(
        @PathVariable String email,
        @RequestHeader("Authorization") String accessToken
    ) {
        Optional<User> optionalUserToDelete = userService.getUserByEmail(email);
        User userToDelete = getUserFromOptional(optionalUserToDelete);

        User currentUser = getCurrentUser(accessToken);

        if (!havePermission(userToDelete, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to delete this user");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

    private User getUserFromOptional(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return optionalUser.get();
    }

    private Boolean havePermission(User userToModify, User currentUser) {
        if (!userToModify.getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().equals(List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))) {
            return false;
        }
        return true;
    }

    private User getCurrentUser(String accessToken) {
        String currentUserEmail = userInfoService.getEmail(accessToken);
        User currentUser = userService.getUserByEmail(currentUserEmail).get();
        return currentUser;
    }


}
