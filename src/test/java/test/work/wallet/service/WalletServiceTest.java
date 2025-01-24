package test.work.wallet.service;

import test.work.wallet.model.Wallet;
import test.work.wallet.repository.WalletRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.randomUUID();
        wallet = new Wallet(walletId);
    }

    //Проверка пополнения кошелька
    @Test
    public void testProcessOperationDeposit() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        String result = walletService.processOperation(walletId, "DEPOSIT", BigDecimal.valueOf(1000.0));

        assertEquals("Операция прошла успешно!", result);
        assertEquals(0, BigDecimal.valueOf(1000.0).compareTo(wallet.getBalance()));
        verify(walletRepository).save(wallet);
    }

    //Проверка снятия средств с кошелька
    @Test
    public void testProcessOperationWithdrawSuccess() {
        wallet.deposit(BigDecimal.valueOf(1000.0));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        String result = walletService.processOperation(walletId, "WITHDRAW", BigDecimal.valueOf(500.0));

        assertEquals("Операция прошла успешно!", result);
        assertEquals(0, BigDecimal.valueOf(500.0).compareTo(wallet.getBalance()));
        verify(walletRepository).save(wallet);
    }

    //Проверка на недостаточное количество средств при снятии
    @Test
    public void testProcessOperationWithdrawInsufficientFunds() {
        wallet.deposit(BigDecimal.valueOf(50.0));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        String result = walletService.processOperation(walletId, "WITHDRAW", BigDecimal.valueOf(1000.0));

        assertEquals("Недостаточно средств", result);
        assertEquals(0, BigDecimal.valueOf(50.0).compareTo(wallet.getBalance()));
        verify(walletRepository, never()).save(wallet);
    }

    //Проверка на несуществующий кошелек
    @Test
    public void testProcessOperationWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        String result = walletService.processOperation(walletId, "DEPOSIT", BigDecimal.valueOf(1000.0));

        assertEquals("Кошелек не найден!", result);
        verify(walletRepository, never()).save(any());
    }

    //Проверка на недопустимый тип операции
    @Test
    public void testProcessOperationInvalidOperationType() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        String result = walletService.processOperation(walletId, "INVALID_OPERATION", BigDecimal.valueOf(1000.0));

        assertEquals("Недопустимый тип операции!", result);
        assertEquals(0, BigDecimal.ZERO.compareTo(wallet.getBalance()));
        verify(walletRepository, never()).save(wallet);
    }

    //Проверка на проверку баланса на существующем кошельке
    @Test
    public void testGetBalanceWalletExists() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        wallet.deposit(BigDecimal.valueOf(1000.0));

        BigDecimal balance = walletService.getBalance(walletId);

        assertEquals(0, BigDecimal.valueOf(1000.0).compareTo(balance));
    }

    //Проверка на проверку баланса на несуществующем кошельке
    @Test
    public void testGetBalanceWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        BigDecimal balance = walletService.getBalance(walletId);

        assertEquals(BigDecimal.valueOf(-1), balance);
    }
}
