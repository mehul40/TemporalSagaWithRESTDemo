package com.example.config;

import com.example.activity.MoneyTransferActivityImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableCassandraRepositories
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${cassandra.keyspace.placeholder}")
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

    @Component
    @Configuration
    public static class TemporalConfig {

        @Value("{temporal.serviceAddress}")
        private String temporalServiceAddress;

        @Value("{temporal.namespace}")
        private String temporalNamespace;

        @Bean
        public WorkflowServiceStubs workflowServiceStubs() {
            return WorkflowServiceStubs.newInstance(WorkflowServiceStubsOptions.newBuilder().setTarget(temporalServiceAddress).build());
        }

        @Bean
        public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
            return WorkflowClient.newInstance(workflowServiceStubs,
                    WorkflowClientOptions.newBuilder().setNamespace(temporalNamespace).build());
        }

        @Bean
        public WorkerFactory workerFactory(WorkflowClient workflowClient) {
            return WorkerFactory.newInstance(workflowClient);
        }

        @Bean
        public MoneyTransferActivityImpl TransferActivity() {
            return new MoneyTransferActivityImpl();
        }

    }
}
