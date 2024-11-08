package com.emmariescurrena.bookesy.user_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpsertConfigPreferenceDto {

    @NotNull
    @Pattern(regexp = "enabled|disabled", message = "Notification must be either 'enabled' or 'disabled'")
    private String notification;

    @NotNull
    @Pattern(regexp = "dark|light", message = "Theme must be either 'dark' or 'light'")
    private String theme;

    @NotNull
    @Pattern(regexp = "en|es", message = "Language must be either 'en' or 'es'")
    private String language;

}
