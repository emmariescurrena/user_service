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
        return Flux.fromIterable(configPreferenceRepository.findByUserId(userId));
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
        return Mono.fromCallable(() -> configPreferenceRepository.findByUserIdAndName(userId, preferenceName).orElse(null))
            .flatMap(configPreference -> {
                if (configPreference == null) {
                    return userService.getUserById(userId)
                        .flatMap(user -> {
                            ConfigPreference newPreference = new ConfigPreference();
                            newPreference.setUser(user);
                            newPreference.setName(preferenceName);
                            newPreference.setValue(value);
                            return Mono.just(newPreference);
                        });
                }
                return Mono.just(configPreference);
            })
            .flatMap(existingOrNewPreference -> {
                existingOrNewPreference.setValue(value);
                return Mono.just(configPreferenceRepository.save(existingOrNewPreference));
            });
    }
    

}
