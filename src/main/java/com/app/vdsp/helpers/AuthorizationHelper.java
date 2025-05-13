package com.app.vdsp.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthorizationHelper {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);

    public static void ensureAuthorizationHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Authorization header is missing or invalid");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is required");
        }

        log.info("Authorization header is valid");
    }
}
