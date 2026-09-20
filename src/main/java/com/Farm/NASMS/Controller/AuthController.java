package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AuthService;
import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.LoginRequest;
import com.Farm.NASMS.dto.UserResponse;
import com.Farm.NASMS.model.User;

import com.Farm.NASMS.security.AuthenticatedUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticatedUserService authenticatedUserService;
    public AuthController(AuthService authService,AuthenticatedUserService authenticatedUserService)
    {
        this.authService = authService;
        this.authenticatedUserService = authenticatedUserService;
    }
    //register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody FarmerRegistrationRequest request){
        try{
        UserResponse response=authService.register(request);
        return ResponseEntity.ok(response);
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
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User currentUser = authenticatedUserService.getCurrentUser();
        UserResponse response = new UserResponse();
        response.setId(currentUser.getId());
        response.setUserName(currentUser.getUserName());
        response.setEmailAddress(currentUser.getEmailAddress());
        response.setRole(currentUser.getRole());
        return ResponseEntity.ok(response);
    }
}
