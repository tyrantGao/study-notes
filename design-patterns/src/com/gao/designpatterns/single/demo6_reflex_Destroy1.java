package com.gao.designpatterns.single;

/**
 * 反射对于单例模式的破坏1
 *
 * @author gaosicheng
 */
public class demo6_reflex_Destroy1 {

    public static volatile demo6_reflex_Destroy1 instance;

    private demo6_reflex_Destroy1() {
        if (null != instance) {
            throw new RuntimeException("禁止非法访问");
        }
    }

    public static demo6_reflex_Destroy1 getInstance() {
        // 1、使不为的线程直接返回对象，不在执行同步锁的代码块，来提高代码效率
        if (null == instance) {
            // 2、多个实例为空的线程同时进入的时候，是同步锁保证线程安全
            synchronized (demo6_reflex_Destroy1.class) {
                if (null == instance) {
                    /**
                     * JVM实例化对象的时候分为三步:
                     * 1、分配内存空间
                     * 2、初始化对象
                     * 3、将创建好的对象只想分配好的内存
                     *
                     * 在这实例化的三步中，JVM可能会进行指令重排，来减少寄存器的
                     * 存取次数，提高效率。例如先执行1，3，2尚未执行，
                     * 其他线程判断的对象是否为空的时候，对象誓不为空的，
                     * 但是这个对象还没有进初始化，导致获取道德对象是一个半成品
                     * 从而导致出现异常
                     *
                     * 解决办法：给对象加 volatile 关键字；
                     * volatile 关键字的作用：
                     * 1、保证变量的可见性
                     * 2、禁止指令重排（JVM创建对象的时候就会按照1，2，3的顺序
                     * 执行）
                     */
                    instance = new demo6_reflex_Destroy1();
                }
            }
        }
        return instance;
    }


}
