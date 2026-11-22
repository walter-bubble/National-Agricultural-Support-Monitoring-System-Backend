package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.dto.FarmerRegistrationRequest;
import com.Farm.NASMS.dto.UserResponse;
import com.Farm.NASMS.model.Farmer;
import com.walter.toolkit.security_starter.security.JwtService;
import com.Farm.NASMS.model.User;
import org.springframework.http.HttpStatus;
import com.walter.toolkit.security_starter.audit.Auditable;
import com.walter.toolkit.security_starter.user.CustomUserDetails;
import com.Farm.NASMS.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final FarmerRepository farmerRepository;
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                            JwtService jwtService, AuthenticationManager authenticationManager, FarmerRepository farmerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.farmerRepository = farmerRepository;
    }
    @Override
    @Transactional
    @Auditable(action="FARMER_REGISTERED")
    public UserResponse register(FarmerRegistrationRequest request) {
        //check email
        if (userRepository.findByEmailAddress(request.getEmailAddress()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address is already registered");
        }
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (farmerRepository.existsByNationalId(request.getNationalId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National ID is already registered");
        }
        //check nationalId
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmailAddress(request.getEmailAddress());
        // encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("FARMER");
        user.setEnabled(true);
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
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(emailAddress.trim(), password)
    );
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

    return jwtService.generateAccessToken(userDetails.getUsername(), authorities);
}
}