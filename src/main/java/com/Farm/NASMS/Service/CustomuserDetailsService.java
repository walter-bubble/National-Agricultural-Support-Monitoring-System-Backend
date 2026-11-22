package com.Farm.NASMS.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.walter.toolkit.security_starter.user.CustomUserDetails;

import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.model.User;

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
