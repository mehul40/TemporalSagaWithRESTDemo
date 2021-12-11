package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Getter
@Setter
@Component
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @PrimaryKey("customerid")
    private long customerid;

    @Column("customer_name")
    private String customer_name;

    @Column("balance")
    private double balance;

    @Column("update_date")
    private Instant update_date;
}
