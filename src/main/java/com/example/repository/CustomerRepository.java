package com.example.repository;

import com.example.domain.Customer;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CassandraRepository<Customer, Integer> {
    @AllowFiltering
    List<Customer> findByCustomerid(long customer_id);
}
