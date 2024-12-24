package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.Favorite;
import com.emmariescurrena.bookesy.user_service.repositories.FavoriteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Transactional
    public Mono<Void> addFavorite(Long userId, String bookId) {
        return favoriteRepository.existsByUserIdAndBookId(userId, bookId)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.empty();
                } else {
                    Favorite favorite = new Favorite(userId, bookId);
                    return favoriteRepository.save(favorite).then();
                }
            });
    }

    public Flux<String> getUserFavoritesBooks(Long userId) {
        return favoriteRepository.findByUserId(userId).map(Favorite::getBookId);
    }

    public Mono<Void> removeFavorite(Long userId, String bookId) {
        return favoriteRepository.findByUserIdAndBookId(userId, bookId)
            .switchIfEmpty(Mono.error(new NotFoundException("Favorite book not found")))
            .flatMap(favorite -> {
                return favoriteRepository.delete(favorite).then();
            });
    }

}
