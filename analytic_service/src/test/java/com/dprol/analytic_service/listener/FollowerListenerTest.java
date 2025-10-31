package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.FollowerEvent;
import com.dprol.analytic_service.event.GoalCompletedEvent;
import com.dprol.analytic_service.mapper.FollowerMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerListenerTest {

    @Mock
    private AnalyticService analyticService;
    @Mock
    private FollowerMapper followerMapper;

    @InjectMocks
    private FollowerListener followerListener;

    private FollowerEvent event;
    private Analytic analytic;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        event = FollowerEvent.builder().followerId(String.valueOf(1L)).build();
        analytic = Analytic.builder().receiverId(1L).build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, FollowerEvent.class)).thenReturn(event);
        when(followerMapper.toAnalytic(event)).thenReturn(analytic);
        followerListener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticService, followerMapper);
        inOrder.verify(followerMapper).toAnalytic(event);
        inOrder.verify(analyticService).saveAnalytic(analytic);
    }
}
