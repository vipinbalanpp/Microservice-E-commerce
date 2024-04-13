package com.vipin.user.service;

import com.vipin.user.entity.User;
import com.vipin.user.entity.CustomUserDetails;
import com.vipin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            return new CustomUserDetails(user);
        }catch (Exception e){
            throw new UsernameNotFoundException("user not found with username"+username);
        }

    }
}
