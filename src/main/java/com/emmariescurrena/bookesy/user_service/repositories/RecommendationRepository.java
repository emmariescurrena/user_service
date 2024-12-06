package com.emmariescurrena.bookesy.user_service.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.Recommendation;

import reactor.core.publisher.Flux;


@Repository
public interface RecommendationRepository extends ReactiveCrudRepository<Recommendation, Long> {
    Flux<Recommendation> findByUserId(Long userId);
}
