package com.dprol.social.controller.profile;

import com.dprol.social.service.viewprofile.ViewProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")

public class ProfileController {

    private final ViewProfile viewProfile;

    @GetMapping("/view/{userId}")
    public void show(long profileId){
        viewProfile.show(profileId);
    }
}
