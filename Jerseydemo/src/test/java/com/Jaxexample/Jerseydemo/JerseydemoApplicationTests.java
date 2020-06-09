package com.Jaxexample.Jerseydemo;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JerseydemoApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Test
	public void testGetUserListSuccess() throws URISyntaxException {
		final RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:" + randomServerPort + "/users/";
		final URI uri = new URI(baseUrl);

		final ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		System.out.println(result.getBody());
		Assert.assertEquals(true, result.getBody().contains("user"));
	}

	@Test
	public void testGetUsersListSuccessWithHeaders() throws URISyntaxException {
		final RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:" + randomServerPort + "/users/";
		final URI uri = new URI(baseUrl);

		final HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-LOCATION", "USA");

		final HttpEntity<User> requestEntity = new HttpEntity<>(null, headers);

		try {
			restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
			System.out.println("Manual fail");
			Assert.fail();
		} catch (final HttpClientErrorException ex) {
			// Verify bad request and missing header
			Assert.assertEquals(400, ex.getRawStatusCode());
			Assert.assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

}
