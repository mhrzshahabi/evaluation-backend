package com.nicico.evaluation.common;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.nicico.copper.core.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import java.util.*;

@Service
@Slf4j
public class PermissionService {

    public Set<String> getAuthorities() {
        return SecurityUtil.getAuthorities();
    }


    public String getFullName() {

     return   SecurityUtil.getFullName();

    }

    public Long getUserId()
    {

       return  SecurityUtil.getUserId();


    }
    public String getToken()
    {

    return   ((OAuth2AuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();


    }
}
