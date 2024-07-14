package com.irvan.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.irvan.ecommerce.model.OrderRequest;
import com.irvan.ecommerce.model.Product;
import com.irvan.ecommerce.model.Transaction;
import com.irvan.ecommerce.model.User;
import com.irvan.ecommerce.service.TransactionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public List<Transaction> getAllProducts() {
        String nativeQuery = "SELECT t.id as transaction_id, t.invoice, t.qty, t.total_price, " +
                "u.id as user_id, " +
                "u.name as user_name, " +
                "u.email as user_email, " +
                "p.id as product_id, " +
                "p.name as product_name, " +
                "p.price as product_price, " +
                "p.stock as product_stock " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN products p ON t.product_id = p.id";

        Query query = entityManager.createNativeQuery(nativeQuery);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        // Convert query result to DTOs
        List<Transaction> transactions = results.stream()
                .map(row -> mapRowToDto(row))
                .collect(Collectors.toList());

        return transactions;

    }

    @PostMapping("/checkout")
    public ResponseEntity<String> createOrder(@Validated @RequestBody OrderRequest orderRequest) {
        try {
            transactionService.createTransaction(orderRequest.getProductId(),
                    orderRequest.getUserId(),
                    orderRequest.getQty());
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Transaction mapRowToDto(Object[] row) {
        Transaction transactions = new Transaction();
        transactions.setId((Integer) row[0]);
        transactions.setInvoice((String) row[1]);
        transactions.setQty((Integer) row[2]);
        transactions.setTotalPrice((Integer) row[3]);

        User user = new User(0, null, null);
        user.setId((Integer) row[4]);
        user.setName((String) row[5]);
        user.setEmail((String) row[6]);

        transactions.setUser(user);

        Product product = new Product();
        product.setId((Integer) row[7]);
        product.setName((String) row[8]);
        product.setPrice((Integer) row[9]);
        product.setStock((Integer) row[10]);

        transactions.setProduct(product);
        return transactions;
    }

}
