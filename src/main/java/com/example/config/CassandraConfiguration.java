package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.stereotype.Component;

@Configuration
@EnableCassandraRepositories
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${cassandra.keyspace:placeholder}")
    private String cassandraKeySpace;

    @Value("${cassandra.contactpoints:placeholder}")
    private String cassandraContactPoints;

    @Value("${cassandra.port:0000}")
    private int port;

    @Value("${cassandra.datacenter:placeholder")
    private String datacenter;

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getKeyspaceName() {
        return cassandraKeySpace;
    }

    @Override
    public String getContactPoints() {
        return cassandraContactPoints;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
