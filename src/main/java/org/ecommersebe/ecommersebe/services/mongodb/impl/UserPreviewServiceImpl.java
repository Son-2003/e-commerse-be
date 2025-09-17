package org.ecommersebe.ecommersebe.services.mongodb.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.mongodb.UserPreview;
import org.ecommersebe.ecommersebe.repositories.mongodb.UserPreviewRepository;
import org.ecommersebe.ecommersebe.services.mongodb.UserPreviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPreviewServiceImpl implements UserPreviewService {
    private final UserPreviewRepository userPreviewRepository;

    @Override
    public UserPreview saveUser(UserPreview u) {
        return userPreviewRepository.findByUserId(u.getUserId())
                .map(ex -> {
                    ex.setConnected(true);
                    ex.setDisplayName(u.getDisplayName());
                    ex.setAvatar(u.getAvatar());
                    return userPreviewRepository.save(ex);
                })
                .orElseGet(() -> {
                    u.setConnected(true);
                    return userPreviewRepository.save(u);
                });
    }

    @Override
    public UserPreview disconnect(UserPreview u) {
        return userPreviewRepository.findByUserId(u.getUserId())
                .map(ex -> {
                    ex.setConnected(false);
                    return userPreviewRepository.save(ex);
                }).orElse(u);
    }

    @Override
    public List<UserPreview> findConnectedUsers() {
        return userPreviewRepository.findByConnectedTrue();
    }
}
