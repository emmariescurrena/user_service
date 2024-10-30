package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

@Service
public class UserInfoService {

    @Value("${okta.oauth2.client-id}")
    private String clientId;
    
    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;
    
    @Value("${okta.oaoth2.issuer}")
    private static String domain;
        
    private static final String USERINFO_ENDPOINT = domain + "/userinfo";
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    public UserInfo getUserInfo(String accessToken) {
        return webClientBuilder
            .build()
            .get()
            .uri(USERINFO_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(UserInfo.class)
            .block();
    }

    public String getEmail(String accessToken) {
        UserInfo userInfo = getUserInfo(accessToken);
        return userInfo.getEmailAddress();
    }

}

