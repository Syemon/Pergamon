package com.pergamon.Pergamon.v1.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.service.Profile;
import com.pergamon.Pergamon.v1.web.ResourceController;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @MockBean
    private ResourceService resourceService;

    @Test
    public void testUpsert_WhenCorrectUrl() throws Exception {
        body.put("url", "https://example.com");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testUpsert_WhenIncorrectUrl_ReturnError() throws Exception {
        body.put("url", "lorem");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid data"));
    }

    @Test
    public void testUpsert_WhenCannotConnect_ReturnError() throws Exception {
        body.put("url", "https://y.com");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("It's impossible to connect to given resource. Check given URL or try again later"));
    }

    @Test
    public void testUpsert_WhenEmptyBodyProvided_ReturnError() throws Exception {
        String jsonBody = mapper.writeValueAsString(body);
        mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("url: must not be null"));
    }

    @Test
    public void testList_WhenSuccess() throws Exception {
        List<ResourceEntity> resources = new ArrayList<>();

        when(resourceService.list()).thenReturn(resources);

        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(ResourceController.APPLICATION_HAL_JSON));
    }

    @Test
    public void testList_WithSearchParameter() throws Exception {
        List<ResourceEntity> resources = new ArrayList<>();

        when(resourceService.list()).thenReturn(resources);

        mockMvc.perform(get("/api/v1/resources").param("search", "www"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(ResourceController.APPLICATION_HAL_JSON));
    }

    @Test
    public void testDownload_WhenResourceNotExist_ReturnError() throws Exception {
        when(resourceService.exists(any(URL.class))).thenReturn(false);

        mockMvc.perform(get("/api/v1/resources/downloads?url=https://example.com"))
                .andExpect(status().isNotFound())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Resource from given url doesn\'t exist"));
    }
}