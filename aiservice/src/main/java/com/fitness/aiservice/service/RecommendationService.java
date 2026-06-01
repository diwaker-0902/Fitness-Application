package com.fitness.aiservice.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.fitness.aiservice.model.Recommendation;

import com.fitness.aiservice.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor    // Lombok annotation to generate a constructor with required arguments (final fields)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    // To fetch user-specific recommendations based on their activity history and preferences
    public List<Recommendation> getUserRecommendation(String userId) {
        // Placeholder logic to fetch recommendations for a user
        return recommendationRepository.findByUserId(userId);
    }

    // To fetch activity-specific recommendations based on the type of activity and user feedback
    public Optional<Recommendation> getActivityRecommendation(String activityId) {
        // Placeholder logic to fetch recommendation for an activity
        return recommendationRepository.findByActivityId(activityId);
    }

}
