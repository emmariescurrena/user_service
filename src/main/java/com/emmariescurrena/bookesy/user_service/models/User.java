package com.emmariescurrena.bookesy.user_service.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.emmariescurrena.bookesy.user_service.util.RegexValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table("users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("auth0user_id")
    @NotEmpty(message = "The auth0UserId is required")
    private String auth0UserId;

    @Column("email")
    @NotEmpty(message = "The email is required")
    @Size(max = 320, message = "The max length of email must be 320 characters")
    @Email(regexp = RegexValidator.EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email format")
    private String email;

    @Size(min = 2, max = 100, message = "The length of nickname must be between 2 and 100 characters")
    private String nickname;

    @Column("creation_date")
    @CreatedDate
    private LocalDateTime creationDate;

    @Size(max = 1000, message = "The maximum length of bio is 1000 characters")
    private String bio;

    @Column("role")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private RoleEnum role = RoleEnum.USER;

    @Transient
    private String username;

    @Transient
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
        return getAuth0UserId();
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
