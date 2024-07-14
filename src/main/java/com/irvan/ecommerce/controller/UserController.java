package com.irvan.ecommerce.controller;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irvan.ecommerce.model.User;
import com.irvan.ecommerce.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    // Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public List<User> getAllUsers(@RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email) {
        List<User> users;
        users = userService.getAllUsers(name, email);

        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        int result = userService.createUser(user);
        if (result == 1) {
            // logger.info("User created: {}", toJson(user));

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        int result = userService.updateUser(id, userDetails);
        if (result == 1) {
            // logger.info("User update: {}", toJson(userDetails));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        int result = userService.deleteUser(id);
        if (result == 1) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // private String toJson(User user) {
    //     try {
    //         return objectMapper.writeValueAsString(user);
    //     } catch (Exception e) {
    //         logger.error("Error converting User to JSON", e);
    //         return "{}";
    //     }
    // }
}
