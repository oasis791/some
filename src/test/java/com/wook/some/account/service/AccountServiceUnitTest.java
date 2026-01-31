package com.wook.some.account.service;

import com.wook.some.account.entity.Account;
import com.wook.some.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("입금 서비스 흐름 테스트")
    void deposit_service_test() {
        // given
        Account account = Account.builder()
                .accountId(1L)
                .balance(1000L)
                .build();
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));

        // when
        accountService.deposit(1L, 500L);

        // then
        assertEquals(1500L, account.getBalance());
        verify(accountRepository, times(1)).findById(1L);
    }
}
