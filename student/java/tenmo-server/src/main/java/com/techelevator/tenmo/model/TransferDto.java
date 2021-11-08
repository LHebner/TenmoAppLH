package com.techelevator.tenmo.model;



import java.math.BigDecimal;

public class TransferDto {

    private int transferId;
    private BigDecimal amount;

    public TransferDto() {}

    public TransferDto(int transferId,  BigDecimal amount) {
        this.transferId = transferId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
