package com.hjucook.recommend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zheng
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"com.hjucook.recommend.mapper"})
@EnableSwagger2
public class RecommendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendApplication.class, args);
	}

}

