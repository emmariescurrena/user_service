package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmariescurrena.bookesy.user_service.dtos.UpsertConfigPreferenceDto;
import com.emmariescurrena.bookesy.user_service.enums.ConfigPreferenceEnum;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.repositories.ConfigPreferenceRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ConfigPreferenceService {
    
    @Autowired
    ConfigPreferenceRepository configPreferenceRepository;

    @Autowired
    UserService userService;
    
    public Flux<ConfigPreference> getConfigPreferences(Long userId) {
        return configPreferenceRepository.findByUserId(userId);
    }

    @Transactional
    public Flux<ConfigPreference> upsertConfigPreferences(Long userId, UpsertConfigPreferenceDto dto) {
        
        if (dto.getNotification() != null) {
            upsertSingleConfigPreference(userId, ConfigPreferenceEnum.NOTIFICATION, dto.getNotification());
        }

        if (dto.getTheme() != null) {
            upsertSingleConfigPreference(userId, ConfigPreferenceEnum.THEME, dto.getTheme());
        }

        if (dto.getLanguage() != null) {
            upsertSingleConfigPreference(userId, ConfigPreferenceEnum.LANGUAGE, dto.getLanguage());
        }

        return getConfigPreferences(userId);

    }

    private Mono<ConfigPreference> upsertSingleConfigPreference(Long userId, ConfigPreferenceEnum preferenceName, String value) {
        return configPreferenceRepository.findByUserIdAndName(userId, preferenceName)
            .switchIfEmpty(Mono.defer(() -> {
                ConfigPreference newPreference = new ConfigPreference();
                newPreference.setUserId(userId);
                newPreference.setName(preferenceName);
                return Mono.just(newPreference);
            }))
            .flatMap(configPreference -> {
                configPreference.setValue(value);
                return configPreferenceRepository.save(configPreference);
            });
    }
    

}
