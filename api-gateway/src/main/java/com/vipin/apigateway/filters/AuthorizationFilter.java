package com.vipin.apigateway.filters;

import com.vipin.apigateway.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
  JwtUtil jwtUtil;
    public AuthorizationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            log.info(authorizationHeader,"authHeader");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.info("no auth header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token = authorizationHeader.substring(7);
            System.out.println("userid from jwt token");
            System.out.println(jwtUtil.getUserIdFromToken(token));
            System.out.println("userid from jwt token");
            System.out.printf(token);
            try {
                if (jwtUtil.validateToken(token)) {
                    log.info("token validated");
                    request = exchange
                            .getRequest()
                            .mutate()
                            .header("token", token)
                            .header("role", jwtUtil.getUserRoleFromToken(token))
                            .header("userId",Integer.toString(jwtUtil.getUserIdFromToken(token)))
                            .build();
                } else {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }
    public static class Config{}
}
