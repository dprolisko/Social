package com.dprol.social.filter.user;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class UserFilterUsername implements UserFilter {

    @Override
    public boolean booleanFilter(UserFilterDto userFilterDto) {
        return userFilterDto.getUsername() != null;
    }

    @Override
    public Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto) {
        return users.filter(u -> u.getUsername().startsWith(userFilterDto.getUsername()));
    }
}
