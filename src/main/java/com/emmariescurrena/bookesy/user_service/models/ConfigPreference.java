package com.emmariescurrena.bookesy.user_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.emmariescurrena.bookesy.user_service.enums.ConfigPreferenceEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Table("config_preferences")
public class ConfigPreference {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("name")
    @Enumerated(EnumType.STRING)
    private ConfigPreferenceEnum name;

    @Column("value")
    private String value;

    @JsonIgnore
    @Column("user_id")
    private Long userId;
    
}
