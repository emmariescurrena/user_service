package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.repositories.FavoriteRepository;

@Service
public class FavoriteService {
    
    @Autowired
    FavoriteRepository favoriteRepository;

    

}
