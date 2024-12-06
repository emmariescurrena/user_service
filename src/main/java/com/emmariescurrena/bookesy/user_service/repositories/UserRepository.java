package com.emmariescurrena.bookesy.user_service.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.User;

import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByEmail(String email);
    Mono<User> findByAuth0UserId(String auth0UserId);
    Mono<Boolean> existsByEmail(String email);
}
