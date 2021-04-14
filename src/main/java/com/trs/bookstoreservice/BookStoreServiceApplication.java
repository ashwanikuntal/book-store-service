package com.trs.bookstoreservice;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.spi.NameTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.SDKGlobalConfiguration;

@EnableCaching
@EnableAsync
@SpringBootApplication
public class BookStoreServiceApplication {

	@Value("${http.timeout.in.seconds}")
	private Integer timeoutInSeconds;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		SpringApplication.run(BookStoreServiceApplication.class, args);
		System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		Duration duration = Duration.ofSeconds(timeoutInSeconds);
		return restTemplateBuilder
				.setConnectTimeout(duration)
				.setReadTimeout(duration)
				.build();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
		return mapper;
	}
}
