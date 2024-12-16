package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.UpsertConfigPreferenceDto;
import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.services.AuthorizationService;
import com.emmariescurrena.bookesy.user_service.services.ConfigPreferenceService;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/users/config-preferences")
public class ConfigPreferenceController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ConfigPreferenceService configPreferenceService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/{email}")
    public Flux<ConfigPreference> getConfigPreferences(
        @PathVariable String email,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMapMany(userToGetPreferences -> {
                authorizationService.hasPermission(userToGetPreferences, accessToken.getSubject());
                return configPreferenceService.getConfigPreferences(userToGetPreferences.getId());
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigPreference> upsertConfigPreferences(
        @PathVariable String email,
        @Valid @RequestBody UpsertConfigPreferenceDto preferenceDto,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return userService.getUserByEmail(email)
            .flatMapMany(userToGetPreferences -> {
                authorizationService.hasPermission(userToGetPreferences, accessToken.getSubject());
                return configPreferenceService.upsertConfigPreferences(
                    userToGetPreferences.getId(), preferenceDto);
            })
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")));
    }

}
