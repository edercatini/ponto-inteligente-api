package com.edercatini.pontointeligente.api.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PasswordUtilsTest {

    private static final String PASSWORD = "123456";
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void nullPassword() throws Exception {
        assertNull(PasswordUtils.generateBCrypt(null));
    }

    @Test
    public void passwordMatches() {
        String hash = PasswordUtils.generateBCrypt(PASSWORD);
        assertTrue(encoder.matches(PASSWORD, hash));
    }
}