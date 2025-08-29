package org.ecommersebe.ecommersebe.services.impl;

import org.ecommersebe.ecommersebe.models.entities.User;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.RoleType;
import org.ecommersebe.ecommersebe.models.exception.ECommerseException;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.ecommersebe.ecommersebe.repositories.UserRepository;
import org.ecommersebe.ecommersebe.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, UserRequest, UserResponse> implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        super(userRepository, modelMapper, User.class, UserRequest.class, UserResponse.class);
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponse update(UserRequest request) {
        User existed = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getId()));
        if(existed.getStatus().equals(EntityStatus.ACTIVE)) {
            modelMapper.map(request,existed);
            return modelMapper.map(userRepository.save(existed), UserResponse.class);
        } else throw new ECommerseException(HttpStatus.BAD_REQUEST, "Unable to update information due to its unavailability!");
    }

    @Override
    public UserResponse deleteUser(Long id) {
        User existed = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if(existed.getStatus().equals(EntityStatus.INACTIVE)) {
            existed.setStatus(EntityStatus.DISABLED);
            return modelMapper.map(userRepository.save(existed), UserResponse.class);
        } else throw new ECommerseException(HttpStatus.BAD_REQUEST, "Unable to delete this user due to its unavailability!");
    }

    @Override
    public int getNumberOfActiveUsersInSystem() {
        return userRepository.countUsersByStatusEqualsAndRoleEquals(EntityStatus.ACTIVE, RoleType.CUSTOMER);
    }
}
