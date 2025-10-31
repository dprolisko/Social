package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.ProfileViewEvent;
import com.dprol.analytic_service.mapper.AnalyticMapper;
import com.dprol.analytic_service.mapper.ProfileViewMapper;
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
class ProfileViewListenerTest {

    @Mock
    private AnalyticService analyticService;
    @Mock
    private ProfileViewMapper profileViewMapper;

    @InjectMocks
    private ProfileViewListener listener;

    private ProfileViewEvent event;
    private Analytic analytic;

    @Mock
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        event = ProfileViewEvent.builder().userId(String.valueOf(123L)).profileViewId(String.valueOf(456L)).build();
        analytic = Analytic.builder().authorId(123L).receiverId(456L).build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        when(profileViewMapper.toAnalytic(event)).thenReturn(analytic);
        listener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticService, profileViewMapper);
        inOrder.verify(profileViewMapper).toAnalytic(event);
        inOrder.verify(analyticService).saveAnalytic(analytic);
    }
}
