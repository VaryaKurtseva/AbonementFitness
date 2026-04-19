package com.example.Abonement_demo_rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
		scanBasePackages = {
				"com.example.Abonement_demo_rest",
				"com.example.AbonementFitness"
		}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@OpenAPIDefinition(info = @Info(title = "Abonement Fitness API", version = "1.0"))
public class AbonementDemoRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbonementDemoRestApplication.class, args);
	}
}