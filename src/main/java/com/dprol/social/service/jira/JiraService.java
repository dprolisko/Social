package com.dprol.social.service.jira;

import com.dprol.social.dto.JiraDto;

public interface JiraService {

    JiraDto addJira(JiraDto jiraDto);

    void deleteJira(Long id);

    JiraDto getJira(Long userId);
}
