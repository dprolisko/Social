package com.dprol.social.controller.jira;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.JiraDto;
import com.dprol.social.service.jira.JiraService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("account/jira")
@RequiredArgsConstructor
public class JiraController {

    private final JiraService jiraService;

    private final UserContextConfig userContextConfig;

    @PostMapping
    public JiraDto addJira(JiraDto jiraDto) {
        long userId = userContextConfig.getUserId();
        return jiraService.addJira(userId, jiraDto);
    }

    @DeleteMapping
    public void deleteJira(){
        long userId = userContextConfig.getUserId();
        jiraService.deleteJira(userId);
    }

    @GetMapping
    public JiraDto getJira(){
        long userId = userContextConfig.getUserId();
        return jiraService.getJira(userId);
    }
}
