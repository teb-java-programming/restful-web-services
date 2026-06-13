package com.teb.practice.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.of;

import com.teb.practice.dto.PublicResponse;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PublicUserSteps {

    private final RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort private int port;
    private ResponseEntity<PublicResponse[]> response;

    @When("I call GET {string}")
    public void i_call_get(String path) {

        response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + path, PublicResponse[].class);
    }

    @Then("response status should be {int}")
    public void response_status_should_be(Integer status) {

        assertEquals(status, response.getStatusCode().value());
    }

    @Then("response should contain {int} users")
    public void response_should_contain_users(Integer size) {

        assertEquals(size, response.getBody() != null ? response.getBody().length : 0);
    }

    @Then("response should contain users with names:")
    public void response_should_contain_users_with_names(DataTable table) {

        List<String> actualNames =
                of(requireNonNull(response.getBody())).map(PublicResponse::name).sorted().toList();

        assertEquals(table.asList(), actualNames);
    }
}
