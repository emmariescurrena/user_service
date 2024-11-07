package com.emmariescurrena.bookesy.user_service.util;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.models.UserInfo;
import com.emmariescurrena.bookesy.user_service.services.UserInfoService;
import com.emmariescurrena.bookesy.user_service.services.UserService;

public class ControllerHelper {
    
    @Autowired
    static UserInfoService userInfoService;

    @Autowired
    static UserService userService;
    
    public static User getCurrentUser(String accessToken) {
        UserInfo currentUserInfo = userInfoService.getUserInfo(accessToken);
        User currentUser = userService.getUserByEmail(currentUserInfo.getEmail()).get();
        return currentUser;
    }

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
