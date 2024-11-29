package com.emmariescurrena.bookesy.user_service.util;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.User;

import reactor.core.publisher.Mono;


public class ControllerHelper {

    public static Boolean hasPermission(User userToModify, User currentUser) {
        if (!userToModify.getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().equals(List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))) {
            return false;
        }
        return true;
    }

    public static Mono<User> getUserFromMono(Mono<User> userMono) {
        return userMono.switchIfEmpty(
            Mono.error(new NotFoundException("User not found"))
        );
    }

}
