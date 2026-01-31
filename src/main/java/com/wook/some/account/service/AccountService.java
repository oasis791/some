package com.wook.some.account.service;

import com.wook.some.account.entity.Account;
import com.wook.some.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountUnitService {
    private final AccountRepository accountRepository;

    @Transactional
    public Long createAccount(String accountName) {
        String generatedAccountNumber = generateAccountNumber();

        if (accountRepository.findByAccNum(generatedAccountNumber).isPresent()) {
            generatedAccountNumber = generateAccountNumber();
        }

        Account account = Account.builder()
                .accNum(generatedAccountNumber)
                .accName(accountName)
                .balance(0L)
                .accStat(Account.AccountStatus.ACTIVE)
                .build();

        return accountRepository.save(account).getAccountId();
    }

    @Transactional
    public Long closeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

        account.close();

        return account.getAccountId();
    }

    @Transactional
    public void deposit(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다."));
        account.deposit(amount);
    }

    @Transactional
    public void withdraw(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다."));
        account.withdraw(amount);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        int suffix = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return "100-10-" + suffix;
    }
}
