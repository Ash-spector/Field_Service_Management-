package com.zidio.keystones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeystonesApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeystonesApplication.class, args);
	}

}
