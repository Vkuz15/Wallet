package test.work.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.work.wallet.model.WalletOperation;
import test.work.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<String> performOperation(@RequestBody WalletOperation operation) {
        String response = walletService.processOperation(operation.getWalletId(), operation.getOperationType(), operation.getAmount());

        if (response.equals("Кошелек не найден!")) {
            return ResponseEntity.status(404).body(response); //404 Not Found
        } else if (response.equals("Недостаточно средств!")) {
            return ResponseEntity.status(400).body(response); //400 Bad Request
        } else if (response.equals("Недопустимый тип операции!")) {
            return ResponseEntity.status(400).body(response); //400 Bad Request
        }

        return ResponseEntity.ok(response); //200 OK
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        if (balance.compareTo(BigDecimal.valueOf(-1)) == 0) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
        return ResponseEntity.ok(balance); //200 OK
    }
}