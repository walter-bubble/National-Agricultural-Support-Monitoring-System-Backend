package com.Farm.NASMS.Service;

import com.Farm.NASMS.User;
import com.Farm.NASMS.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User register(User user) {
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // save
    User savedUser = userRepository.save(user);
        // hide password in response
        savedUser.setPassword(null);
        return savedUser;
    }
    @Override
    public String login(String userName, String password) {
        User user = userRepository.findByUserName(userName).orElseThrow(()->new RuntimeException());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return "Login successful";
    }
}