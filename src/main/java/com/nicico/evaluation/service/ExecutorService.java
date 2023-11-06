package com.nicico.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class ExecutorService {

    @Autowired
    @Qualifier("threadPoolAsync")
    private Executor asyncExecutor;

    public void runAsync(Callable method) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Locale locale = LocaleContextHolder.getLocale();
        asyncExecutor.execute(() -> {
            try {
                SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                ctx.setAuthentication(authentication);
                SecurityContextHolder.setContext(ctx);
                LocaleContextHolder.setLocale(locale, true);
                method.call();
            } catch (Exception e) {
            } finally {
                SecurityContextHolder.clearContext();
            }
        });
    }
}
