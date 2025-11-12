package com.dprol.achievement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableFeignClients("com.dprol.achievement_service.client")
@EnableKafka
public class AchievementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AchievementServiceApplication.class, args);
	}

}
