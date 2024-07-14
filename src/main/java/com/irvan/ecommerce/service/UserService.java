package com.irvan.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.irvan.ecommerce.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getAllUsers(String name, String email) {
        String query = "SELECT * FROM users WHERE 1=1";

        List<Object> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            query += " AND name LIKE ?";
            params.add("%" + name + "%");
        }

        if (email != null && !email.isEmpty()) {
            query += " AND email LIKE ?";
            params.add("%" + email + "%");
        }

        List<User> users = jdbcTemplate.query(query, params.toArray(),
                BeanPropertyRowMapper.newInstance(User.class));
        return users.stream()
                .filter(user -> (name == null || name.isEmpty()
                        || user.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(user -> (email == null || email.isEmpty()
                        || user.getEmail().toLowerCase().contains(email.toLowerCase())))
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { id }, BeanPropertyRowMapper.newInstance(User.class));
    }

    public int createUser(User user) {

        String query = "INSERT INTO users (name, email) VALUES (?, ?)";

        return jdbcTemplate.update(query, user.getName(), user.getEmail());
    }

    public int updateUser(Long id, User userDetails) {
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(query, userDetails.getName(), userDetails.getEmail(), id);
    }

    public int deleteUser(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }
}
