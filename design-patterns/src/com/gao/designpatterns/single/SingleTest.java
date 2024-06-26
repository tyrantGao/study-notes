package com.gao.designpatterns.single;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Constructor;

public class SingleTest {

    /**
     * 测试饿汉式是否实现了单例
     */
    @Test
    public void test1() {
        Demo1 demo11 = Demo1.getInstance();
        Demo1 demo12 = Demo1.getInstance();
        System.out.println(demo12 == demo11);
    }

    /**
     * 测试懒汉式是否实现了单例
     */
    @Test
    public void test2() {
        Demo2 demo21 = Demo2.getInstance();
        Demo2 demo22 = Demo2.getInstance();
        System.out.println(demo22 == demo21);
    }

    /**
     * 测试懒汉式在多线程环境中是否还能是单例
     */
    @Test
    public void test3() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Demo2.getInstance());
            }).start();
        }
    }

    /**
     * 测试饿汉式在多线程环境中是否还能是单例
     */
    @Test
    public void test4() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                System.out.println(Demo1.getInstance());
            }).start();
        }
    }

    /**
     * 测试同步懒汉式在多线程环境中是否还能是单例
     */
    @Test
    public void test5() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Demo3.getInstance());
            }).start();
        }
    }

    /**
     * 测试双重检索单例模式在多线程环境中是否还能是单例
     */
    @Test
    public void test6() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                System.out.println(Demo4.getInstance());
            }).start();
        }
    }

    /**
     * 测试反射是否会破坏单例模式
     */
    @Test
    public void test7() throws Exception {
        Class<demo6_reflex_Destroy1> demo6Class = demo6_reflex_Destroy1.class;
        Constructor<demo6_reflex_Destroy1> declaredConstructor = demo6Class.getDeclaredConstructor();
        // 将此对象的可访问标志设置为指示的布尔值。
        // 值true表示反射对象在使用时应禁止Java语言访问检查。
        // 值false表示反射的对象应该强制执行Java语言访问检查。
        declaredConstructor.setAccessible(true);
        demo6_reflex_Destroy1 instance1 = declaredConstructor.newInstance();
        demo6_reflex_Destroy1 instance2 = declaredConstructor.newInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 有效处理
     * <p>
     * 在构造方法中处理过后，反射创建对象是否还能执行
     * (静态内部类实现的单例）
     */
    @Test
    public void test8() throws Exception {
        Class<demo7_reflex_Destroy2> demo7Class = demo7_reflex_Destroy2.class;
        Constructor<demo7_reflex_Destroy2> declaredConstructor = demo7Class.getDeclaredConstructor();
        // 将此对象的可访问标志设置为指示的布尔值。
        // 值true表示反射对象在使用时应禁止Java语言访问检查。
        // 值false表示反射的对象应该强制执行Java语言访问检查。
        declaredConstructor.setAccessible(true);
        demo7_reflex_Destroy2 instance1 = declaredConstructor.newInstance();
        demo7_reflex_Destroy2 instance2 = declaredConstructor.newInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 无效处理
     * <p>
     * 在构造方法中处理过后，反射创建对象是否还能执行
     * (双重检查实现的单例，不能阻止反射对单例模式的破坏）
     *
     * @throws Exception
     */
    @Test
    public void test8_2() throws Exception {
        Class<demo6_reflex_Destroy1> Demo6Class = demo6_reflex_Destroy1.class;
        Constructor<demo6_reflex_Destroy1> declaredConstructor = Demo6Class.getDeclaredConstructor();
        // 将此对象的可访问标志设置为指示的布尔值。
        // 值true表示反射对象在使用时应禁止Java语言访问检查。
        // 值false表示反射的对象应该强制执行Java语言访问检查。
        declaredConstructor.setAccessible(true);
        demo6_reflex_Destroy1 instance1 = declaredConstructor.newInstance();
        demo6_reflex_Destroy1 instance2 = declaredConstructor.newInstance();
        System.out.println(instance1 == instance2);
    }

    /**
     * 单例模式是否还正常
     * 静态内部类方式的单例模式可以通过在构造类中处理来阻止反射对于实例化的破坏
     *
     * @throws Exception
     */
    @Test
    public void test9() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                demo7_reflex_Destroy2 instance = demo7_reflex_Destroy2.getInstance();
                System.out.println(instance);
            }).start();
        }
    }

    /**
     * 序列化对于单例模式的破坏
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void test10() throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("a.txt"));
        Demo8_Serialize_Destroy1 instance1 = Demo8_Serialize_Destroy1.getInstance();
        oos.writeObject(instance1);
        oos.flush();
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("a.txt"));
        Demo8_Serialize_Destroy1 instance2 = (Demo8_Serialize_Destroy1) ois.readObject();
        System.out.println(instance2 == instance1);
    }

    /**
     * 解决序列化对于单例模式的破坏（在反序列化类中实现readResolve方法）
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void test11() throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("a.txt"));
        Demo8_Serialize_Destroy_Repair instance1 = Demo8_Serialize_Destroy_Repair.getInstance();
        oos.writeObject(instance1);
        oos.flush();
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("a.txt"));
        Demo8_Serialize_Destroy_Repair instance2 = (Demo8_Serialize_Destroy_Repair) ois.readObject();
        System.out.println(instance2 == instance1);
    }
}
