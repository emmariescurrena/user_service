package com.emmariescurrena.bookesy.user_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emmariescurrena.bookesy.user_service.models.Role;
import com.emmariescurrena.bookesy.user_service.models.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
