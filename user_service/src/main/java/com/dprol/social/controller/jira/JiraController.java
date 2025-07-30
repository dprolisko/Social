package com.dprol.social.controller.jira;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.JiraDto;
import com.dprol.social.service.jira.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account/jira")
@RequiredArgsConstructor
public class JiraController {

    private final JiraService jiraService;

    private final UserContextConfig userContextConfig;

    @PostMapping("/create")
    public JiraDto addJira(JiraDto jiraDto) {
        long userId = userContextConfig.getUserId();
        return jiraService.addJira(userId, jiraDto);
    }

    @DeleteMapping("/delete/{jiraId}")
    public void deleteJira(long jiraId) {
        long userId = userContextConfig.getUserId();
        jiraService.deleteJira(userId);
    }

    @GetMapping("/get/{jiraId}")
    public JiraDto getJira(long jiraId){
        long userId = userContextConfig.getUserId();
        return jiraService.getJira(userId);
    }
}
