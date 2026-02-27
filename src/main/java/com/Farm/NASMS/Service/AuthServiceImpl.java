package com.Farm.NASMS.Service;

import org.springframework.boot.security.autoconfigure.SecurityProperties;

public class AuthServiceImpl implements AuthService {
    @Override
    public SecurityProperties.User register(SecurityProperties.User user) {
        return null;
    }

    @Override
    public String login(String username, String password) {
        return "";
    }
}
