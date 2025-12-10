package com.vonader.tender_info_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.vonader.tender_info_platform.domain")
@EnableJpaRepositories("com.vonader.tender_info_platform.repository")
public class TenderInfoPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenderInfoPlatformApplication.class, args);
	}
}