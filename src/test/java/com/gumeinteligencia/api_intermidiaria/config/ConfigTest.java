package com.gumeinteligencia.api_intermidiaria.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigTest {

    @Test
    void deveCriarSqsClient() {
        SqsConfig config = new SqsConfig("http://localhost:9324");

        try (SqsClient client = config.sqsClient()) {
            assertNotNull(client);
        }
    }

    @Test
    void deveCriarDynamoDbClientProd() {
        DynamoDbConfig config = new DynamoDbConfig();

        try (DynamoDbClient client = config.dynamoDbClient()) {
            assertNotNull(client);
        }
    }

    @Test
    void deveCriarDynamoDbClientDev() {
        DynamoDbConfigDev config = new DynamoDbConfigDev("http://localhost:8000");

        try (DynamoDbClient client = config.dynamoDbClient()) {
            assertNotNull(client);
        }
    }

    @Test
    void deveCriarWebClient() {
        WebClientConfig config = new WebClientConfig("http://localhost:8080");
        WebClient client = config.webClient(WebClient.builder());

        assertNotNull(client);
    }
}
