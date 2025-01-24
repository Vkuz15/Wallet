package test.work.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.work.wallet.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
