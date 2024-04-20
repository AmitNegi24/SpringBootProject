package com.amit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@ServletComponentScan

@PropertySource("classpath:messages.properties")
public class EKartApplication {

	public static void main(String[] args) {
		SpringApplication.run(EKartApplication.class, args);
	}

}
