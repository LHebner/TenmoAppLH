package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        // create transfer in transfer table
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) " + "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newTransferId;
        boolean success = false;
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
        } catch (DataAccessException e) {}

        // get account info for sender
        sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, accountFrom);
        Account fromAccount = null;
        if (rs.next()) {
            fromAccount = jdbcAccountDao.getAccountById(accountFrom);
        }

        // get account info for recipient
        sql = "SELECT * FROM accounts WHERE account_id = ?";
        rs = jdbcTemplate.queryForRowSet(sql, accountTo);
        Account toAccount = null;
        if (rs.next()) {
            toAccount = jdbcAccountDao.getAccountById(accountTo);
        }

        // amount transaction
        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            try {
                jdbcTemplate.execute("BEGIN TRANSACTION");

                sql = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?";
                jdbcTemplate.update(sql, amount, fromAccount.getAccountId());

                sql = "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?";
                jdbcTemplate.update(sql, amount, toAccount.getAccountId());

                jdbcTemplate.execute("COMMIT");
            } catch (DataAccessException e) {
                jdbcTemplate.execute("ROLLBACK");
                return success;
            }
        } return success = true;
    }

    public List<Transfer> getTransfers(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

//    @Override
//    public boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom,
//                                  int accountTo, BigDecimal amount) {
//        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
//                "account_to, amount) " + "VALUES (2, 2, ?, ?, ?);";
//        Integer newTransferId;
//        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, accountFrom, accountTo, amount);
//
//        return true;
//    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
