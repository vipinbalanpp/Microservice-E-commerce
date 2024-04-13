package com.vipin.user.service;

import com.vipin.user.entity.User;
import com.vipin.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final String SECRET_KEY="YXNmYXNmYXNmZGZhc2RzZmNhdmNzdg==";
    UserRepository userRepository;
    public JwtService (UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public String generateToken(Authentication authentication){
        Date currentDate = new Date();
        User user = userRepository.findByUsername(authentication.getName());
        System.out.println(authentication.getName());
        System.out.println(user.getId());
        System.out.println(user.getId().getClass());
        Date expirationTime = new Date(currentDate.getTime() +   30 * 24 * 60 * 60 * 1000L);
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", roles.get(0))
                .claim("userId",user.getId())
                .setIssuedAt(currentDate)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return  claims.getSubject();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Jwt token is expired or is invalid");
        }

    }
    public void expireToken(String token){
        Date currentDate = new Date();
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        claims.setExpiration(new Date(currentDate.getTime()));
    }
    public String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
