package com.zigzag.cas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * AtomicReference
 * 原子引用类型保护 BigDecimal
 * @author qlk
 */
public interface DecimalAccount {

    BigDecimal getBalance();

    void withdraw(BigDecimal amount);

    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
            }));
        }
        ts.forEach(Thread::start);

        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(account.getBalance());
    }
}
