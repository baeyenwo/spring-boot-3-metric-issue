package com.spring.restapi;

import org.assertj.core.api.Assertions;
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
class SpringBoot3RestApiMetricsTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBoot3RestApiMetricsTests.class);

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
				.rootUri("http://localhost:" + adminPort)
				.build();
	}
	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnHttpServerRequestsMetrics() {
		// Given

		// When
		// endpoint is annotated @Timed, not named
		restTemplate.getForObject("/mysite/api/tutorials1", String.class);

		// Then
		// http.server.requests with label "uri:/mysite/api/tutorials1" is added
		String body1 = sendRequest("/admin/metrics/http.server.requests", MediaType.ALL).getBody();
		Assertions.assertThat(body1).contains("/mysite/api/tutorials1");


		// When
		// endpoint is annotated @Timed, named
		restTemplate.getForObject("/mysite/api/tutorials2", String.class);

		// Then
		// ERROR: http.server.requests with label "uri:/mysite/api/tutorials1" is NOT added
		String body2 = sendRequest("/admin/metrics/http.server.requests", MediaType.ALL).getBody();
		Assertions.assertThat(body2).contains("/mysite/api/tutorials2");

		// raw prometheus output to verify
		sendRequest("/admin/prometheus", MediaType.ALL);
	}

	private ResponseEntity<String> sendRequest(String url, MediaType accept) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(accept));
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

		ResponseEntity<String> response = adminRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		LOGGER.info("Response received for '{}': {}", url, response.getBody());
		return response;
	}

}
