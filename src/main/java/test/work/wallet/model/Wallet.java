package test.work.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue
    private UUID walletId;

    @Column(nullable = false)
    private BigDecimal balance;

    public Wallet(UUID walletId) {
        this.walletId = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
    }

    //Метод для пополнения счета
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Сумма депозита должна быть положительной и не равной null");
        }
        this.balance = this.balance.add(amount);
    }

    //Метод для вывода средств
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Сумма вывода должна быть положительной и не равной null");
        }
        if (amount.compareTo(this.balance) > 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }
        this.balance = this.balance.subtract(amount);
    }

    public UUID getWalletId() {
        return walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", balance=" + balance +
                '}';
    }
}
