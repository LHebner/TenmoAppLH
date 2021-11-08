package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface TransferDao {

    boolean transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);

    void accountTransfer(Transfer transfer);

    List<Transfer> getTransfers(Account account);

//    boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);
}
