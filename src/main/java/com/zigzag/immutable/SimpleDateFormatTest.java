package com.zigzag.immutable;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * SimpleDateFormat不是线程安全的
 *
 * @author qlk
 */
@Slf4j(topic = "c.SimpleDateFormatTest")
public class SimpleDateFormatTest {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    synchronized (sdf) {
                        log.debug("{}", sdf.parse("1951-04-21"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
