package com.mediscreen.mdiabeteassess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//enable component scanning for interfaces that declare they are Feign clients with @FeignClient
@EnableFeignClients("com.mediscreen.mdiabeteassess")
public class MdiabeteassessApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdiabeteassessApplication.class, args);
	}

}
