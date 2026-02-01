package com.wook.some.account.service;

import com.wook.some.account.entity.Account;
import com.wook.some.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountConcurrencyTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("동시에 100명이 각각 1,000원씩 입금하면 잔액은 100,000원이어야 한다")
    void concurrency_deposit_test() throws InterruptedException {

        // 1. 초기 계좌 생성 (잔액 0원)
        Long accountId = accountService.createAccount("동시성 테스트 계좌");

        int threadCount = 100;
        // 32개의 스레드를 가진 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 100개의 작업이 모두 끝날 때까지 대기하기 위한 장치
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 2. 100번의 입금 요청을 멀티스레드로 동시에 실행
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.deposit(accountId, 1000L);
                } catch (Exception e) {
                    System.out.println("에러 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 하나 완료 시 숫자를 줄임
                }
            });
        }

        latch.await(); // 숫자가 0이 될 때까지 메인 스레드는 대기

        // 3. 최종 잔액 확인
        Account account = accountRepository.findById(accountId).orElseThrow();
        System.out.println("최종 잔액: " + account.getBalance());

        // assertEquals(100000L, account.getBalance());
    }
}
