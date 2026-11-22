package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AuthService;
import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.LoginRequest;
import com.Farm.NASMS.dto.UserResponse;
import com.Farm.NASMS.model.Farmer;
import org.springframework.http.HttpStatus;
import com.walter.toolkit.security_starter.dto.AuthResponse;
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
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody FarmerRegistrationRequest request){
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest lgRequest) { // <--- CHANGED (added @Valid & AuthResponse)
        String token = authService.login(lgRequest.getEmailAddress(), lgRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer")); // <--- CHANGED
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User currentUser = authenticatedUserService.getCurrentUser();
        UserResponse response = new UserResponse();
        response.setId(currentUser.getId());
        response.setUserName(currentUser.getUserName());
        response.setEmailAddress(currentUser.getEmailAddress());
        response.setRole(currentUser.getRole());
        if ("FARMER".equalsIgnoreCase(currentUser.getRole())) {
            Farmer farmer = authenticatedUserService.getCurrentFarmer();
            response.setFarmerId(farmer.getId());
            response.setNationalId(farmer.getNationalId());
        }
        return ResponseEntity.ok(response);
    }
}
