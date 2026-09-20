package com.Farm.NASMS.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.model.User;
import com.Farm.NASMS.security.CustomUserDetails;
@Service
public class CustomuserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomuserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmailAddress(emailAddress)
        .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + emailAddress));
        return new CustomUserDetails(user);
    
    }

}
