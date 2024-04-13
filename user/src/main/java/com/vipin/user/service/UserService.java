package com.vipin.user.service;

import com.vipin.user.dto.UserDto;

public interface UserService {
    void register(UserDto user) throws Exception;
}
