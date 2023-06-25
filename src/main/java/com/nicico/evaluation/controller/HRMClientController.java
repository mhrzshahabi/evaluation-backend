package com.nicico.evaluation.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.evaluation.dto.HrmPersonDTO;
import com.nicico.evaluation.exception.EvaluationHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final RestTemplate restTemplate;

    @Value("${nicico.hrmBackend}")
    private String hrmUrl;

    @Loggable
    @GetMapping(value = "/image-profile/{nationalCode}")
    public ResponseEntity<String> getProfileImage(@PathVariable String nationalCode) {

        String url = hrmUrl + "/persons/profile/" + nationalCode;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, null);
        try {
            HrmPersonDTO hrmPersonDTO = restTemplate.exchange(url, HttpMethod.GET, entity, HrmPersonDTO.class).getBody();
            return new ResponseEntity<>(hrmPersonDTO.getImageProfile(), HttpStatus.OK);
        } catch (Exception e) {
            throw new EvaluationHandleException(EvaluationHandleException.ErrorType.Unauthorized, "خطا در دسترسی به سیستم منابع انسانی");
        }
    }

}
