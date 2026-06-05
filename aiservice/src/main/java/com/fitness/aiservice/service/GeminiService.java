package com.fitness.aiservice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    private final WebClient webClient = WebClient.create();

    // private final WebClient webClient;  // using WebClient for making HTTP requests to Gemini API and handling responses

    @Value("${gemini.api.url}")
    private String geminiApiUrl;  // Base URL for Gemini API

    @Value("${gemini.api.key}")
    private String geminiApiKey;  // API key for authenticating with Gemini API

    // public GeminiService(WebClient.Builder webClientBuilder) {
    //     this.webClient = webClientBuilder.build();
    //     // Initialize geminiApiUrl and geminiApiKey from application properties or environment variables
    // }

    public String getRecommendations(String details) {
        Map<String, Object> requestBody = Map.of(
            "contents", new Object[] {
                Map.of("parts", new Object[] {
                    Map.of("text", details)
                    }
                )
            }
        );
       
        String response = webClient.post()
            .uri(geminiApiUrl)
            .header("Content-Type", "application/json")
            .header("X-goog-api-key", geminiApiKey)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return response;  // Return the response from Gemini API as a string
    }

}
