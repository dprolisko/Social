package com.dprol.social.validator.user;

import com.dprol.social.entity.user.User;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component

public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;

    @Override
    public User ValidateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> {String s = String.format("User with id %s not found", id);
        return new DataValidationException(s);});
    }

    @Override
    public List<User> ValidateUsers(List<Long> ids) {
        return ids.stream().map(this::ValidateUser).collect(Collectors.toList());
    }
}
