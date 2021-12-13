package activity;


import domain.Customer;
import io.temporal.activity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import repository.CustomerRepository;
import repository.TransactionHistoryRepository;

import java.math.BigDecimal;
import java.util.List;

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
        try {
            System.out.println("Activity - Initiate Transfer.");

            List<Customer> customers = customerRepository.findByCustomerid(senderAcctNum);
            Customer sender = customers.get(0);

            customers = customerRepository.findByCustomerid(receiverAcctNum);

            Customer receiver = customers.get(0);

            sender.setBalance(sender.getBalance() - amount.doubleValue());
            receiver.setBalance(receiver.getBalance() + amount.doubleValue());
            customerRepository.save(sender);
            customerRepository.save(receiver);
        }
        catch(Exception e) {
            throw Activity.wrap(e);
        }
    }

    @Override
    public void registerFailedTransaction(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {

    }
}
