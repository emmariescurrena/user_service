package com.emmariescurrena.bookesy.user_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreferencesEnum;

@Repository
public interface ConfigPreferenceRepository extends JpaRepository<ConfigPreference, Long> {
    Optional<ConfigPreference> findByName(ConfigPreferencesEnum name);
}

