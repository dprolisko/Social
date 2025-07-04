package com.dprol.social.filter.user;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;

import java.util.stream.Stream;

public interface UserFilter {

    boolean booleanFilter(UserFilterDto userFilterDto);

    Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto);
}
