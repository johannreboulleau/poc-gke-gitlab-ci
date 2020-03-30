package com.example.demo.controllers;

import com.example.demo.pojo.Comment;
import com.googlecode.objectify.ObjectifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
@RequestMapping("objectify/comments")
@Slf4j
public class ObjectifyRestController {

    @PostConstruct
    public void postContruct() {
        ObjectifyService.init();
        ObjectifyService.register(Comment.class);
        ObjectifyService.begin();
    }

    @GetMapping
    public List<Comment> getAllComments() {

        log.info("getAllComments Objectify");

        return ofy().load().type(Comment.class).list();
    }

    @GetMapping("/{id}")
    public Comment getCommnentById(@PathVariable Long id) {

        log.info("getCommnentById Objectify wih id = {}", id);

        return ofy().load().type(Comment.class).id(id).now();
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {

        log.info("addComment Objectify with body = {}", comment);

        ofy().save().entity(comment);
    }

    @PutMapping
    public void updateComment(@RequestBody Comment comment) {

        log.info("updateComment Objectify with body = {}", comment);

        ofy().save().entity(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {

        log.info("deleteComment with id = {}", id);

        ofy().delete().entity(getCommnentById(id));
    }

}
