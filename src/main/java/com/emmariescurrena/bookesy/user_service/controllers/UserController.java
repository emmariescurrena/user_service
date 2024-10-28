package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
import com.emmariescurrena.bookesy.user_service.models.RoleEnum;
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
    public User updateUserByUsername(
        @PathVariable String username,
        @Valid @RequestBody UpdateUserDto userDto,
        Authentication authentication
    ) {
        Optional<User> optionalUserToUpdate = userService.getUserByUsername(username);
        User userToUpdate = getUserFromOptional(optionalUserToUpdate);
        return updateUser(userToUpdate, userDto, authentication);
    }

    @PatchMapping("/byEmail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserByEmail(
        @PathVariable String email,
        @Valid @RequestBody UpdateUserDto userDto,
        Authentication authentication
    ) {
        Optional<User> optionalUserToUpdate = userService.getUserByEmail(email);
        User userToUpdate = getUserFromOptional(optionalUserToUpdate);
        return updateUser(userToUpdate, userDto, authentication);
    }


    private User updateUser(User userToUpdate, UpdateUserDto userDto, Authentication authentication) {
        if (!havePermission(userToUpdate, authentication)) {
            throw new AccessDeniedException("You don't have the permission to update this user");
        }
        return userService.updateUser(userToUpdate, userDto);
    }


    private Boolean havePermission(User userToModify, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long currentUserId = (Long) jwt.getClaim("sub");

        Boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleEnum.SUPER_ADMIN.toString()));

        if (!userToModify.getId().equals(currentUserId) && !isAdmin) {
            return false;
        }

        return true;
    }

    private User getUserFromOptional(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return optionalUser.get();
    }

}
