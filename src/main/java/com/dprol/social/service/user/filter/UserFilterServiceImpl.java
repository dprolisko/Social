package com.dprol.social.service.user.filter;

import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.filter.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class UserFilterServiceImpl implements UserFilterService {

    private final List<UserFilter> userFilters;

    @Override
    public Stream<User> filterUsers(Stream<User> users, UserFilterDto userFilterDto) {
        if (userFilterDto != null){
            for (UserFilter userFilter : userFilters){
                if(userFilter.booleanFilter(userFilterDto)){
                    users = userFilter.filterUsers(users, userFilterDto);
                }
            }
        }
        return users;
    }
}
