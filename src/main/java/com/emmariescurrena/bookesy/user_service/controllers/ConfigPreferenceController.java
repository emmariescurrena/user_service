package com.emmariescurrena.bookesy.user_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
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



@RestController
@RequestMapping("/api/users/config-preferences")
public class ConfigPreferenceController {
    
    @Autowired
    UserService userService;

    @Autowired
    ConfigPreferenceService configPreferenceService;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/{email}")
    public List<ConfigPreference> getConfigPreferences(
        @PathVariable String email,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        User userToGetPreferences = ControllerHelper.getUserFromOptional(userService.getUserByEmail(email));

        User currentUser = (User) userDetailsService.loadUserByUsername(accessToken.getSubject());

        if (!ControllerHelper.hasPermission(userToGetPreferences, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to update this user preferences");
        }

        return configPreferenceService.getConfigPreferences(userToGetPreferences.getId());
    }
    

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigPreference> upsertConfigPreferences(
        @PathVariable String email,
        @Valid @RequestBody UpsertConfigPreferenceDto preferenceDto,
        @AuthenticationPrincipal Jwt accessToken
    ) {
        
        User userToUpdate = ControllerHelper.getUserFromOptional(userService.getUserByEmail(email));

        User currentUser = (User) userDetailsService.loadUserByUsername(accessToken.getSubject());

        if (!ControllerHelper.hasPermission(userToUpdate, currentUser)) {
            throw new AccessDeniedException("You don't have the permission to update this user preferences");
        }

        return configPreferenceService.upsertConfigPreferences(userToUpdate.getId(), preferenceDto);
    }

}
