package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.UpsertMyBookshelfBookDto;
import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.services.AuthorizationService;
import com.emmariescurrena.bookesy.user_service.services.MyBookshelfService;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




@RestController
@RequestMapping("/api/users/my-bookshelf")
public class MyBookshelfController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private MyBookshelfService myBookshelfService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/{email}")
    public Flux<String> getMyBookshelfBooks(
        @PathVariable String email,
        @AuthenticationPrincipal Jwt accessToken)
    {
        return userService.getUserByEmail(email)
            .flatMapMany(userToGetBookshelf -> {
                authorizationService.hasPermission(userToGetBookshelf, accessToken.getSubject());
                return myBookshelfService.getUserBookshelfBooks(userToGetBookshelf.getId());
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> upsertBook(
        @PathVariable String email,
        @Valid @RequestBody UpsertMyBookshelfBookDto bookDto,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToAddBook -> {
                authorizationService.hasPermission(userToAddBook, accessToken.getSubject());
                return myBookshelfService.upsertBook(
                    userToAddBook.getId(), bookDto).then();
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<String>> removeBook(
        @PathVariable String email,
        @RequestParam String bookId,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMap(userToRemoveBook -> {
                authorizationService.hasPermission(userToRemoveBook, accessToken.getSubject());
                return myBookshelfService.removeBook(userToRemoveBook.getId(), bookId)
                    .thenReturn(ResponseEntity.ok("Book removed from shelf"));
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

}
