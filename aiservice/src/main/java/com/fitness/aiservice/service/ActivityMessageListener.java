package com.fitness.aiservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;  // Injecting ActivityAIService to use its functionality for processing activity data and generating AI recommendations
    private final RecommendationRepository recommendationRepository;  // Injecting RecommendationRepository to save the generated recommendations to the database

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Received activity event for processing: {}", activity.getUserId());
        // Here you can add logic to process the activity data, such as saving it to a database,
        // performing analysis, or triggering other actions based on the activity type or metrics.

        Recommendation recommendation = activityAIService.generateRecommendation(activity);  // Call the method to generate AI recommendations based on the received activity data
        recommendationRepository.save(recommendation);
    }

}
