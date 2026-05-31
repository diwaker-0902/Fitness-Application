package com.fitness.activityservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        // Call the user service to validate the user id
        try {
            return userServiceWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        } catch (WebClientResponseException e) {
            // Log the exception and return false
            // log.error("Error validating user id: {}", userId, e);
            e.printStackTrace();
        }

        return false;
    }
}


/*
This class work is to consume the user service to validate the user id and get the user details. 
It will be used in the activity service to validate the user id before creating an activity. 
It will also be used to get the user details to create the activity with the user details. 
*/