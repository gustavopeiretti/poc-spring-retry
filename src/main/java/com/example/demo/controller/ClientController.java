package com.example.demo.controller;

import com.example.demo.model.DemoResponse;
import com.example.demo.service.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    private final DemoService demoService;

    public ClientController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/find")
    public ResponseEntity<DemoResponse> search(@RequestParam("name") String name) {
        DemoResponse response = demoService.search(name);
        return ResponseEntity.ok(response);
    }


}
