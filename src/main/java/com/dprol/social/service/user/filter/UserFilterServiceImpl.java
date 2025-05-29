package com.dprol.social.service.user.filter;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.filter.UserFilter;

import java.util.List;
import java.util.stream.Stream;

public class UserFilterServiceImpl implements UserFilterService {

    private List<UserFilter>

    @Override
    public Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto) {
        return Stream.empty();
    }
}
