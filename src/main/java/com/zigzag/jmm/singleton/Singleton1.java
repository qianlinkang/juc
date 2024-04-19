package com.zigzag.jmm.singleton;

import java.io.Serializable;

/**
 * 静态final变量实现单例
 *
 * @author qlk
 */
// 问题1：为什么加 final? --> 不希望有子类覆盖getInstance方法,导致单例被破坏
// 问题2：如果实现了序列化接口, 还要做什么来防止反序列化破坏单例? --> 添加readResolve方法,防止反序列化产生新的对象,破坏单例
public final class Singleton1 implements Serializable {

    // 问题3：为什么设置为私有? --> 防止其他人创建, 是否能防止反射创建新的实例? --> 不能
    private Singleton1() {
    }

    // 问题4：这样初始化是否能保证单例对象创建时的线程安全?  --> 可以, 静态final变量,jvm只会在类加载是调用一次初始化操作
    // 恶汉式,加载类时创建
    private static final Singleton1 INSTANCE = new Singleton1();

    // 问题5：为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由?
    // 更好的封装
    // 更好的控制创建这个对象的一些操作
    // 方法可以支持泛型
    public static Singleton1 getInstance() {
        return INSTANCE;
    }

    // 保证反序列化的对象仍然是单例的
    public Object readResolve() {
        return INSTANCE;
    }
}
