package com.hmfurtado.invoice.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmfurtado.invoice.model.dto.ClientDTO;
import com.hmfurtado.invoice.model.dto.InvoiceDTO;
import com.hmfurtado.invoice.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InvoiceController.class)
class InvoiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvoiceService service;

    private ClientDTO clientDTO;

    private InvoiceDTO dto;

    @BeforeEach
    private void setUp() {
        clientDTO = ClientDTO.builder()
                .name("Paul")
                .email("paul@beatles.com")
                .id(1L)
                .fiscalId(1L)
                .build();
        dto = InvoiceDTO.builder()
                .invoiceId(1L)
                .clientDTO(Stream.of(clientDTO).collect(Collectors.toSet()))
                .build();

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }

    @Test
    void retrieveClientOkTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/retrieve-bahamas-client/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void retrieveClientNotFoundTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/retrieve-bahamas-client/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createInvoiceOkTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/store-bahamas-client/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("fiscal_id", "1")
                        .param("name", "Paul")
                        .param("email", "paul@beatles.com")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void createInvoiceUrlNotFoundTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/store-bahamas-client/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("fiscal_id", "1")
                        .param("name", "Paul")
                        .param("email", "paul@beatles.com")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createInvoiceWithoutParamsTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/store-bahamas-client/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
