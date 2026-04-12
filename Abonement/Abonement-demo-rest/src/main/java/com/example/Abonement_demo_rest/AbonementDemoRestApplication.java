package com.example.Abonement_demo_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
		scanBasePackages = {
				"com.example.Abonement_demo_rest",
				"com.example.AbonementFitness"
		},
		exclude = {
				DataSourceAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class
		}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class AbonementDemoRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbonementDemoRestApplication.class, args);
	}
}