package com.vipin.apigateway.config;

import com.vipin.apigateway.filters.AuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, AuthorizationFilter authorizationFilter) {
        return builder.routes()
                .route("user-identity-route", p ->
                        p.path("/auth/**")
                                .uri("http://localhost:8083"))
                .route("product-service-route", p -> p
                        .path("/product/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                        .uri("http://localhost:8082"))
                .route("cart-service-route", p -> p
                        .path("/cart/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                        .uri("http://localhost:8081"))
                .build();
    }
}