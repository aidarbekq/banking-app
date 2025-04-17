package com.example.banking.controller;

import com.example.banking.dto.AccountDto;
import com.example.banking.entity.Account;
import com.example.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@PathVariable Long userId,
                                    @RequestParam String accountNumber,
                                    @RequestParam BigDecimal initialBalance) {
        Account account = accountService.createAccount(userId, accountNumber, initialBalance);
        return mapToDto(account);
    }

    @PostMapping("/{accountId}/deposit")
    public AccountDto deposit(@PathVariable Long accountId,
                              @RequestParam BigDecimal amount) {
        return mapToDto(accountService.deposit(accountId, amount));
    }

    @PostMapping("/{accountId}/withdraw")
    public AccountDto withdraw(@PathVariable Long accountId,
                               @RequestParam BigDecimal amount) {
        return mapToDto(accountService.withdraw(accountId, amount));
    }

    @GetMapping("/user/{userId}")
    public List<AccountDto> getAccountsByUser(@PathVariable Long userId) {
        return accountService.getAccountsByUser(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AccountDto mapToDto(Account account) {
        return new AccountDto(account.getId(), account.getAccountNumber(), account.getBalance());
    }
}
