package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.User;


public interface AuthService {
User register(User user);
    String login(String emailAddress, String password);
}
