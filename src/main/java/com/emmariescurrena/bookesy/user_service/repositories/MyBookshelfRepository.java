package com.emmariescurrena.bookesy.user_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.MyBookshelf;


@Repository
public interface MyBookshelfRepository extends JpaRepository<MyBookshelf, Long> {
    Optional<MyBookshelf> findByUserId(Long userId);
}
