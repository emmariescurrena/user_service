package com.emmariescurrena.bookesy.user_service.repositories;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.Favorite;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FavoriteRepository extends ReactiveCrudRepository<Favorite, Long> {
    Flux<Favorite> findByUserId(Long userId);
    Flux<Favorite> findByBookId(String bookId);
    Mono<Favorite> findByUserIdAndBookId(Long userId, String bookId);
    Mono<Boolean> existsByUserIdAndBookId(Long userId, String bookId);
}
