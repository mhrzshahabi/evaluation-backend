package com.nicico.evaluation.common;

import org.springframework.security.access.AccessDeniedException;

public class ExceptionUtil {

    public static void sendAccessDeniedException(String message) {
        throw new AccessDeniedException(message);

    }
}
