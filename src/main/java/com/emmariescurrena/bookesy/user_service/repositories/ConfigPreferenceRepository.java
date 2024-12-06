package com.emmariescurrena.bookesy.user_service.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.enums.ConfigPreferenceEnum;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ConfigPreferenceRepository extends ReactiveCrudRepository<ConfigPreference, Long> {
    Flux<ConfigPreference> findByUserId(Long userId);

    @Query("{ 'userId': ?0, 'name': ?1}")
    Mono<ConfigPreference> findByUserIdAndName(Long userId, ConfigPreferenceEnum name);
}

