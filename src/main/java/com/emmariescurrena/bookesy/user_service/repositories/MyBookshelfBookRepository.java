package com.emmariescurrena.bookesy.user_service.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.MyBookshelfBook;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface MyBookshelfBookRepository extends ReactiveCrudRepository<MyBookshelfBook, Long> {
    Flux<MyBookshelfBook> findByUserId(Long userId);
    Mono<MyBookshelfBook> findByUserIdAndBookId(Long userId, String bookId);
    Mono<Boolean> existsByUserIdAndBookId(Long userId, String bookId);
}
