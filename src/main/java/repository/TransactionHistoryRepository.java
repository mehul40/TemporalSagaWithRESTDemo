package repository;

import domain.TransactionHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class TransactionHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionHistoryRepository(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }

    public void insertTransaction(long customerId, String name, BigDecimal amount, String activity) {
        String sql = "INSERT INTO transactionhistory(customerId, name, amount, update_timestamp, activity) VALUES(?,?,?,CURRENT_TIMESTAMP,?)";
        jdbcTemplate.update(sql, customerId, name, amount, activity);
    }
}
