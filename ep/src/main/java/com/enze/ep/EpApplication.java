package com.enze.ep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan
@SpringBootApplication
@MapperScan(basePackages="com.enze.ep.dao")
@EnableScheduling
public class EpApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpApplication.class, args);
	}
}
