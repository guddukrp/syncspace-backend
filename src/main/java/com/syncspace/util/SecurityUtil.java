package com.syncspace.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String principalText) {
            try {
                return UUID.fromString(principalText);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        return null;
    }
}
