package com.repolens;

import com.repolens.github.client.GitHubClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GitHubClientProperties.class)
public class RepolensApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepolensApplication.class, args);
	}
}