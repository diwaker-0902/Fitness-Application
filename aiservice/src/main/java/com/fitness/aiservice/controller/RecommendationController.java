package com.fitness.aiservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.aiservice.service.RecommendationService;

import com.fitness.aiservice.model.Recommendation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")

public class RecommendationController {

    private final RecommendationService recommendationService;

    // To fetch user-specific recommendations based on their activity history and preferences
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    // To fetch activity-specific recommendations based on the type of activity and user feedback
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId) {
        return recommendationService.getActivityRecommendation(activityId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No recommendation found for activity: " + activityId));
    }

}
