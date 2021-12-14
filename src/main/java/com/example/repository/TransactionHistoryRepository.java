package com.example.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class TransactionHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionHistoryRepository(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }

    public void insertTransaction(long customerId, String name, Double amount, String activity) {
        String sql = "INSERT INTO transactionhistory(customerid, name, balance, update_timestamp, activity) VALUES(?,?,?,CURRENT_TIMESTAMP,?)";
        jdbcTemplate.update(sql, customerId, name, amount, activity);
    }
}
