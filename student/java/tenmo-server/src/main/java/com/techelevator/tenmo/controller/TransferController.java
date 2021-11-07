package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer/")
public class TransferController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public void transfer(@RequestBody Transfer transfer) {
        transferDao.createTransfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @RequestMapping(path = "all", method = RequestMethod.GET)
    public List<Transfer> getTransferList(Account account) {
        List<Transfer> transferList = new ArrayList<>();
        transferList = transferDao.getTransfers(account.getAccountId());
        return transferList;
    }
}
