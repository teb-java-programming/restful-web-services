package com.teb.practice.cucumber.steps;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static java.util.Objects.nonNull;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import tools.jackson.databind.ObjectMapper;

public class UserStepDefinitions {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private MockMvc mockMvc;
    private MvcResult result;

    @When("I call {string} {string}")
    public void i_call_http(String method, String url) throws Exception {

        result = mockMvc.perform(buildRequest(method, url, null)).andReturn();
    }

    @When("I call {string} {string} with body")
    public void i_call_http_with_datatable(String method, String url, DataTable table)
            throws Exception {

        result =
                mockMvc.perform(
                                buildRequest(
                                        method,
                                        url,
                                        objectMapper.writeValueAsString(table.asMaps().getFirst())))
                        .andReturn();
    }

    @Then("response status should be {int}")
    public void response_status_should_be(int status) {

        int actual = result.getResponse().getStatus();

        if (actual != status) {
            throw new AssertionError("Expected " + status + " but got " + actual);
        }
    }

    @Then("response should contain user with id {string}")
    public void response_should_contain_user_with_id(String expectedId) throws Exception {

        String actualId =
                objectMapper
                        .readTree(result.getResponse().getContentAsString())
                        .get("data")
                        .get("id")
                        .asString();

        if (!actualId.equals(expectedId)) {
            throw new AssertionError("Expected id " + expectedId + " but got " + actualId);
        }
    }

    @Then("response should contain at least {int} users")
    public void response_should_contain_at_least_users(int minCount) throws Exception {

        int size =
                objectMapper.readTree(result.getResponse().getContentAsString()).get("data").size();

        if (size < minCount) {
            throw new AssertionError("Expected >= " + minCount + " but got " + size);
        }
    }

    private MockHttpServletRequestBuilder buildRequest(String method, String url, String body) {

        var request =
                switch (method.toUpperCase()) {
                    case "GET" -> get(url);
                    case "POST" -> post(url);
                    case "PUT" -> put(url);
                    case "PATCH" -> patch(url);
                    default -> throw new IllegalArgumentException("Unsupported method: " + method);
                };

        if (nonNull(body)) {
            request.contentType(APPLICATION_JSON).content(body);
        }

        return request;
    }
}
