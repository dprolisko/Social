package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PremiumBoughtEvent;
import com.dprol.analytic_service.event.ProfileViewEvent;
import com.dprol.analytic_service.mapper.PremiumBoughtMapper;
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
class PremiumBoughtListenerTest {

    @Mock
    private AnalyticService analyticService;
    @Mock
    private PremiumBoughtMapper premiumBoughtMapper;

    @InjectMocks
    private PremiumBoughtListener listener;

    private PremiumBoughtEvent event;
    private Analytic analytic;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        event = PremiumBoughtEvent.builder().userId(String.valueOf(123L)).premiumBoughtId(String.valueOf(456L)).build();
        analytic = Analytic.builder().authorId(123L).receiverId(456L).build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};
        when(objectMapper.readValue(body, PremiumBoughtEvent.class)).thenReturn(event);
        when(premiumBoughtMapper.toAnalytic(event)).thenReturn(analytic);
        listener.onMessage(message, pattern);
        InOrder inOrder = inOrder(analyticService, premiumBoughtMapper);
        inOrder.verify(premiumBoughtMapper).toAnalytic(event);
        inOrder.verify(analyticService).saveAnalytic(analytic);
    }}
