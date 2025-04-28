package com.evapi.tracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectricVehiclesApplication {
	//private static Logger log = LoggerFactory.getLogger(ElectricVehiclesApplication.class);
	public static void main(String[] args) {
		//log.info("Application Starting up...");
		SpringApplication.run(ElectricVehiclesApplication.class, args);
	}

}
