package com.meli.middleend;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.client.ApiMLClientImpl;
import com.meli.middleend.service.ValidatorService;
import com.meli.middleend.service.impl.ValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class MeliMiddleendApplicationTests {


	@Configuration
	static class TestConfig {

		@Bean
		public ApiMLClient apiMLClient() {
			return new ApiMLClientImpl(new RestTemplate());
		}
	}

	@Test
	void contextLoads() {
	}

}
