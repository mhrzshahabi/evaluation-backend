package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.OAuthCurrentUserDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/oauth")
public class OauthClientController {

    @Autowired
    @Qualifier("oauthToken")
    private RestTemplate restTemplateOAuth;

    @Value("${nicico.oauthBackend}")
    private String oAuthUrl;

    @Loggable
    @GetMapping(value = "/current-user-authorities/{token}")
    public List<String> getAllAuthorities(@PathVariable String token) {

        String url = oAuthUrl + "/tokens/" + token;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            OAuthCurrentUserDTO oAuthCurrentUserDTO = restTemplateOAuth.exchange(url, HttpMethod.GET, entity, OAuthCurrentUserDTO.class).getBody();
            if (oAuthCurrentUserDTO != null)
                return oAuthCurrentUserDTO.getAuthorities();
            else
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم OAuth یافت نشد");
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم OAuth");
        }
    }

}
