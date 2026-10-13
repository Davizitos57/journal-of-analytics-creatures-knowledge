package com.scarfox.jack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JackApplication {

	static void main(String[] args) {
		SpringApplication.run(JackApplication.class, args);
	}

}
