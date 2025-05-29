package com.dprol.social.filter;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class UserFilterCity implements UserFilter {

    @Override
    public boolean booleanFilter(UserFilterDto userFilterDto) {
        return userFilterDto.getCity() != null;
    }

    @Override
    public Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto) {
        return users.filter(u -> u.getCity().startsWith(userFilterDto.getCity()));
    }
}
