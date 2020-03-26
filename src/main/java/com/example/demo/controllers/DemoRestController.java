package com.example.demo.controllers;

import com.example.demo.pojo.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class DemoRestController {


    private static final Logger logger = LoggerFactory.getLogger(DemoRestController.class);

    @GetMapping
    public Comment getComment() {

        System.out.println("getCommnent System.out.printlm");
        System.err.println("getCommnent System.err.printlm");

        logger.info("getCommnent log4j");
        return new Comment(1L, "comment 1");
    }

    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {

        logger.info("addComment {}", comment);
        return comment;
    }
}
