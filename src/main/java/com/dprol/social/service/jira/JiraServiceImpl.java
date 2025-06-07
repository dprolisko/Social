package com.dprol.social.service.jira;

import com.dprol.social.dto.JiraDto;
import com.dprol.social.entity.Jira;
import com.dprol.social.exception.JiraNotFoundException;
import com.dprol.social.mapper.JiraMapper;
import com.dprol.social.repository.JiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class JiraServiceImpl implements JiraService {

    private final JiraRepository jiraRepository;

    private final JiraMapper jiraMapper;

    @Override
    public JiraDto addJira(JiraDto jiraDto) {
        jiraDto.setUserId(jiraDto.getUserId());
        jiraDto.setJiraId(jiraDto.getJiraId());
        Jira jira = jiraMapper.toEntity(jiraDto);
        jiraRepository.save(jira);
        return jiraMapper.toDto(jira);
    }

    @Override
    public void deleteJira(Long id) {
        Jira jira = jiraRepository.findByUserId(id)
                .orElseThrow(() -> new JiraNotFoundException("Jira account with id " + id + " not found"));
        jiraRepository.delete(jira);
    }

    @Override
    public JiraDto getJira(Long userId) {
        Jira jira = jiraRepository.findByUserId(userId)
                .orElseThrow(() -> new JiraNotFoundException("Jira account with id " + userId + " not found"));
        return jiraMapper.toDto(jira);
    }
}
