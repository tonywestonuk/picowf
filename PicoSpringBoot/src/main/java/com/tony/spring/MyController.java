package com.tony.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @RequestMapping("/")
	public String doGet(){
		return "Hello World - from Spring Boot";
	}
    
   
}
