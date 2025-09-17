package org.ecommersebe.ecommersebe.services.mongodb;

import org.ecommersebe.ecommersebe.models.mongodb.UserPreview;

import java.util.List;

public interface UserPreviewService {
    UserPreview saveUser(UserPreview u);
    UserPreview disconnect(UserPreview u);
    List<UserPreview> findConnectedUsers();
}
