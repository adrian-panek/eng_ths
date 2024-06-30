package com.pwr.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwr.project.dto.LoginDTO;
import com.pwr.project.dto.RegisterDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testGetNoticeByIdUnauthenticated() throws Exception {
        long noticeId = 1L;
        ResultActions result = mockMvc.perform(get("/api/notices/{id}", noticeId));
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    public void testCreateUserEndpoint() throws Exception{
        RegisterDTO registerDTO = new RegisterDTO("Testowy", "uzytkownik", "testowy@uzytkownik.com", "test1234", false);
        String requestBody = objectMapper.writeValueAsString(registerDTO);
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    public void testLoginEndpoint() throws Exception {
        LoginDTO loginDTO = new LoginDTO("testowy@uzytkownik.com", "test1234");
        String requestBody = objectMapper.writeValueAsString(loginDTO);
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(4)
    public void testAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/auth/current-user")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

    }
}