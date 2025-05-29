package com.dprol.social.filter;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class UserFilterEmail implements UserFilter {

    @Override
    public boolean booleanFilter(UserFilterDto userFilterDto) {
        return userFilterDto.getEmail() != null;
    }

    @Override
    public Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto) {
        return users.filter(u -> u.getEmail().startsWith(userFilterDto.getEmail()));
    }
}
