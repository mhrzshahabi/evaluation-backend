package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OAuthCurrentUserDTO {
    private String token;
    private principalInfo principal;

    @Getter
    @Setter
    public static class principalInfo {
        private String username;
        private String nationalCode;
    }
}
