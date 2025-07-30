package com.dprol.social.service.viewprofile;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.entity.user.User;
import com.dprol.social.event.ProfileViewEvent;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.publisher.ProfileViewPublisher;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.viewprofile.ViewProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class ViewProfileImpl implements ViewProfile {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserContextConfig userContextConfig;

    private final ProfileViewPublisher profileViewPublisher;

    @Override
    public void show(long profileId) {
        User user = userService.findUserById(profileId);
        long viewId = userContextConfig.getUserId();
        ProfileViewEvent event = new ProfileViewEvent(profileId, viewId, LocalDateTime.now());
        if(event.getViewerId() != event.getUserId()){
            profileViewPublisher.publisher(event);
        }
        userMapper.toDto(user);
    }
}
