package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AuthService;
import com.Farm.NASMS.Service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;
    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }
    //register
    @PostMapping("/register")
    public ResponseEntity<SecurityProperties.User> registerUser(@RequestBody SecurityProperties.User user){
        SecurityProperties.User savedUser=authServiceImpl.register(user);
        return ResponseEntity.ok(savedUser);
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody SecurityProperties.User user){
        String token= authServiceImpl.login(user.getName(),user.getPassword());
        return ResponseEntity.ok(token);
    }
}
