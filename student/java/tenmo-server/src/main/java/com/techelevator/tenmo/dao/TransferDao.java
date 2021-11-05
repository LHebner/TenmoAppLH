package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferDao {

    boolean transfer(int accountFrom, int accountTo, BigDecimal amount);

    boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);
}
