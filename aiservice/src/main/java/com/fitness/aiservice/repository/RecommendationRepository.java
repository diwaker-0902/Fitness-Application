package com.fitness.aiservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.fitness.aiservice.model.Recommendation;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

    // We want to fetch the data using the userId  that's why we are using findByUserId method 
    // Java gives us in-build findById method but we want to fetch the data using the userId 
    // that's why we are using findByUserId method and that's why we have define 
    // also this method here in the repository interface
    // and also we want to fetch the data using the activityId that's why we are using findByActivityId method
    List<Recommendation> findByUserId(String userId);

    // Similarly we have in-build findById method but we want to fetch the data using the activityId
    // So that's why we are using findByActivityId method and that's why we have define also this method here in the repository interface
    Optional<Recommendation> findByActivityId(String activityId);
}
