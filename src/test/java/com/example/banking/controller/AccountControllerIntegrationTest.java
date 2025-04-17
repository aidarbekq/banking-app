package com.example.banking.controller;

import com.example.banking.dto.AmountRequest;
import com.example.banking.dto.CreateAccountRequest;
import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.hamcrest.Matchers.containsString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private AccountRepository accountRepository;

    private Long userId;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User("Account Owner", "account@example.com", LocalDateTime.now());
        user = userRepository.save(user);
        userId = user.getId();
    }

    @Test
    @Order(1)
    void testCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest("ACC10001", new BigDecimal("1000.00"));

        mockMvc.perform(post("/api/users/" + userId + "/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountNumber").value("ACC10001"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    @Order(2)
    void testDepositToAccount() throws Exception {
        Account account = new Account("ACC20002", new BigDecimal("500.00"), userRepository.findById(userId).get());
        account = accountRepository.save(account);

        AmountRequest deposit = new AmountRequest(new BigDecimal("150.00"));

        mockMvc.perform(post("/api/users/" + userId + "/accounts/" + account.getId() + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(650.00));
    }

    @Test
    @Order(3)
    void testWithdrawFromAccount() throws Exception {
        Account account = new Account("ACC30003", new BigDecimal("700.00"), userRepository.findById(userId).get());
        account = accountRepository.save(account);

        AmountRequest withdraw = new AmountRequest(new BigDecimal("200.00"));

        mockMvc.perform(post("/api/users/" + userId + "/accounts/" + account.getId() + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    @Order(4)
    void testGetAccountsByUser() throws Exception {
        accountRepository.save(new Account("ACC40004", new BigDecimal("100.00"), userRepository.findById(userId).get()));
        accountRepository.save(new Account("ACC40005", new BigDecimal("300.00"), userRepository.findById(userId).get()));

        mockMvc.perform(get("/api/users/" + userId + "/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testWithdrawInsufficientBalance_shouldReturnBadRequest() throws Exception {
        Account account = new Account("ACC999", new BigDecimal("100.00"), userRepository.findById(userId).get());
        account = accountRepository.save(account);

        AmountRequest withdraw = new AmountRequest(new BigDecimal("200.00"));

        mockMvc.perform(post("/api/users/" + userId + "/accounts/" + account.getId() + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Недостаточно средств для списания"));
    }

    @Test
    @Order(5)
    void testDeposit_negativeAmount_shouldReturnBadRequest() throws Exception {
        Account account = new Account("ACC_NEG_TEST", new BigDecimal("100.00"), userRepository.findById(userId).get());
        account = accountRepository.save(account);

        AmountRequest negativeAmount = new AmountRequest(new BigDecimal("-50.00"));

        mockMvc.perform(post("/api/users/" + userId + "/accounts/" + account.getId() + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeAmount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Amount must be > 0")));
    }

}
