package com.zigzag.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 反射获取Unsafe
 *
 * @author qlk
 */
public class UnsafeAccessor {

    static Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException| IllegalAccessException e) {
            throw new Error(e);
        }
    }

    static Unsafe getUnsafe() {
        return unsafe;
    }

    public static void main(String[] args) {
        System.out.println(UnsafeAccessor.getUnsafe());
    }
}
