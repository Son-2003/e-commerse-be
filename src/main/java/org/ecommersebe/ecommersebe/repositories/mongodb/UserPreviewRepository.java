package org.ecommersebe.ecommersebe.repositories.mongodb;

import org.ecommersebe.ecommersebe.models.mongodb.UserPreview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserPreviewRepository extends MongoRepository<UserPreview, String> {
    Optional<UserPreview> findByUserId(String userId);
    List<UserPreview> findByConnectedTrue();
}
