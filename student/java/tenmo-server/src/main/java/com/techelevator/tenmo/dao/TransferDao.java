package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);

    List<Transfer> getTransfers(int accountId);

    boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom,
                           int accountTo, BigDecimal amount);
}
