package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TransactionHistory {
    private long customerid;
    private String name;
    private BigDecimal balance;
    private Instant update_timestamp;
    private String activity;
}
