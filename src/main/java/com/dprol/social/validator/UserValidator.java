package com.dprol.social.validator;

import com.dprol.social.entity.User;

import java.util.List;

public interface UserValidator {
    User ValidateUser (Long id);

    List<User> ValidateUsers (List<Long> ids);
}
