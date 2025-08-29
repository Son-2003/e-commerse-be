package org.ecommersebe.ecommersebe.services;

import org.ecommersebe.ecommersebe.models.payload.dto.user.UserRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.springframework.data.domain.Page;



public interface UserService extends BaseService<UserRequest, UserResponse>{
    Page<UserResponse> search(int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
    UserResponse get(Long id);
    UserResponse update(UserRequest request);
    UserResponse deleteUser(Long id);
    int getNumberOfActiveUsersInSystem();
}
