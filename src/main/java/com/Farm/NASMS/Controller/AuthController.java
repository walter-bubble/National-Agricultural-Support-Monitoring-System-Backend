package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AuthService;
import com.Farm.NASMS.Service.AuthServiceImpl;
import com.Farm.NASMS.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }
    //register
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        User savedUser=authService.register(user);
        return ResponseEntity.ok(savedUser);
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        String token= authService.login(user.getUserName(),user.getPassword());
        return ResponseEntity.ok(token);
    }
}
