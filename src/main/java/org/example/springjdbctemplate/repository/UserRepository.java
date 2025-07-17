package org.example.springjdbctemplate.repository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.springjdbctemplate.dao.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @PostConstruct
    // This method will be called after the bean is initialized
    public void init() {
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), password VARCHAR(100))";
        jdbcTemplate.execute(sql);
        log.info("Table created");
    }

    public void createUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword());
    }

    public void deleteUserById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateUserEmail(int id, String email) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        jdbcTemplate.update(sql, email, id);
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }, id).stream().findFirst().orElse(null);
    }

    public User getUserByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }, name);
    }

    public List<User> getAllUser(String name) {
        String sql = "SELECT * FROM users";

        return new ArrayList<User>(Collections.singleton(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        })));

    }


    // Example: programmatic transaction for creating and updating a user
    public void createAndUpdateUser(User user, String newEmail) {
        transactionTemplate.execute(status -> {
            createUser(user);
            // Use user.getUsername() only if it is unique and matches your update logic.
            // If you want to update by id, pass id instead of username.
            updateUserEmail(1, newEmail); // If you want to update by id, change this line.
            return null;
        });
    }
}
