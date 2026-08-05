package com.io.CoreBackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityRulesTest {



        @Autowired
        MockMvc mvc;

        @Test
        void publicEndpointAllowsAnonymous() throws Exception {
            mvc.perform(get("/api/v1/books/allbooks")).andExpect(status().isOk());
        }

        @Test
        void protectedEndpointRejectsAnonymous() throws Exception {
            mvc.perform(delete("/api/v1/books/1")).andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        void adminEndpointRejectsCustomer() throws Exception {
            mvc.perform(delete("/api/v1/books/1")).andExpect(status().isForbidden());
        }
    }
