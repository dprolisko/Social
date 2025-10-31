package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.event.ProfileAppearedInSearchEvent;
import com.dprol.analytic_service.mapper.ProfileAppearedInSearchMapper;
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
class ProfileAppearedInSearchListenerTest {

    @Mock
    private AnalyticService analyticService;
    @Mock
    private ProfileAppearedInSearchMapper profileAppearedInSearchMapper;

    @InjectMocks
    private ProfileAppearedInSearchListener listener;

    private ProfileAppearedInSearchEvent event;
    private Analytic analytic;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        event = ProfileAppearedInSearchEvent.builder().userId(String.valueOf(123L)).build();
        analytic = Analytic.builder().authorId(123L).build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, ProfileAppearedInSearchEvent.class)).thenReturn(event);
        when(profileAppearedInSearchMapper.toAnalytic(event)).thenReturn(analytic);
        listener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticService, profileAppearedInSearchMapper);
        inOrder.verify(profileAppearedInSearchMapper).toAnalytic(event);
        inOrder.verify(analyticService).saveAnalytic(analytic);
    }
}
