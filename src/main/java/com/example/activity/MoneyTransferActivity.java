package com.example.activity;

import com.example.domain.Customer;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.math.BigDecimal;

@ActivityInterface
public interface MoneyTransferActivity {

    @ActivityMethod
    void initiateTransfer(long senderAcctNum, long receiverAcctNum, BigDecimal amount);

    @ActivityMethod
    void cancelTransfer(Customer sender, Customer receiver);

    @ActivityMethod
    Customer getCustomerAccountDetails(long customerAcctNum);

    @ActivityMethod
    void registerTransactionActivity(long senderAcctNum, long receiverAcctNum, BigDecimal amount);

    @ActivityMethod
    void registerFailedTransaction(long senderAcctNum, long receiverAcctNum, BigDecimal amount);

    @ActivityMethod
    boolean checkSufficientBalance(long senderAcctNum, BigDecimal amount);

}
