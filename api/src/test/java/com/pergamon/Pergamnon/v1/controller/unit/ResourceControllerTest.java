package com.pergamon.Pergamnon.v1.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pergamon.Pergamnon.v1.controller.ResourceController;
import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
public class ResourceControllerTest {
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
        List<Resource> resources = new ArrayList<>();

        when(resourceService.list()).thenReturn(resources);

        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"));
    }

    @Test
    public void testList_WithSearchParameter() throws Exception {
        List<Resource> resources = new ArrayList<>();

        when(resourceService.list()).thenReturn(resources);

        mockMvc.perform(get("/api/v1/resources").param("search", "www"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"));
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