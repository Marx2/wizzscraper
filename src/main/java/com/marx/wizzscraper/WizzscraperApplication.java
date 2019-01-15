package com.marx.wizzscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WizzscraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(WizzscraperApplication.class, args);
	}

}

