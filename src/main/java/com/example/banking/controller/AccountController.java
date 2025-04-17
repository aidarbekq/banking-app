package com.example.banking.controller;

import com.example.banking.dto.AccountDto;
import com.example.banking.dto.AmountRequest;
import com.example.banking.dto.CreateAccountRequest;
import com.example.banking.entity.Account;
import com.example.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Создать банковский счет")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(
            @PathVariable Long userId,
            @RequestBody @Valid CreateAccountRequest req
    ) {
        Account acct = accountService.createAccount(
                userId,
                req.getAccountNumber(),
                req.getInitialBalance()
        );
        return new AccountDto(acct.getId(), acct.getAccountNumber(), acct.getBalance());
    }

    @Operation(summary = "Получить все счета пользователя")
    @ApiResponse(responseCode = "200", description = "Список счетов")
    @GetMapping
    public List<AccountDto> getAccountsByUser(@PathVariable Long userId) {
        return accountService.getAccountsByUser(userId).stream()
                .map(a -> new AccountDto(a.getId(), a.getAccountNumber(), a.getBalance()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Пополнить счет")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Баланс успешно пополнен"),
            @ApiResponse(responseCode = "400", description = "Неверная сумма или ошибка запроса"),
            @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    @PostMapping("/{accountId}/deposit")
    public AccountDto deposit(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody @Valid AmountRequest req
    ) {
        Account acct = accountService.deposit(accountId, req.getAmount());
        return new AccountDto(acct.getId(), acct.getAccountNumber(), acct.getBalance());
    }

    @Operation(summary = "Снять средства со счета")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Средства успешно сняты"),
            @ApiResponse(responseCode = "400", description = "Недостаточно средств или ошибка запроса"),
            @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    @PostMapping("/{accountId}/withdraw")
    public AccountDto withdraw(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody @Valid AmountRequest req
    ) {
        Account acct = accountService.withdraw(accountId, req.getAmount());
        return new AccountDto(acct.getId(), acct.getAccountNumber(), acct.getBalance());
    }
}