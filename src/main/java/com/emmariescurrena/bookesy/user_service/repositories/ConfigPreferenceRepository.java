package com.emmariescurrena.bookesy.user_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreferenceEnum;

@Repository
public interface ConfigPreferenceRepository extends JpaRepository<ConfigPreference, Long> {
    List<ConfigPreference> findByUserId(Long userId);
    Optional<ConfigPreference> findByUserIdAndName(Long userId, ConfigPreferenceEnum name);
}

