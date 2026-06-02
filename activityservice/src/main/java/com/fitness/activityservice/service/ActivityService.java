package com.fitness.activityservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fitness.activityservice.ActivityRepository;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {

      boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if (!isValidUser) {
          throw new RuntimeException("Invalid user: " + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())    // here userid will be consume from USERSERVICE and will be passed in request body. USERSERVICE will expose an API 
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        // Publish activity event to Kafka
        try {
            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
          
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId()); 
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }


}

/*
We're using WebClient for inter-service communication, 
then the Activity Service can call the User Service to retrieve the correct user ID and 
update its own database.
*/

/*
User Service and Activity Service are two separate microservices. User Service manages user profiles, while Activity Service tracks fitness activities.
User Service has the correct user id
Activity service (wothout validation) can accept any user id and create activity records, which can lead to data integrity issues.

To ensure data integrity, we need to implement inter-service communication between Activity Service and User Service. 
When Activity Service receives a request to track an activity, it should first validate the user id by calling User Service's API 
(using RestTemplate(old approach) or WebClient(new approach we are using)) to check if the user exists. 
If the user is valid, Activity Service can proceed to create the activity record; otherwise, it should return an error response indicating that the user is not found.

User Service exposes APIs for user registration and profile retrieval, while Activity Service exposes APIs for tracking activities.
We need inter-service communication so that Activity Service can get the correct user information
 from User Service and update its records.
 */