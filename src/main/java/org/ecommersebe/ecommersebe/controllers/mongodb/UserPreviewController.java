package org.ecommersebe.ecommersebe.controllers.mongodb;

import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.mongodb.UserPreview;
import org.ecommersebe.ecommersebe.services.mongodb.UserPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserPreviewController {
    private final UserPreviewService userPreviewService;

    @MessageMapping("/user.addUser")
    @SendTo("/topic/public")
    public UserPreview addUser(@Payload UserPreview userPreview) {
        userPreviewService.saveUser(userPreview);
        return userPreview;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/topic/public")
    public UserPreview disconnectUser(@Payload UserPreview user) {
        userPreviewService.disconnect(user);
        return user;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserPreview>> findConnectedUsers() {
        return ResponseEntity.ok(userPreviewService.findConnectedUsers());
    }
}
