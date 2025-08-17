package org.ecommersebe.ecommersebe.utils;

import lombok.experimental.UtilityClass;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;

@UtilityClass
public class SecurityUtils {

    public static boolean isAuthorizeLocation(Long id, UserRepository userRepository) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        org.ecommersebe.ecommersebe.models.entities.User user = userRepository.findByEmailOrPhone(userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        return user.getOrders().stream().anyMatch(order -> order.getId().equals(id));
    }
}
