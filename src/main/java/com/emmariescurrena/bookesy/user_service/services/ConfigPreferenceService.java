package com.emmariescurrena.bookesy.user_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmariescurrena.bookesy.user_service.enums.ConfigPreferenceEnum;
import com.emmariescurrena.bookesy.user_service.models.ConfigPreference;
import com.emmariescurrena.bookesy.user_service.repositories.ConfigPreferenceRepository;

@Service
public class ConfigPreferenceService {
    
    @Autowired
    ConfigPreferenceRepository configPreferenceRepository;

    @Autowired
    UserService userService;
    
    public List<ConfigPreference> getConfigPreferences(Long userId) {
        return configPreferenceRepository.findByUserId(userId);
    }

    @Transactional
    public ConfigPreference upsertConfigPreference(Long userId, ConfigPreferenceEnum preferenceName, String value) {
        ConfigPreference configPreference = configPreferenceRepository
                .findByUserIdAndName(userId, preferenceName)
                .orElse(null);
                
        if (configPreference == null) {
            configPreference = new ConfigPreference();
            configPreference.setUser(userService.getUserById(userId).get());
            configPreference.setName(preferenceName);
        }

        configPreference.setValue(value);
        return configPreferenceRepository.save(configPreference);
    }


}
