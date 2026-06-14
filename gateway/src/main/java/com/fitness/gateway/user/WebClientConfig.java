package com.fitness.gateway.user;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Creating an instance of WebClient that can be used to call other microservices,
    // such as the UserService, to fetch user details when tracking an activity.

    // LoadBalanced annotation allows WebClient to use Ribbon for client-side load balancing when calling other services registered with Eureka.

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {   
        return WebClient.builder();
    }

    // This method creates a WebClient instance which is pointed to the User Service, 
    // allowing us to call its APIs using the service name (USERSERVICE) instead of a hardcoded URL.
  
    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://USER-SERVICE")
        .build(); // USERSERVICE is the service name registered in Eureka
    }

}

// @LoadBalanced is used with Spring's HTTP clients (such as WebClient.Builder or RestTemplate) when you're using service discovery (for example, with Eureka Server).

// It allows us to call another service by its service name instead of a hardcoded host and port.

// Without @LoadBalanced

// We must use the actual URL of the service, which can lead to issues if the service's location changes (e.g., due to scaling or redeployment).

// Here we create an instance of WebClient that can be used to call other microservices, 
// such as the UserService, 
// to fetch user details when tracking an activity. 
// This allows us to keep our ActivityService decoupled from the UserService 
// while still being able to retrieve necessary information about users when processing activity data.