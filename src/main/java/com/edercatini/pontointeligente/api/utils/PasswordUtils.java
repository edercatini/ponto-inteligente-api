package com.edercatini.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtils.class);

    public PasswordUtils() {
    }

    public static String generateBCrypt(String password) {
        if (password == null) {
            return password;
        }

        logger.info("Generating BCrypt Hash");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}