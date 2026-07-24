package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.UserResponse;
import com.Farm.NASMS.model.User;


public interface AuthService {
UserResponse register(FarmerRegistrationRequest request);
    String login(String emailAddress, String password);
}
