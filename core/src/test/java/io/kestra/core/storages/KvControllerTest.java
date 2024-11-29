package io.kestra.core.controllers;

import io.kestra.core.storages.KvQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KvController.class)
public class KvControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KvQueryService kvQueryService;

    @BeforeEach
    public void setup() {
        Mockito.reset(kvQueryService);
    }

    @Test
    public void testQueryKv() throws Exception {
        String searchValue = "test";
        when(kvQueryService.queryKv(searchValue)).thenReturn(Arrays.asList("key1", "key2"));

        mockMvc.perform(get("/api/kv/query")
                .param("searchValue", searchValue))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"key1\",\"key2\"]"));
    }
}
