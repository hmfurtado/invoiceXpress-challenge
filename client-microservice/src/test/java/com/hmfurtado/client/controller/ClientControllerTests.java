package com.hmfurtado.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmfurtado.client.model.dto.ClientDTO;
import com.hmfurtado.client.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClientController.class)
class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService service;

    @Test
    void getClientByFiscalIdOkTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/client/fiscalId/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getClientByFiscalIdNotFoundTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/client/fiscalId/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClientTest() throws Exception {
        ClientDTO dto = ClientDTO.builder()
                .name("John")
                .email("john@beatles.com")
                .id(1L)
                .fiscalId(1L)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}
