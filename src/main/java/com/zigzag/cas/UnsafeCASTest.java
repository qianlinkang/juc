package com.zigzag.cas;

import lombok.Data;
import sun.misc.Unsafe;

/**
 * @author qlk
 */
public class UnsafeCASTest {
    public static void main(String[] args) throws NoSuchFieldException {

        Unsafe unsafe = UnsafeAccessor.getUnsafe();

        // 获取成员变量的偏移量
        long idOffset = unsafe.objectFieldOffset(Student.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Student.class.getDeclaredField("name"));

        Student student = new Student();
        // 使用cas方法替换成员变量的值

        unsafe.compareAndSwapInt(student, idOffset, 0, 20); // 返回true
        unsafe.compareAndSwapObject(student, nameOffset, null, "张三"); // 返回true

        System.out.println(student);
    }
}


@Data
class Student {
    volatile int id;
    volatile String name;
}