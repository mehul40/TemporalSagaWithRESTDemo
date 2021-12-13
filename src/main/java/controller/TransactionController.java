package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import request.TransferRequest;
import service.TransferService;

@RestController
public class TransactionController {

    @Autowired
    TransferService transferService;

    @PostMapping("/startTransaction")
    public void startTransaction(@RequestBody TransferRequest request) {
        transferService.startMoneyTransfer(request.getSenderAcctNum(), request.getReceiverAcctNum());
    }

    @PostMapping("/transfer")
    public void tranferMoney(@RequestBody TransferRequest request) {
        transferService.transferMoney(request.getSenderAcctNum(), request.getReceiverAcctNum(), request.getAmount());
    }

    @PostMapping("/completeTransaction")
    public void completeTransaction(@RequestBody TransferRequest request) {
        transferService.completeTransaction(request.getSenderAcctNum(), request.getReceiverAcctNum());
    }
}

