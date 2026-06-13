package com.teb.practice.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.status;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getEncoder;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.of;

import com.teb.practice.dto.SecureResponse;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SecureUserSteps {

    private final RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort private int port;
    private ResponseEntity<SecureResponse[]> response;

    @When("I secure call GET {string}")
    public void i_secure_call_get(String path) {

        try {
            response =
                    restTemplate.exchange(
                            "http://localhost:" + port + path, GET, null, SecureResponse[].class);
        } catch (HttpClientErrorException e) {
            response = status(e.getStatusCode()).body(null);
        }
    }

    @When("I call GET {string} with username {string} and password {string}")
    public void i_call_get_with_credentials(String path, String username, String password) {

        HttpHeaders headers = new HttpHeaders();

        headers.set(
                "Authorization",
                "Basic "
                        + getEncoder().encodeToString((username + ":" + password).getBytes(UTF_8)));

        try {
            response =
                    restTemplate.exchange(
                            "http://localhost:" + port + path,
                            GET,
                            new HttpEntity<>(headers),
                            SecureResponse[].class);
        } catch (HttpClientErrorException e) {
            response = status(e.getStatusCode()).body(null);
        }
    }

    @Then("secure response status should be {int}")
    public void secure_response_status_should_be(Integer status) {

        assertEquals(status, response.getStatusCode().value());
    }

    @Then("secure response should contain users with ages:")
    public void secure_response_should_contain_users_with_ages(DataTable table) {

        List<Integer> actual =
                of(requireNonNull(response.getBody()))
                        .map(SecureResponse::getAge)
                        .sorted()
                        .toList();

        assertEquals(table.asList(Integer.class), actual);
    }
}
