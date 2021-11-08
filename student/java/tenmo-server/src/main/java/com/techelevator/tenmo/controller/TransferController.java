package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void transfer(@Valid @RequestBody Transfer transfer) {
        transferDao.transfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                transfer.getAccountTo(), transfer.getAmount());
        transferDao.accountTransfer(transfer);
    }

//    public void transfer(@RequestBody Transfer transfer) {
//        transferDao.createTransfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@RequestBody Account account) {
        List<Transfer> transferList = new ArrayList<>();
        transferList = transferDao.getTransfers(account);
        return transferList;
    }

    private TransferDto convertToDto(Transfer transfer) {
        TransferDto dto = new TransferDto(transfer.getTransferId(), transfer.getAmount());
        return dto;
    }
}
