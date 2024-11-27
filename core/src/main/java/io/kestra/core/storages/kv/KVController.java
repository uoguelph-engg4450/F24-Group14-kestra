package io.kestra.core.controllers;

import io.kestra.core.storages.KvQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kv")
public class KvController {
    private final KvQueryService kvQueryService;

    public KvController() {
        this.kvQueryService = new KvQueryService();
    }

    @GetMapping("/query")
    public List<String> queryKv(@RequestParam String searchValue) throws Exception {
        return kvQueryService.queryKv(searchValue);
    }
}