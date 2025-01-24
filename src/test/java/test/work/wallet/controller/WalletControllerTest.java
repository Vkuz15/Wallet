package test.work.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import test.work.wallet.model.WalletOperation;
import test.work.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    //Проверка пополнения кошелька
    @Test
    public void testPerformOperationDeposit() throws Exception {
        WalletOperation operation = new WalletOperation(UUID.randomUUID(), "DEPOSIT", BigDecimal.valueOf(1000.0));
        when(walletService.processOperation(any(), anyString(), any(BigDecimal.class))).thenReturn("Операция выполнена успешно!");

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена успешно!"));
    }

    //Проверка снятия средств с кошелька
    @Test
    public void testPerformOperationWithdraw() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperation operation = new WalletOperation(walletId, "WITHDRAW", BigDecimal.valueOf(1000.0));

        when(walletService.processOperation(any(UUID.class), anyString(), any(BigDecimal.class))).thenReturn("Операция выполнена успешно!");

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена успешно!"));
    }

    //Проверка баланса кошелька
    @Test
    public void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal expectedBalance = BigDecimal.valueOf(1500.0);

        when(walletService.getBalance(walletId)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedBalance.toString()));
    }

    //Проверка на проверку баланса на несуществующем кошельке
    @Test
    public void testGetBalanceNotFound() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.getBalance(walletId)).thenReturn(BigDecimal.valueOf(-1));

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isNotFound());
    }

    //Проверка на несуществующий кошелек
    @Test
    public void testPerformOperationWalletNotFound() throws Exception {
        WalletOperation operation = new WalletOperation(UUID.randomUUID(), "DEPOSIT", BigDecimal.valueOf(1000.0));
        when(walletService.processOperation(any(), anyString(), any(BigDecimal.class))).thenReturn("Кошелек не найден!");

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Кошелек не найден!"));
    }

    //Проверка на недостаточное количество средств при снятии
    @Test
    public void testPerformOperationInsufficientFunds() throws Exception {
        WalletOperation operation = new WalletOperation(UUID.randomUUID(), "WITHDRAW", BigDecimal.valueOf(1000.0));
        when(walletService.processOperation(any(), anyString(), any(BigDecimal.class))).thenReturn("Недостаточно средств!");

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Недостаточно средств!"));
    }

    //Проверка на недопустимый тип операции
    @Test
    public void testPerformOperationInvalidType() throws Exception {
        WalletOperation operation = new WalletOperation(UUID.randomUUID(), "INVALID_TYPE", BigDecimal.valueOf(1000.0));
        when(walletService.processOperation(any(), anyString(), any(BigDecimal.class))).thenReturn("Недопустимый тип операции!");

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Недопустимый тип операции!"));
    }
}