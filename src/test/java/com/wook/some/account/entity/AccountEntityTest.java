package com.wook.some.account.entity;

import com.wook.some.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountEntityTest {

    @Test
    @DisplayName("입금 성공 테스트")
    void deposit_success() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when
        account.deposit(500L);

        // then
        assertEquals(1500L, account.getBalance());
    }

    @Test
    @DisplayName("출금 성공 테스트")
    void withdraw_success() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when
        account.withdraw(500L);

        // then
        assertEquals(500L, account.getBalance());
    }

    @Test
    @DisplayName("입금 실패 - 입금액이 0원")
    void deposit_fail_deposit_amount_zero() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0L));
    }

    @Test
    @DisplayName("출금 실패 - 출금 금액이 잔액보다 더 큼")
    void withdraw_fail_balance_smaller_than_amount() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when & then
        assertThrows(RuntimeException.class, () -> account.withdraw(5000L));
    }
}
