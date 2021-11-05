package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean transfer(int accountFrom, int accountTo, BigDecimal amount) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, accountFrom);
        Account fromAccount = null;
        if (rs.next()) {
            fromAccount = jdbcAccountDao.getAccountById(accountFrom);
        }

        sql = "SELECT * FROM accounts WHERE account_id = ?";
        rs = jdbcTemplate.queryForRowSet(sql, accountTo);
        Account toAccount = null;
        if (rs.next()) {
            toAccount = jdbcAccountDao.getAccountById(accountTo);
        }

        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            try {
                jdbcTemplate.execute("BEGIN TRANSACTION");

                sql = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?";
                jdbcTemplate.update(sql, amount, fromAccount.getAccountId());

                sql = "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?";
                jdbcTemplate.update(sql, amount, toAccount.getAccountId());

                jdbcTemplate.execute("COMMIT");
            } catch (Exception e) {
                jdbcTemplate.execute("ROLLBACK");
                return false;
            }
        } return true;
    }

    @Override
    public boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom,
                                  int accountTo, BigDecimal amount) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) " + "VALUES (2, 2, ?, ?, ?);";
        Integer newTransferId;
        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, accountFrom, accountTo, amount);

        return true;
    }

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
