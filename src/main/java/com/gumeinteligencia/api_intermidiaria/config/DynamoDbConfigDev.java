package com.gumeinteligencia.api_intermidiaria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("dev")
public class DynamoDbConfigDev {
    @Value("${aws.dynamodb.url}")
    private final String dynamoEndpoint;

    public DynamoDbConfigDev(
            @Value("${aws.dynamodb.url}") String dynamoEndpoint
    ) {
        this.dynamoEndpoint = dynamoEndpoint;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamoEndpoint))
                .region(Region.US_EAST_1)
                .build();
    }
}
