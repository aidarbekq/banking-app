package com.example.banking.service;

import com.example.banking.entity.Account;
import com.example.banking.entity.User;
import com.example.banking.exception.ResourceNotFoundException;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, userRepository);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createAccount_shouldCreateAccountSuccessfully() {
        Long userId = 1L;
        User user = new User(userId, "John", "john@example.com", LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        accountService.createAccount(userId, "ACC999", BigDecimal.valueOf(500));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account saved = captor.getValue();
        assertThat(saved.getAccountNumber()).isEqualTo("ACC999");
        assertThat(saved.getBalance()).isEqualByComparingTo("500.00");
        assertThat(saved.getUser()).isEqualTo(user);
    }

    @Test
    void createAccount_shouldThrowExceptionIfUserNotFound() {
        Long userId = 42L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                accountService.createAccount(userId, "ACC001", BigDecimal.ZERO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь не найден");
    }

    @Test
    void deposit_shouldAddAmountToAccount() {
        Account account = new Account(1L, "ACC777", new BigDecimal("100.00"), new User());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account updated = accountService.deposit(1L, new BigDecimal("50.00"));

        assertThat(updated.getBalance()).isEqualByComparingTo("150.00");
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_shouldSubtractAmountFromAccount() {
        Account account = new Account(1L, "ACC888", new BigDecimal("200.00"), new User());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account updated = accountService.withdraw(1L, new BigDecimal("75.00"));

        assertThat(updated.getBalance()).isEqualByComparingTo("125.00");
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_shouldThrowIfBalanceInsufficient() {
        Account account = new Account(1L, "ACC444", new BigDecimal("20.00"), new User());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.withdraw(1L, new BigDecimal("100.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недостаточно средств для списания");
    }

    @Test
    void getAccountsByUser_shouldReturnList() {
        Long userId = 10L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        List<Account> accounts = List.of(
                new Account("ACC101", BigDecimal.TEN, new User()),
                new Account("ACC102", BigDecimal.ONE, new User())
        );
        when(accountRepository.findByUserId(userId)).thenReturn(accounts);

        List<Account> result = accountService.getAccountsByUser(userId);

        assertThat(result).hasSize(2);
    }
}
