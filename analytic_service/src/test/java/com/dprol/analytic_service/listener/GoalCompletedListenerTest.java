package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.GoalCompletedEvent;
import com.dprol.analytic_service.event.PostCommentEvent;
import com.dprol.analytic_service.mapper.GoalCompletedMapper;
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
class GoalCompletedListenerTest {

    @Mock
    private AnalyticService analyticService;
    @Mock
    private GoalCompletedMapper goalCompletedMapper;

    @InjectMocks
    private GoalCompletedListener listener;

    private GoalCompletedEvent event;
    private Analytic analytic;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        event = GoalCompletedEvent.builder().userId(String.valueOf(123L)).goalId(String.valueOf(456L)).build();
        analytic = Analytic.builder().receiverId(456L).authorId(123L).build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, GoalCompletedEvent.class)).thenReturn(event);
        when(goalCompletedMapper.toAnalytic(event)).thenReturn(analytic);
        listener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticService, goalCompletedMapper);
        inOrder.verify(goalCompletedMapper).toAnalytic(event);
        inOrder.verify(analyticService).saveAnalytic(analytic);
    }
}
