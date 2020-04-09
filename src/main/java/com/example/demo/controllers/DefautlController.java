package com.example.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
public class DefautlController {

    @GetMapping("/")
    public String getDefaultRoute() {
        log.info("getDefaultRoute Hello");
        return "Hello v2";
    }

    @GetMapping("/v2")
    public String getDefaultRouteV2() {
        log.info("getDefaultRoute Hello v2");
        return "Hello v2 v2";
    }

}
