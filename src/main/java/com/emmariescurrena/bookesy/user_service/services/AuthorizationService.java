package com.emmariescurrena.bookesy.user_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.models.User;

import reactor.core.publisher.Mono;

@Service
public class AuthorizationService {

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    public Mono<Void> hasPermission(User targetUser, String currentUserUsername) {
        return userDetailsService.findByUsername(currentUserUsername)
            .flatMap(currentUserDetails -> {
                User currentUser = (User) currentUserDetails;
                if (!targetUser.getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().equals(List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))) {
                    return Mono.error(new AccessDeniedException(
                        "You don't have the permission to modify this user"));
                }
                return Mono.empty();
            });
    }

}
