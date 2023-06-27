package com.nicico.evaluation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableAsync
@Slf4j
public class HRMTokenConfig {

    @Value("${spring.security.oauth2.client.provider.oserver.token-uri}")
    private String accessTokenUri;

    private String clientId = "HRM";

    @Value("${nicico.hrm-password}")
    private String clientSecret;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${nicico.security.sys-password}")
    private String sysPassword;

    @Bean("hrmToken")
    public OAuth2RestTemplate reInitialize() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setGrantType("password");
        resourceDetails.setClientId(this.clientId);
        resourceDetails.setClientSecret(this.clientSecret);
        resourceDetails.setUsername("sys_" + appName);
        resourceDetails.setPassword(this.sysPassword);
        resourceDetails.setAccessTokenUri(this.accessTokenUri);
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        log.info("## Reinitialize OAuth2RestTemplate. for hrm token: token=[{}]", restTemplate.getAccessToken().getValue().substring(0, 10));
        return restTemplate;
    }
}
