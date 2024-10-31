package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.emmariescurrena.bookesy.user_service.models.UserInfo;


@Service
public class UserInfoService {

    @Value("${okta.oauth2.client-id}")
    private String clientId;
    
    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;
    
    @Value("${okta.oauth2.issuer}")
    private String domain;
        
    @Autowired
    private WebClient.Builder webClientBuilder;

    public UserInfo getUserInfo(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        
        UserInfo userInfo = webClient
            .get()
            .uri(domain + "userinfo")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .retrieve()
            .bodyToMono(UserInfo.class)
            .block();

        return userInfo;
    }

    public String getEmail(String accessToken) {
        UserInfo userInfo = getUserInfo(accessToken);
        return userInfo.getEmail();
    }

}

