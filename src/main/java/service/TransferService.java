package service;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public void startMoneyTransfer(long senderAcctNum, long receiverAcctNum) {

    }

}
