package com.pergamon.Pergamnon.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ResourceController.class)
public class ResourceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Map<String, String> body = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

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
}