package com.dprol.social.validator.user;

import com.dprol.social.entity.user.User;

import java.util.List;

public interface UserValidator {
    User ValidateUser (Long id);

    List<User> ValidateUsers (List<Long> ids);
}
