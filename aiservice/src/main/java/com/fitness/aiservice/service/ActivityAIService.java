package com.fitness.aiservice.service;

import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;  // Injecting GeminiService to use its functionality for getting AI recommendations

    public void generateRecommendations(Activity activity) {
        String prompt = createPromptForActivity(activity);  // Create a prompt based on the activity details
        // Further processing of the prompt can be done here
        log.info("RESPONSE FROM AI {}", geminiService.getRecommendations(prompt));  // Log the response from GeminiService for debugging purposes
    }

    private String createPromptForActivity(Activity activity) {
        // Create a prompt string based on the activity details
        return String.format("""
                Analyze this fitness activity and provide detailedrecommendations in the following EXACT JSON format:
                {
                    "analysis" : {
                    "overall" : "Overall analysis here",
                    "pace" : "Pace analysis here",
                    "heartRate" : "Heart rate analysis here",
                    "caloriesBurned" : "Calories burned analysis here", 
                    },
                    "improvements" : [{
                    "area" : "Area name",
                    "recommendation" : "Detailed recommendation for improvement in this area"
                    }],
                    "suggestions" : [{
                    "workout" : "workout name",
                    "description" : "Detailed description of the workout and how it can help improve the activity"
                    }],
                    "safety" : [
                        "Safety point 1",
                        "Safety point 2"
                    ]
                }

                Analyze this activity:
                Activity Type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s

                Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety tips.
                Ensure the response follow exact same JSON format shown above.
                """, activity.getType(),
                     activity.getDuration(),
                    activity.getCaloriesBurned(), 
                    activity.getAdditionalMetrics()
                );
    }
}
