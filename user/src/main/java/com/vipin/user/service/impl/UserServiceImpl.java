package com.vipin.user.service.impl;

import com.vipin.user.dto.UserDto;
import com.vipin.user.entity.Roles;
import com.vipin.user.entity.User;
import com.vipin.user.repository.UserRepository;
import com.vipin.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RabbitTemplate rabbitTemplate){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    @Transactional
    public void register(UserDto userDto) throws Exception {
        if(userRepository.existsByUsername(userDto.getUsername())){
            log.warn("username exists");
            throw new Exception("Username exists");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            log.warn("email exists");
            throw new Exception("email exists");
        }
        User user = new User();
        if(userRepository.count()<1){
            user.setRole(Roles.ADMIN);
        }else {
            user.setRole(Roles.USER);
        }
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        System.out.println(user.getId());
        userDto.setId(user.getId());
        rabbitTemplate.convertAndSend("user-queue",user.getId());
    }
}
