package com.zigzag.immutable;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author qlk
 */
@Slf4j(topic = "c.DateTimeFormatterTest")
public class DateTimeFormatterTest {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log.debug("{}", dtf.parse("2018-10-01", LocalDate::from));
            }).start();
        }
    }
}
