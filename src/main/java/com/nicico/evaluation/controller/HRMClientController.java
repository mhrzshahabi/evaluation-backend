package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.HrmPersonDTO;
import com.nicico.evaluation.dto.OAuthCurrentUserDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/hrm")
public class HRMClientController {

    @Autowired
    @Qualifier("hrmToken")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("oauthToken")
    private RestTemplate restTemplateOAuth;

    @Value("${nicico.hrmBackend}")
    private String hrmUrl;

    @Value("${nicico.oauthBackend}")
    private String oAuthUrl;

    @Loggable
    @GetMapping(value = "/image-profile/{nationalCode}")
    public ResponseEntity<HrmPersonDTO> getProfileImage(@PathVariable String nationalCode) {

        String url = hrmUrl + "/persons/profile/" + nationalCode;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            HrmPersonDTO hrmPersonDTO = restTemplate.exchange(url, HttpMethod.GET, entity, HrmPersonDTO.class).getBody();
            if (hrmPersonDTO != null)
                return new ResponseEntity<>(hrmPersonDTO, HttpStatus.OK);
            else
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم منابع انسانی یافت نشد");
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم منابع انسانی");
        }
    }

    @Loggable
    @GetMapping(value = "/current-user-name/{token}")
    public String getUserName(@PathVariable String token) {

        String url = oAuthUrl + "/tokens/" + token;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            OAuthCurrentUserDTO oAuthCurrentUserDTO = restTemplateOAuth.exchange(url, HttpMethod.GET, entity, OAuthCurrentUserDTO.class).getBody();
            if (oAuthCurrentUserDTO != null)
                return oAuthCurrentUserDTO.getPrincipal().getUsername();
            else
                throw new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound, "شخص مورد نظر در سیستم منابع انسانی یافت نشد");
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم منابع انسانی");
        }
    }

}
