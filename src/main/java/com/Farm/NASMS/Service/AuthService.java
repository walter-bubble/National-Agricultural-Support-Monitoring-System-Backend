package com.Farm.NASMS.Service;

import org.springframework.boot.security.autoconfigure.SecurityProperties;

public interface AuthService {
SecurityProperties.User register(SecurityProperties.User user);
String login(String username,String password);
}
