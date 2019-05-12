package com.pergamon.Pergamnon.v1.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pergamon.Pergamnon.v1.controller.ResourceController;
import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.resource.ResourceResource;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private MockMvc mockMvc;

    private Map<String, String> body = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ResponseEntity responseEntity;

    @MockBean
    private ResourceService resourceService;

    @Test
    public void testCreate_WhenCorrectUrl() throws Exception {
        this.body.put("url", "https://example.com");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testCreate_WhenIncorrectUrl_ReturnError() throws Exception {
        this.body.put("url", "lorem");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(put(
                "/api/v1/resources").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testList_WhenSuccess() throws Exception {
        List<Resource> resources = new ArrayList<>();
        List<ResourceResource> folderResources = new ArrayList<>();

        when(this.resourceService.list()).thenReturn(resources);

        this.mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"));
    }

    @Test
    public void testDownload_WhenResourceNotExist_ReturnError() throws Exception {
        when(this.resourceService.exists(any(URL.class))).thenReturn(false);

        this.mockMvc.perform(get("/api/v1/resources/downloads?url=https://example.com"))
                .andExpect(status().isNotFound())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Resource from given url doesn\'t exist"));
    }
}