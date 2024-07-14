package com.irvan.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.irvan.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> getAllProducts(String name) {
        String query = "SELECT * FROM products WHERE 1=1";

        List<Object> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            query += " AND name LIKE ?";
            params.add("%" + name + "%");
        }

        List<Product> products = jdbcTemplate.query(query, params.toArray(),
                BeanPropertyRowMapper.newInstance(Product.class));

        return products.stream()
                .filter(product -> (name == null || name.isEmpty()
                        || product.getName().toLowerCase().contains(name.toLowerCase())))
                .collect(Collectors.toList());
    }

    public Product getProductById(Long id) {
        String query = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { id },
                BeanPropertyRowMapper.newInstance(Product.class));
    }

    public int createProduct(Product product) {
        String query = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, product.getName(), product.getPrice(), product.getStock());
    }

    public int updateProduct(Long id, Product productDetails) {
        String query = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
        return jdbcTemplate.update(query, productDetails.getName(), productDetails.getPrice(),
                productDetails.getStock(), id);
    }

    public int deleteProduct(Long id) {
        String query = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }
}
