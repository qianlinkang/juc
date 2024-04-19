package com.zigzag.cas;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qlk
 */
public class DecimalAccountCas implements DecimalAccount {
    AtomicReference<BigDecimal> ref;

    public DecimalAccountCas(BigDecimal balance) {
        this.ref = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return ref.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = ref.get();
            BigDecimal next = prev.subtract(amount);
            if (ref.compareAndSet(prev, next)) {
                break;
            }
        }
//        System.out.println(ref.get());
    }

    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }
}
