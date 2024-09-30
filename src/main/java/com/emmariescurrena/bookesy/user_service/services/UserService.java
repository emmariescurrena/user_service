package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

}
