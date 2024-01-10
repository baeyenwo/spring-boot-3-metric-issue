package com.bezkoder.spring.restapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBoot3RestApiExampleApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBoot3RestApiExampleApplicationTests.class);

	@LocalManagementPort
	int adminPort;

	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	@Autowired
	TestRestTemplate restTemplate;

	RestTemplate adminRestTemplate;

	@BeforeEach
	public void addRootUri() {
		adminRestTemplate = restTemplateBuilder
				.rootUri("http://localhost:" + adminPort) // this endpoint is on a separate port only accessible from within the tenant -> no authentication needed.
				.build();
	}
	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnStatusHealthyThroughPrometheus() {
		// Given - When
		// http_client_requests_active_seconds
		restTemplate.getForObject("/api/tutorials", String.class);

		// http_custom -> all spring labels disappear
		restTemplate.getForObject("/api/tutorials2", String.class);

		ResponseEntity<String> initialResponse = sendRequest(adminRestTemplate, "/admin/prometheus", MediaType.ALL);
	}

	private ResponseEntity<String> sendRequest(RestTemplate restTemplate, String url, MediaType accept) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(accept));
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		LOGGER.info("Response received for '{}': {}", url, response.getBody());
		return response;
	}

}
