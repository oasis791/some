package com.wook.some.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(length = 20, nullable = false, unique = true)
    private String accNum;

    @Column(length = 20, nullable = false)
    private String accName;

    @Column(length = 20, nullable = false)
    private Long balance;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AccountStatus accStat;

    public enum AccountStatus {
        ACTIVE,     // 정상
        SUSPENDED,  // 정지
        DORMANT,    // 휴면
        CLOSED      // 해지
    }

    public void deposit(Long amount) {
        if (amount < 0) throw new IllegalArgumentException("입금액은 0보다 커야합니다.");
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        if (amount > this.balance) throw new RuntimeException("잔액이 부족합니다.");
        this.balance -= amount;
    }

    public void close() {
        if (this.accStat == AccountStatus.CLOSED) {
            throw new IllegalArgumentException("이미 해지된 계좌입니다.");
        }

        if (this.balance > 0) {
            throw new IllegalArgumentException("잔액이 남아있는 계좌는 해지할 수 없습니다.");
        }

        this.accStat = AccountStatus.CLOSED;
    }
}
