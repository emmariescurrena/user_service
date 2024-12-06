package com.emmariescurrena.bookesy.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmariescurrena.bookesy.user_service.dtos.UpsertConfigPreferenceDto;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.ConfigPreferenceService;
import com.emmariescurrena.bookesy.user_service.services.UserService;
import com.emmariescurrena.bookesy.user_service.util.ControllerHelper;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/api/users/config-preferences")
public class ConfigPreferenceController {
    
    @Autowired
    UserService userService;

    @Autowired
    ConfigPreferenceService configPreferenceService;

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @GetMapping("/{email}")
    public Flux<ConfigPreference> getConfigPreferences(
        @PathVariable String email,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return ControllerHelper.getUserFromMono(userService.getUserByEmail(email))
        .flatMapMany(userToGetPreferences -> {
            return userDetailsService.findByUsername(accessToken.getSubject())
            .flatMapMany(currentUser -> {
                if (!ControllerHelper.hasPermission(userToGetPreferences, (User) currentUser)) {
                    return Flux.error(new AccessDeniedException(
                        "You don't have the permission to get this user preferences"));
                }
                return configPreferenceService.getConfigPreferences(userToGetPreferences.getId());
            });
        });
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigPreference> upsertConfigPreferences(
        @PathVariable String email,
        @Valid @RequestBody UpsertConfigPreferenceDto preferenceDto,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        return ControllerHelper.getUserFromMono(userService.getUserByEmail(email))
        .flatMapMany(userToGetPreferences -> {
            return userDetailsService.findByUsername(accessToken.getSubject())
            .flatMapMany(currentUser -> {
                if (!ControllerHelper.hasPermission(userToGetPreferences, (User) currentUser)) {
                    return Flux.error(new AccessDeniedException(
                        "You don't have the permission to get this user preferences"));
                }
                return configPreferenceService.upsertConfigPreferences(
                    userToGetPreferences.getId(), preferenceDto);
            });
        });

    }

}
