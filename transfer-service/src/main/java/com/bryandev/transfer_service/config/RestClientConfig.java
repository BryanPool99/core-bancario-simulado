package com.bryandev.transfer_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient accountServiceRestClient(@Value("${services.account-service.url}") String baseUrlAcountService) {
        return RestClient.builder()
                .baseUrl(baseUrlAcountService)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
