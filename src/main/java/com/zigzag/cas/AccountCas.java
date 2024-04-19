package com.zigzag.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qlk
 */
public class AccountCas implements Account {

    private AtomicInteger balance;

    public AccountCas(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
//        while (true) {
//            // 取一次当前最新值
//            int prev = balance.get();
//            // 正常做减法
//            int next = prev - amount;
//            // 真正修改, compareAndSet
//            // compareAndSet返回true,修改成功,退出循环
//            // 返回false,进入while,重试
//            if (balance.compareAndSet(prev, next)) {
//                break;
//            }
//        }

        // 以上可以简化为
        // 先取款在取值,原子操作
        balance.addAndGet(-1 * amount);
    }

    public static void main(String[] args) {
        Account.demo(new AccountCas(10000));
    }
}
