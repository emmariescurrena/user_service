package com.emmariescurrena.bookesy.user_service.util;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.User;


public class ControllerHelper {

    public static Boolean hasPermission(User userToModify, User currentUser) {
        if (!userToModify.getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().equals(List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))) {
            return false;
        }
        return true;
    }

    public static User getUserFromOptional(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return optionalUser.get();
    }

}
