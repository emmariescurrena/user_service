package com.emmariescurrena.bookesy.user_service.models;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.emmariescurrena.bookesy.user_service.util.RegexValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "USERS")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "The auth0UserId is required")
    private String auth0UserId;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "The email is required")
    @Size(max = 320, message = "The max length of email must be 320 characters")
    @Email(regexp = RegexValidator.EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email format")
    private String email;

    @Column(unique = true)
    @Size(min = 2, max = 100, message = "The length of nickname must be between 2 and 100 characters")
    private String nickname;

    @Column(name = "creation_date", updatable = false)
    @CreationTimestamp
    private Date creationDate;

    @Size(max = 1000, message = "The maximum length of bio is 1000 characters")
    private String bio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private RoleEnum role = RoleEnum.USER;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;

    public User setRole(RoleEnum role) {
        this.role = role;

        return this;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toString());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
