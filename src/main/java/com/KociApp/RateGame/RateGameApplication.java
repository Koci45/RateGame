package com.KociApp.RateGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RateGameApplication {

	public static void main(String[] args){
		SpringApplication.run(RateGameApplication.class, args);
	}

}
