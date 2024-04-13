package com.vipin.user.controller;
import com.vipin.user.dto.UserDto;
import com.vipin.user.service.JwtService;
import com.vipin.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class Controller {

    UserService userService;

    AuthenticationManager authenticationManager;
    JwtService jwtService;
    public Controller(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user){
             try {
                 userService.register(user);
             }catch (Exception e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
             }
             return new ResponseEntity<>("user registered successfully",HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody UserDto userDto){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
            String token = jwtService.generateToken(authentication);
            return new ResponseEntity<>(token,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
