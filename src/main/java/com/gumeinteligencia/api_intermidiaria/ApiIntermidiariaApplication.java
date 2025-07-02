package com.gumeinteligencia.api_intermidiaria;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiIntermidiariaApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("AWS_SQS_URL", dotenv.get("AWS_SQS_URL"));
		System.setProperty("AWS_DYNAMODB_URL", dotenv.get("AWS_DYNAMODB_URL"));

		SpringApplication.run(ApiIntermidiariaApplication.class, args);
	}

}
