package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.FavoriteService;
import com.emmariescurrena.bookesy.user_service.services.UserService;
import com.emmariescurrena.bookesy.user_service.util.ControllerHelper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReactiveUserDetailsService userDetailsService;


    @PostMapping("/{email}")
    public Mono<ResponseEntity<String>> addFavorite(
        @PathVariable String email,
        @RequestParam String bookId,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return ControllerHelper.getUserFromMono(userService.getUserByEmail(email))
        .flatMap(userToAddFavorite ->
            userDetailsService.findByUsername(accessToken.getSubject())
                .flatMap(currentUser -> {
                    if (!ControllerHelper.hasPermission(userToAddFavorite, (User) currentUser)) {
                        return Mono.error(new AccessDeniedException(
                            "You don't have the permission to add a favorite book to this user"));
                    }
                    return favoriteService.addFavorite(userToAddFavorite.getId(), bookId)
                        .thenReturn(ResponseEntity.ok("Favorite book added"));
                })
        );
    }


    @GetMapping("/{email}")
    public Flux<String> getUserFavorites(
        @PathVariable String email
    ) {
        return ControllerHelper.getUserFromMono(userService.getUserByEmail(email))
            .flatMapMany(user -> favoriteService.getUserFavoritesBooks(user.getId()));
    }


    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<String>> removeFavorite(
        @PathVariable String email,
        @RequestParam String bookId,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return ControllerHelper.getUserFromMono(userService.getUserByEmail(email))
        .flatMap(userToRemoveFavorite ->
            userDetailsService.findByUsername(accessToken.getSubject())
                .flatMap(currentUser -> {
                    if (!ControllerHelper.hasPermission(userToRemoveFavorite, (User) currentUser)) {
                        return Mono.error(new AccessDeniedException(
                            "You don't have the permission to remove a favorite book to this user"));
                    }
                    return favoriteService.removeFavorite(userToRemoveFavorite.getId(), bookId)
                        .thenReturn(ResponseEntity.ok("Favorite book removed"));
                })
        );
    }

}
