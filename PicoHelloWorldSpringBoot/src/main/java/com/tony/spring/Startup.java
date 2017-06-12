package com.tony.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("org.picojs")
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}


}
