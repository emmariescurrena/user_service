package com.emmariescurrena.bookesy.user_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.UserGenrePreference;

@Repository
public interface UserGenrePreferenceRepository extends JpaRepository<UserGenrePreference, Long>{
    List<UserGenrePreference> findByUser_Id(Long userId);
}
