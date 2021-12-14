package com.example.service;

import com.google.common.base.Throwables;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.workflow.MoneyTransferWorkflow;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public void startMoneyTransfer(long senderAcctNum, long receiverAcctNum) {

        try {
            MoneyTransferWorkflow workflow = createWorkflowConnection(senderAcctNum, receiverAcctNum);
            WorkflowClient.start(workflow::startMoneyTransferWorkflow, senderAcctNum, receiverAcctNum);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void transferMoney(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {
        MoneyTransferWorkflow workflow = workflowClient.newWorkflowStub(MoneyTransferWorkflow.class, "MoneyTransfer_" + senderAcctNum + "_" + receiverAcctNum);
        try {
            workflow.signalBackupCompleted(senderAcctNum, receiverAcctNum);
        }
        catch(ApplicationFailure ex) {
            System.out.println("Application Failure");
            throw ex;
        }
        catch(WorkflowException we) {
            System.out.println("\n Stack Trace:\n" + Throwables.getStackTraceAsString(we));
            Throwable cause = Throwables.getRootCause(we);
            System.out.println("\n Root cause: " + cause.getMessage());
            throw we;
        }
        catch(Exception ge) {
            ge.printStackTrace();
            throw ge;
        }

        MoneyTransferWorkflow transferCompleteWorkflow = workflowClient.newWorkflowStub(MoneyTransferWorkflow.class, "MoneyTransfer_" + senderAcctNum + "_" + receiverAcctNum);
        try {
            transferCompleteWorkflow.signalTransferCompleted(senderAcctNum, receiverAcctNum, amount);
        }
        catch(ApplicationFailure ex) {
            System.out.println("Application Failure");
            throw ex;
        }
        catch(WorkflowException we) {
            System.out.println("\n Stack Trace:\n" + Throwables.getStackTraceAsString(we));
            Throwable cause = Throwables.getRootCause(we);
            System.out.println("\n Root cuase: " + cause.getMessage());
            throw we;
        }
        catch(Exception ge) {
            ge.printStackTrace();
            throw ge;
        }

        MoneyTransferWorkflow registerActivityWorkflow = workflowClient.newWorkflowStub(MoneyTransferWorkflow.class, "MoneyTransfer_" + senderAcctNum + "_" + receiverAcctNum);
        try {
            registerActivityWorkflow.signalCustomerActivityRegistered(senderAcctNum, receiverAcctNum, amount);
        }
        catch(ApplicationFailure ex) {
            System.out.println("Application Failure");
            throw ex;
        }
        catch(WorkflowException we) {
            System.out.println("\n Stack Trace:\n" + Throwables.getStackTraceAsString(we));
            Throwable cause = Throwables.getRootCause(we);
            System.out.println("\n Root cuase: " + cause.getMessage());
            throw we;
        }
        catch(Exception ge) {
            ge.printStackTrace();
            throw ge;
        }

    }

    public MoneyTransferWorkflow createWorkflowConnection(long senderAcctNum, long receiverAcctNum) {
        WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(MoneyTransferWorkflow.QUEUE_NAME)
                .setWorkflowId("MoneyTransfer_" + senderAcctNum + "_" + receiverAcctNum).build();
        return workflowClient.newWorkflowStub(MoneyTransferWorkflow.class, options);
    }

    public void completeTransaction(long senderAcctNum, long receiverAcctNum) {
        MoneyTransferWorkflow workflow = workflowClient.newWorkflowStub(MoneyTransferWorkflow.class, "MoneyTransfer_" + senderAcctNum + "_" + receiverAcctNum);
        workflow.signalTransactionCompleted(senderAcctNum, receiverAcctNum);
    }
}
