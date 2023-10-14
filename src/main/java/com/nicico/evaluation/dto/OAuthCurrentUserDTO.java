package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class OAuthCurrentUserDTO {
    private String token;
    private principalInfo principal;
    private List<String> authorities;

    @Getter
    @Setter
    public static class principalInfo {
        private String username;
        private String nationalCode;
    }
}
