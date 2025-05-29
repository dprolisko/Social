package com.dprol.social.service.user.filter;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;

import java.util.stream.Stream;

public interface UserFilterService {

    Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto);
}
