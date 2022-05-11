package com.hmfurtado.bahamas.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BahamasGovController.class)
class BahamasGovControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("invoice", "1")
                        .param("fiscal_id", "1")
                        .param("name", "a")
                        .param("email", "a")
                        .content("Request completed"))
                .andExpect(status().isOk());
    }


}
