package com.example.demo.controllers;

import com.example.demo.pojo.Comment;
import com.example.demo.properties.PgProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("sql/comments")
@Slf4j
public class PostgresSqlRestController {

    @Autowired
    private PgProperties pgProperties;

    private DataSource dataSource;
    private HikariConfig config = new HikariConfig();

    @PostConstruct
    public void postConstruct() throws SQLException {

        log.info("postConstruct pgProperties = {}", pgProperties);

        config.setJdbcUrl(pgProperties.getJdbcUrl());
        config.setUsername(pgProperties.getUsername());
        config.setPassword(pgProperties.getPassword());

        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", pgProperties.getInstanceName());

        this.dataSource = new HikariDataSource(config);

        this.createTable(dataSource);
    }

    private void createTable(DataSource pool) throws SQLException {
        // Safely attempt to create the table schema.
        try (Connection conn = pool.getConnection()) {
            PreparedStatement createTableStatement = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS comment ( "
                            + "id SERIAL NOT NULL, comment VARCHAR(250),"
                            + " PRIMARY KEY (id) );"
            );
            createTableStatement.execute();

            log.info("create table done");
        }
    }

    @GetMapping
    public List<Comment> getComments() throws SQLException {

        List<Comment> commentList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from comment ");
            ResultSet resultSet = statement.executeQuery();
            log.info("getComments SQL size = {}", resultSet.getRow());

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String comment = resultSet.getString("comment");

                commentList.add(new Comment(id, comment));
            }
        }

        return commentList;
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from comment where id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long idResult = resultSet.getLong("id");
            String comment = resultSet.getString("comment");

            log.info("getComment id = {} and comment = {}", idResult, comment);

            return new Comment(idResult, comment);
        }
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) throws SQLException {

        log.info("addComment comment = {}", comment);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO comment(comment) VALUES(?)");
            preparedStatement.setString(1, comment.getComment());

            preparedStatement.execute();
        }
    }

    @PutMapping
    public void updateComment(@RequestBody Comment comment) throws SQLException {

        log.info("updateComment comment = {}", comment);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE comment set comment = ? where id = ? ");
            preparedStatement.setString(1, comment.getComment());
            preparedStatement.setLong(2, comment.getId());

            preparedStatement.execute();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) throws SQLException {

        log.info("deleteComment with id = {}", id);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM comment where id  = ?");
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
        }
    }
}
