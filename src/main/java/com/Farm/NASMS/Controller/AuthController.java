package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AuthService;
import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.LoginRequest;
import com.Farm.NASMS.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody FarmerRegistrationRequest request){
        try{
        User savedUser=authService.register(request);
        return ResponseEntity.ok(savedUser);
        }
    catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest LgRequest){
        try {
            String token = authService.login(LgRequest.getEmailAddress(), LgRequest.getPassword());
            return ResponseEntity.ok(token);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
