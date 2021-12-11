package workflow;


import activity.MoneyTransferActivity;
import domain.Customer;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

import java.math.BigDecimal;
import java.time.Duration;

public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    private final RetryOptions retryOptions = RetryOptions.newBuilder().setMaximumAttempts(1).build();

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

    }

    @Override
    public void signalCustomerActivityRegistered(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }

    @Override
    public void signalTransferCompleted(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }

    @Override
    public void signalTransactionCompleted(long senderAcctNum, long receiverAcctNum) {

    }


}
