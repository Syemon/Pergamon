package com.pergamon.Pergamon.v1.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.service.Profile;
import com.pergamon.Pergamon.v1.web.ResourceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = Profile.TEST_FLYWAY)
public class ResourceControllerTest extends PostgresTestContainerResourceTest {
    private Map<String, String> body = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUpsert_WhenCorrectUrl() throws Exception {
        body.put("url", "https://wikipedia.com");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testUpsert_WhenIncorrectUrl_ReturnError() throws Exception {
        body.put("url", "lorem");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid data"));
    }

    @Test
    public void testUpsert_WhesnIncorrectUrl_ReturnError() throws Exception {
        body.put("url", "https://wikipedia.com/..asd");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                        "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Received content is not valid. Details: [Filename contains invalid path sequence]"));
    }

    @Test
    public void create_shouldReturnConflictStatus_whenResourceAlreadyExists() throws Exception {
        body.put("url", "https://example.com");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                        "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Resource https://example.com was already created"));
    }

    @Test
    public void testUpsert_WhenCannotConnect_ReturnError() throws Exception {
        body.put("url", "https://y.com");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("It's impossible to connect to given resource. Check given URL or try again later"));
    }

    @Test
    public void testUpsert_WhenEmptyBodyProvided_ReturnError() throws Exception {
        String jsonBody = mapper.writeValueAsString(body);
        mockMvc.perform(post(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("url: must not be null"));
    }

    @Test
    public void testList_WhenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(ResourceController.APPLICATION_HAL_JSON));
    }

    @Test
    public void testList_WithSearchParameter() throws Exception {
        mockMvc.perform(get("/api/v1/resources").param("search", "www"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(ResourceController.APPLICATION_HAL_JSON));
    }

    @Test
    public void testDownload_WhenResourceNotExist_ReturnError() throws Exception {
        mockMvc.perform(get("/api/v1/resources/downloads?url=https://notfound.com"))
                .andExpect(status().isNotFound())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Resource from given url doesn\'t exist"));
    }
}