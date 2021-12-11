package activity;


import domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import repository.CustomerRepository;
import repository.TransactionHistoryRepository;

import java.math.BigDecimal;

public class MoneyTransferActivityImpl implements  MoneyTransferActivity{

    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void cancelTransfer(Customer sender, Customer receiver) {

    }

    @Override
    public Customer getCustomerAccountDetails(long customerAcctNum) {
        return null;
    }

    @Override
    public void registerTransactionActivity(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }

    @Override
    public void initiateTransfer(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }

    @Override
    public void registerFailedTransaction(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }
}
