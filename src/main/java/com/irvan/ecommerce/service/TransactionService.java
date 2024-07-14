package com.irvan.ecommerce.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void createTransaction(Integer productId, Integer userId, Integer qty) {

        if (!isUserIdValid(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }

        if (!isProductIdValid(productId)) {
            throw new IllegalArgumentException("Invalid productId");
        }
        String invoice = generateInvoiceCode();
        String nativeQuery = "INSERT INTO transactions (product_id, user_id, qty, total_price, invoice) " +
                "VALUES (:productId, :userId, :qty, " +
                "(SELECT price * :qty FROM products WHERE id = :productId), :invoice)";

        entityManager.createNativeQuery(nativeQuery)
                .setParameter("productId", productId)
                .setParameter("userId", userId)
                .setParameter("qty", qty)
                .setParameter("invoice", invoice)
                .executeUpdate();
    }

    private String generateInvoiceCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        return "INV-" + randomNumber;
    }

    private boolean isUserIdValid(Integer userId) {
        String userQuery = "SELECT COUNT(*) FROM users WHERE id = :userId";
        Query query = entityManager.createNativeQuery(userQuery)
                .setParameter("userId", userId);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    private boolean isProductIdValid(Integer productId) {
        String userQuery = "SELECT COUNT(*) FROM products WHERE id = :productId";
        Query query = entityManager.createNativeQuery(userQuery)
                .setParameter("productId", productId);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

}
