package io.kestra.core.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KvQueryServiceTest {
    private KvQueryService kvQueryService;

    @BeforeEach
    public void setup() {
        kvQueryService = new KvQueryService();
    }

    @Test
    public void testIndexAndQueryKv() throws Exception {
        // Index some data
        kvQueryService.indexKv("key1", "value1");
        kvQueryService.indexKv("key2", "value2");
        kvQueryService.indexKv("key3", "value1 value2");

        // Query data
        List<String> results1 = kvQueryService.queryKv("value1");
        List<String> results2 = kvQueryService.queryKv("value2");

        // Verify the results
        assertEquals(2, results1.size());
        assertEquals(List.of("key1", "key3"), results1);

        assertEquals(2, results2.size());
        assertEquals(List.of("key2", "key3"), results2);
    }
}
