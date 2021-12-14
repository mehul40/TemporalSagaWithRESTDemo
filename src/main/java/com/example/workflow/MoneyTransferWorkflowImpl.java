package com.example.workflow;


import com.example.activity.MoneyTransferActivity;
import com.example.workflow.MoneyTransferWorkflow;
import com.google.common.base.Throwables;
import com.example.domain.Customer;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowException;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

import java.math.BigDecimal;
import java.time.Duration;

public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    private final RetryOptions retryOptions = RetryOptions.newBuilder().setMaximumAttempts(10).build();

    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(120)).setRetryOptions(retryOptions).build();

    private final MoneyTransferActivity moneyTransferActivity = Workflow.newActivityStub(MoneyTransferActivity.class, defaultActivityOptions);

    private final Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).setContinueWithError(true).build();

    private final Saga saga = new Saga(sagaOptions);

    private boolean isTransferCompleted = false;

    private boolean isAccountInfoRetrieved = false;

    private boolean isTransactionCompleted = false;

    private boolean isBackupCompleted = false;

    private boolean isCustomerActivityRegistered = false;

    private Customer senderAcct = new Customer();

    private Customer receiverAcct = new Customer();


    @Override
    public void startMoneyTransferWorkflow(long senderAcctNum, long receiverAcctNum) {
        try {
            Workflow.await( () -> this.isBackupCompleted);

            Workflow.await( () -> this.isTransferCompleted);

            Workflow.await( () -> this.isCustomerActivityRegistered);

            Workflow.await( () -> this.isTransactionCompleted);
        }
        catch(Exception e) {
            throw e;
        }
    }

    @Override
    public void signalBackupCompleted(long senderAcctNum, long receiverAcctNum) {

        if(this.isBackupCompleted == true) {
            System.out.println("Backup was already taken.");
            return;
        }
        try {
            Customer sender = moneyTransferActivity.getCustomerAccountDetails(senderAcctNum);
            senderAcct.setCustomerid(sender.getCustomerid());
            senderAcct.setCustomer_name(sender.getCustomer_name());
            senderAcct.setBalance(sender.getBalance());
            senderAcct.setUpdate_date(sender.getUpdate_date());

            System.out.println("Sender Balance during Backup: " + senderAcct.getBalance());

            Customer receiver = moneyTransferActivity.getCustomerAccountDetails(receiverAcctNum);
            receiverAcct.setCustomerid(receiver.getCustomerid());
            receiverAcct.setCustomer_name(receiver.getCustomer_name());
            receiverAcct.setBalance(receiver.getBalance());
            receiverAcct.setUpdate_date(receiver.getUpdate_date());

            System.out.println("Receiver Balance during Backup: " + receiverAcct.getBalance());
            isBackupCompleted = true;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void signalCustomerActivityRegistered(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {
        if(this.isCustomerActivityRegistered == true) {
            System.out.println("Customer activity was already registered. No further action required.");
            return;
        }
        try {
            saga.addCompensation(moneyTransferActivity::registerFailedTransaction, senderAcctNum, receiverAcctNum, amount);
            moneyTransferActivity.registerTransactionActivity(senderAcctNum, receiverAcctNum, amount);
            this.isCustomerActivityRegistered = true;
            System.out.println("Signal - Customer activity registered.");

        }
        catch(ActivityFailure e) {
            System.out.println("Failed to register customer activity. Execute saga compensation.");
            saga.compensate();
            throw e;
        }
        catch(ApplicationFailure ex) {
            System.out.println("Application Failure.");
            throw ex;
        }
        catch(WorkflowException we) {
            System.out.println("\n Stack Trace \n" + Throwables.getStackTraceAsString(we));
            Throwable cause = Throwables.getRootCause(we);
            System.out.println("\n Root cause: " + cause.getMessage());
            throw we;
        }
        catch(Exception ge) {
            ge.printStackTrace();
            throw ge;
        }
    }

    @Override
    public void signalTransferCompleted(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {
        if(this.isTransferCompleted == true) {
            System.out.println("Transfer was already completed. No further action required.");
            return;
        }
        try {
            saga.addCompensation(moneyTransferActivity::cancelTransfer, senderAcct, receiverAcct);
            moneyTransferActivity.initiateTransfer(senderAcctNum, receiverAcctNum, amount);
            this.isTransferCompleted = true;
            System.out.println("Signal - Transfer completed.");
        }
        catch(ActivityFailure e) {
            System.out.println("Failed to transfer money. Execute saga compensation.");
            saga.compensate();
            throw e;
        }
        catch(ApplicationFailure ex) {
            System.out.println("Application Failure.");
            throw ex;
        }
        catch(WorkflowException we) {
            System.out.println("\n Stack Trace \n" + Throwables.getStackTraceAsString(we));
            Throwable cause = Throwables.getRootCause(we);
            System.out.println("\n Root cause: " + cause.getMessage());
            throw we;
        }
        catch(Exception ge) {
            ge.printStackTrace();
            throw ge;
        }
    }

    @Override
    public void signalTransactionCompleted(long senderAcctNum, long receiverAcctNum) {
       if(this.isBackupCompleted && this.isTransferCompleted && this.isCustomerActivityRegistered) {
            System.out.println("Signal - Transaction Completed for " + senderAcctNum + "_" + receiverAcctNum);
            this.isTransactionCompleted = true;
        } else {
            System.out.println("Transfer has not completed yet.");
        }

    }


}
