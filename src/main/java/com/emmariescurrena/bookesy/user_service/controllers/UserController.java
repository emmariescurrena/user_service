package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserInfoService;
import com.emmariescurrena.bookesy.user_service.services.UserService;
import com.emmariescurrena.bookesy.user_service.util.ControllerHelper;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public Mono<ResponseEntity<?>> createUser(@Valid @RequestBody CreateUserDto userDto) {
        return userService.getUserByEmail(userDto.getEmail())
            .flatMap(existingUser ->
                Mono.just(ResponseEntity.ok("User already exists"))
            )
            .flatMap(response ->
                response.getStatusCode() == HttpStatus.OK ? Mono.just(response) :
                userService.createUser(userDto).map(newUser -> ResponseEntity.ok(newUser))
            );
    }

    @GetMapping("/{email}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable String email) {
        return userService.getUserByEmail(email)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<User>> updateUser(
        @PathVariable String email,
        @Valid @RequestBody UpdateUserDto userDto,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToUpdate ->
                Mono.justOrEmpty((User) userDetailsService.loadUserByUsername(accessToken.getSubject()))
                .flatMap(currentUser -> {
                    if (!ControllerHelper.hasPermission(userToUpdate, currentUser)) {
                        return Mono.error(new AccessDeniedException("You don't have the permission to update this user"));
                    }
                    userInfoService.updateUser(userToUpdate.getAuth0UserId(), userDto);
                    return userService.updateUser(userToUpdate, userDto)
                    .map(updatedUser -> ResponseEntity.ok(updatedUser));
                })
            );
    }

    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<String>> deleteUser(
        @PathVariable String email,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToDelete ->
                Mono.justOrEmpty((User) userDetailsService.loadUserByUsername(accessToken.getSubject()))
                .flatMap(currentUser -> {
                    if (!ControllerHelper.hasPermission(userToDelete, currentUser)) {
                        return Mono.error(new AccessDeniedException("You don't have the permission to delete this user"));
                    }
                    userInfoService.deleteUser(userToDelete.getAuth0UserId());
                    userService.deleteUser(userToDelete);
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted"));
                })
            )
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}

