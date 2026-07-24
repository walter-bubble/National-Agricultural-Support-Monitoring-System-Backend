package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.UserResponse;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.security.JwtUtil;
import com.Farm.NASMS.model.User;
import com.Farm.NASMS.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FarmerRepository farmerRepository;
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,FarmerRepository farmerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.farmerRepository = farmerRepository;
    }
    @Override
    @Transactional
    public UserResponse register(FarmerRegistrationRequest request) {
        //check email
        if(userRepository.findByEmailAddress(request.getEmailAddress()).isPresent()){
             throw new RuntimeException("user exists");
        }
        if(userRepository.findByUserName(request.getName()).isPresent()){
            throw new RuntimeException("username exists");
        }
        //check nationalId
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmailAddress(request.getEmailAddress());
        // encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("FARMER");
        // save
    User savedUser = userRepository.save(user);

        Farmer farmer = new Farmer();
        farmer.setPhoneNumber(request.getPhoneNumber());
        farmer.setCounty(request.getCounty());
        farmer.setFarmSize(request.getFarmSize());
        farmer.setTitleNumber(request.getTitleNumber());
        farmer.setFarmSize(request.getFarmSize());
        farmer.setNationalId(request.getNationalId());
        farmer.setName(request.getName());
        farmer.setEmail(request.getEmailAddress());
        farmer.setUser(savedUser);
        farmerRepository.save(farmer);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUserName(savedUser.getUserName());
        response.setRole(savedUser.getRole());
        response.setEmailAddress(savedUser.getEmailAddress());

        return response;

    }
@Override
    public String login(String emailAddress, String password) {
        User user = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(()->new RuntimeException("user not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(emailAddress);
    }
}