package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.emmariescurrena.bookesy.user_service.dtos.UpdateUserDto;
import com.emmariescurrena.bookesy.user_service.models.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@Service
public class UserInfoService {

    @Value("${okta.oauth2.client-id}")
    private String clientId;
    
    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;
    
    @Value("${okta.oauth2.issuer}")
    private String auth0Domain;

    @Value("${okta.oauth2.audience}")
    private String audience;
        
    @Autowired
    private WebClient.Builder webClientBuilder;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static record AuthManagementTokenRequest(
        String clientId,
        String clientSecret,
        String audience,
        String grantType) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static record AuthManagementTokenResponse(
        String accessToken,
        String scope,
        int expiresIn,
        String tokenType) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static record UpdateRequestBody(String nickname) {}

    public UserInfo getUserInfo(String accessToken) {
        String requestUrl = auth0Domain + "userinfo";
        WebClient webClient = webClientBuilder.build();
        
        UserInfo userInfo = webClient
            .get()
            .uri(requestUrl)
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .retrieve()
            .bodyToMono(UserInfo.class)
            .block();

        return userInfo;
    }

    public void updateUser(String userId, UpdateUserDto userDto) {
        String apiToken = "Bearer " + getManagementApiToken();
        String requestUrl = auth0Domain + "api/v2/users/" + userId;
        UpdateRequestBody requestBody = new UpdateRequestBody(userDto.getNickname());

        WebClient webClient = webClientBuilder.build();
        webClient
            .patch()
            .uri(requestUrl)
            .header(HttpHeaders.AUTHORIZATION, apiToken)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public void deleteUser(String userId) {
        String apiToken = "Bearer " + getManagementApiToken();
        String requestUrl = auth0Domain + "api/v2/users/" + userId;
        
        WebClient webClient = webClientBuilder.build();
        webClient
            .delete()
            .uri(requestUrl)
            .header(HttpHeaders.AUTHORIZATION, apiToken)
            .retrieve()
            .toBodilessEntity()
            .block();

    }

    private String getManagementApiToken() {
        String requestUrl = auth0Domain + "oauth/token";
        String grantType = "client_credentials";

        AuthManagementTokenRequest requestBody = new AuthManagementTokenRequest(
            clientId, clientSecret, audience, grantType);

        WebClient webClient = webClientBuilder.build();

        AuthManagementTokenResponse responseBody = webClient
            .post()
            .uri(requestUrl)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(AuthManagementTokenResponse.class)
            .block();

        return responseBody.accessToken();
    }


}

