package com.nicico.evaluation.config;

import com.nicico.copper.common.Loggable;

import com.nicico.copper.core.SecurityUtil;
import com.nicico.copper.oauth.common.domain.CustomUserDetails;
import com.nicico.evaluation.common.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Api(value = "communicate with Ui App on the start the project")
public class CommunicateUIResource {

    private final Logger log = LoggerFactory.getLogger(CommunicateUIResource.class);
    private final OAuth2AuthorizedClientService clientService;
    @Value("${ui.redirect.address}")
    private String uiRedirectAddress;
    @Value("${ui.landing.address}")
    private String uiLandingAddress;

    @Loggable
    @RequestMapping("/")
    @ApiOperation(value = "redirect to the Home page of application")
    public ResponseEntity<Void> redirectToHomePage(HttpServletResponse httpServletResponse) throws URISyntaxException {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        OAuth2AuthenticationToken oToken = null;
        CustomUserDetails userDetails = null;
        try {
            oToken = (OAuth2AuthenticationToken) authentication;
        } catch (Exception e) {
            userDetails = (CustomUserDetails) authentication.getPrincipal();
        }

        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient(
                        "oserver",
                        oToken != null ? oToken.getName() : Objects.requireNonNull(userDetails).getUsername());

        String accessToken = client.getAccessToken().getTokenValue();
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        httpServletResponse.setHeader(HttpHeaders.LOCATION, uiRedirectAddress +
                "?token= Bearer " + accessToken + "&" +
                "landingAddress=" + uiLandingAddress + "&" +
                "userNationalCode=" + SecurityUtil.getNationalCode());
        return new ResponseEntity<>(HttpStatus.MOVED_PERMANENTLY);
    }

}
