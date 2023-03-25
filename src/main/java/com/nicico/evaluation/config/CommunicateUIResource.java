package com.nicico.evaluation.config;

import com.nicico.copper.common.Loggable;

import com.nicico.evaluation.common.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.net.URISyntaxException;

@RestController
@Api(value = "communicate with Ui App on the start the project")
public class CommunicateUIResource {

    private final Logger log = LoggerFactory.getLogger(CommunicateUIResource.class);

    PermissionService permissionService;
     public CommunicateUIResource( PermissionService permissionService){
    this.permissionService=permissionService;
 }

    @Value("${ui.redirect.address}")
    private String uiRedirectAddress;
    @Value("${ui.landing.address}")
    private String uiLandingAddress;

    @Loggable
    @RequestMapping("/")
    @ApiOperation(value = "redirect to the Home page of application")
    public ResponseEntity<Void> redirectToHomePage(@RegisteredOAuth2AuthorizedClient("oserver") OAuth2AuthorizedClient authorizedClient, HttpServletResponse httpServletResponse) throws URISyntaxException {
       String fullName=   permissionService.getFullName();
        long userId= permissionService.getUserId();

        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, authorizedClient.getAccessToken().getTokenValue());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, uiRedirectAddress + "?token= Bearer " +
               authorizedClient.getAccessToken().getTokenValue() + "&" + "landingAddress=" + uiLandingAddress +"&" + "userId="+ userId+"&"+"fullName="+fullName);
        return new ResponseEntity<>(HttpStatus.MOVED_PERMANENTLY);
    }


}
