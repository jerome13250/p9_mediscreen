package com.mediscreen.clientui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//enable component scanning for interfaces that declare they are Feign clients with @FeignClient
@EnableFeignClients("com.mediscreen.clientui")
public class ClientuiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientuiApplication.class, args);
	}

}
