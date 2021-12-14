package com.example.activity;


import com.example.domain.Customer;
import io.temporal.activity.Activity;
import io.temporal.workflow.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.repository.CustomerRepository;
import com.example.repository.TransactionHistoryRepository;

import java.math.BigDecimal;
import java.util.List;

public class MoneyTransferActivityImpl implements MoneyTransferActivity {

    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void cancelTransfer(Customer sender, Customer receiver) {
        System.out.println("Cancel Transfer");
        try {
            System.out.println("Rollback changes for " + sender.getCustomerid());
            System.out.println("Sender balance rolled back to : " + sender.getBalance());
            customerRepository.save(sender);
            System.out.println("Rollback changes for " + receiver.getCustomerid());
            System.out.println("Receiver balance rolled back to : " + receiver.getBalance());
            customerRepository.save(receiver);
        }
        catch(Saga.CompensationException sce) {
            System.out.println("Compensation Exception received.");
            throw Activity.wrap(sce);
        }
        catch(Exception e) {
            throw Activity.wrap(e);
        }

    }

    @Override
    public Customer getCustomerAccountDetails(long customerAcctNum) {
        return customerRepository.findByCustomerid(customerAcctNum).get(0);
    }

    @Override
    public void registerTransactionActivity(long senderAcctNum, long receiverAcctNum, BigDecimal amount) {
        try {
            List<Customer> customers = customerRepository.findByCustomerid(senderAcctNum);
            Customer sender = customers.get(0);
            System.out.println("Sender amount:" + sender.getBalance());

            customers = customerRepository.findByCustomerid(receiverAcctNum);
            Customer receiver = customers.get(0);

            String cActivity = "Sent " + amount.doubleValue() + " to " + receiver.getCustomerid() + " [ " + receiver.getCustomer_name() + " ] ";
            transactionHistoryRepository.insertTransaction(senderAcctNum, sender.getCustomer_name(), sender.getBalance() , cActivity);

            cActivity = "Received " + amount.doubleValue() + " from " + sender.getCustomerid() + " [ " + sender.getCustomer_name() + " ] ";
            transactionHistoryRepository.insertTransaction(receiverAcctNum, receiver.getCustomer_name(), receiver.getBalance(), cActivity);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
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
        try {
            System.out.println("Register failed activity");

            Customer sender = customerRepository.findByCustomerid(senderAcctNum).get(0);
            Customer receiver = customerRepository.findByCustomerid(receiverAcctNum).get(0);

            String cActivity = "Cancelled transaction to send " + amount + " to " + receiverAcctNum + " [ " + receiver.getCustomer_name() + " ] ";
            transactionHistoryRepository.insertTransaction(senderAcctNum, sender.getCustomer_name(), sender.getBalance(), cActivity );

            cActivity = "Rolled back transaction due to failure to receive " + amount + " from " + senderAcctNum + " [ " + sender.getCustomer_name() + " ] ";
            transactionHistoryRepository.insertTransaction(receiverAcctNum, receiver.getCustomer_name(), receiver.getBalance(), cActivity);
        }
        catch(Saga.CompensationException sce) {
            System.out.println("Compensation Exception received.");
            throw sce;
        }
        catch(Exception e) {
            throw Activity.wrap(e);
        }
    }
}
