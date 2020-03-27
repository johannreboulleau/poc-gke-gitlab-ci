package com.example.demo.controllers;

import com.example.demo.exceptions.NotFoundException;
import com.example.demo.pojo.Comment;
import com.google.cloud.datastore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("datastore/comments")
@Slf4j
public class DatastoreRestController {

    private static final String ENTITY_COMMENT = "Comment";
    private static final String FIELD_COMMENT = "comment";

    @GetMapping
    public List<Comment> getAllComments() {

        log.info("getAllComments");

        List<Comment> commentList = new ArrayList<>();

        // Retrieve the last 10 visits from the datastore, ordered by timestamp.
        Query<Entity> query = Query.newEntityQueryBuilder().setKind(ENTITY_COMMENT).setLimit(10).build();
        QueryResults<Entity> results = getDataStore().run(query);

        while (results.hasNext()) {
            Entity entity = results.next();
            commentList.add(new Comment(entity.getKey().getId(), entity.getString(FIELD_COMMENT)));
        }

        return commentList;
    }

    @GetMapping("/{id}")
    public Comment getCommnentById(@PathVariable Long id) {

        log.info("getCommnentById wih id = {}", id);

       Entity entity = getDataStore().get(getDataStore().newKeyFactory().setKind(ENTITY_COMMENT).newKey(id));

       if (entity == null) {
           throw new NotFoundException();
       }

       return new Comment(entity.getKey().getId(), entity.getString(FIELD_COMMENT));
    }

    @PostMapping
    public Entity addComment(@RequestBody Comment comment) {

        log.info("addComment {}", comment);

        KeyFactory keyFactory = getDataStore().newKeyFactory().setKind(ENTITY_COMMENT);
        IncompleteKey key = keyFactory.setKind(ENTITY_COMMENT).newKey();

        FullEntity<IncompleteKey> commentEntity = Entity.newBuilder()
                .setKey(key)
                .set(FIELD_COMMENT, comment.getComment())
                .build();

        return getDataStore().add(commentEntity);
    }

    @PutMapping
    public Entity updateComment(@RequestBody Comment comment) {

        log.info("updateComment {}", comment);

        KeyFactory keyFactory = getDataStore().newKeyFactory().setKind(ENTITY_COMMENT);
        Key key = keyFactory.setKind(ENTITY_COMMENT).newKey(comment.getId());

        FullEntity<IncompleteKey> commentEntity = Entity.newBuilder()
                .setKey(key)
                .set(FIELD_COMMENT, comment.getComment())
                .build();

        return getDataStore().put(commentEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {

        log.info("deleteComment with id = {}", id);

        KeyFactory keyFactory = getDataStore().newKeyFactory().setKind(ENTITY_COMMENT);
        Key key = keyFactory.setKind(ENTITY_COMMENT).newKey(id);

        getDataStore().delete(key);
    }

    private Datastore getDataStore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }

}
