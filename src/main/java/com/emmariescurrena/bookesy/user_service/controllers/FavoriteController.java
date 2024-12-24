package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.services.AuthorizationService;
import com.emmariescurrena.bookesy.user_service.services.FavoriteService;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/api/users/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;


    @PostMapping("/{email}")
    public Mono<ResponseEntity<String>> addFavorite(
        @PathVariable String email,
        @RequestParam String bookId,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToAddFavorite -> {
                authorizationService.hasPermission(userToAddFavorite, accessToken.getSubject());
                return favoriteService.addFavorite(userToAddFavorite.getId(), bookId)
                        .thenReturn(ResponseEntity.ok("Favorite book added"));
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }


    @GetMapping("/{email}")
    public Flux<String> getUserFavorites(
        @PathVariable String email
    ) {
        return userService.getUserByEmail(email)
            .flatMapMany(user -> favoriteService.getUserFavoritesBooks(user.getId()))
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }


    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<String>> removeFavorite(
        @PathVariable String email,
        @RequestParam String bookId,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToRemoveFavorite -> {
                authorizationService.hasPermission(userToRemoveFavorite, accessToken.getSubject());
                return favoriteService.removeFavorite(userToRemoveFavorite.getId(), bookId)
                    .thenReturn(ResponseEntity.ok("Favorite book removed"));
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

}
