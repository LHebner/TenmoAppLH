package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create new transfer
    @Override
    public boolean transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) " + "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
        } catch (DataAccessException e) {
            return false;
        } return true;
    }

    // Amount transaction
    @Override
    public void accountTransfer (Transfer transfer) {

        try {
            jdbcTemplate.execute("BEGIN TRANSACTION");

            String sql = "UPDATE accounts SET balance = (balance - ?) " +
                    "WHERE account_id = ?;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFrom());

            sql = "UPDATE accounts SET balance = (balance + ?) " +
                    "WHERE account_id = ?;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountTo());

            jdbcTemplate.execute("COMMIT");
        } catch (DataAccessException e) {
            jdbcTemplate.execute("ROLLBACK");
        }
    }


    public List<Transfer> getTransfers(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getAccountId(), account.getAccountId());
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom,
                                  int accountTo, BigDecimal amount) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) " + "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id;";
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, accountFrom, accountTo, amount);
        } catch (DataAccessException e) {
            return false;
        }
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
