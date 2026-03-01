package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public SecurityProperties.User register(SecurityProperties.User user) {
        //encode password
        User.setPassword(PasswordEncoder.encode(user.getPassword()));
        //save user in db
        User savedUser= (User) userRepository.save(user);
        savedUser.setPassword(null);
        return userRepository.save(user);
    }
    @Override
    public String login(String username, String password) {
        User user = userRepository.findUserByUserName(username)
        if (!PasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("invalid password");
        }
        return jwtService.generateToken(user);
    }
}
