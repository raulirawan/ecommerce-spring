package com.irvan.ecommerce;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.irvan.ecommerce.model.User;
import com.irvan.ecommerce.service.UserService;

public class UserServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers_WithFilters() {
        List<User> mockUsers = Arrays.asList(
                new User(1, "John Doe", "john.doe@example.com"),
                new User(2, "Jane Smith", "jane.smith@example.com"));

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class)))
                .thenReturn(mockUsers);

        String nameFilter = "John";
        String emailFilter = "example.com";

        List<User> filteredUsers = userService.getAllUsers(nameFilter, emailFilter);

        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class));

        assertEquals(1, filteredUsers.size());
        assertEquals("John Doe", filteredUsers.get(0).getName());
    }

    @Test
    public void testGetAllUsers_WithoutFilters() {

        List<User> mockUsers = Arrays.asList(
                new User(1, "John Doe", "john.doe@example.com"),
                new User(2, "Jane Smith", "jane.smith@example.com"));

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class)))
                .thenReturn(mockUsers);

        List<User> allUsers = userService.getAllUsers(null, null);

        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class));

        assertEquals(2, allUsers.size());
        assertEquals("John Doe", allUsers.get(0).getName());
        assertEquals("Jane Smith", allUsers.get(1).getName());
    }
}
