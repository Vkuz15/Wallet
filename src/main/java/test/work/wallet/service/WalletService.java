package test.work.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.work.wallet.model.Wallet;
import test.work.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public String processOperation(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null) {
            return "Кошелек не найден!";
        }

        switch (operationType.toUpperCase()) {
            case "DEPOSIT":
                wallet.deposit(amount);
                break;
            case "WITHDRAW":
                try {
                    wallet.withdraw(amount);
                } catch (IllegalArgumentException e) {
                    return e.getMessage();
                }
                break;
            default:
                return "Недопустимый тип операции!";
        }
        walletRepository.save(wallet);
        return "Операция прошла успешно!";
    }

    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        return wallet != null ? wallet.getBalance() : BigDecimal.valueOf(-1);
    }
}
