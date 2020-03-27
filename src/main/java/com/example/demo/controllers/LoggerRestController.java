package com.example.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logger")
@Slf4j
public class LoggerRestController {

    @RequestMapping
    public void logger() {
        // 3 maniere de logger

        System.out.println("logger System.out.printlm");
        System.err.println("logger System.err.printlm");

        log.info("logger log4j");
    }
}
