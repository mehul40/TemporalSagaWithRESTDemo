package workflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.math.BigDecimal;

@WorkflowInterface
public interface MoneyTransferWorkflow {
    public static final String QUEUE_NAME = "MoneyTransfer";

    @WorkflowMethod
    void startMoneyTransferWorkflow(long senderAcctNum, long receiverAcctNum);

    @SignalMethod
    void signalTransferCompleted(long senderAcctNum, long receiverAcctNum, BigDecimal amount);

    @SignalMethod
    void signalBackupCompleted(long senderAcctNum, long receiverAcctNum);

    @SignalMethod
    void signalTransactionCompleted(long senderAcctNum, long receiverAcctNum);

    @SignalMethod
    void signalCustomerActivityRegistered(long senderAcctNum, long receiverAcctNum, BigDecimal amount);
}
