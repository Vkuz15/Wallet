package test.work.wallet.model;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletOperation {

    private UUID walletId;
    private String operationType; // DEPOSIT or WITHDRAW
    private BigDecimal amount;

    public WalletOperation(UUID walletId, String operationType, BigDecimal amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
