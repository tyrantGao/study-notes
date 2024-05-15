package com.gao.designpatterns.single;


/**
 * 单例模式懒汉式（线程安全的）
 * 特点：
 * 优点：线程安全了
 * 缺点：效率降低了，因为加了同步锁
 *
 * @author gaosicheng
 */
public class Demo3 {

    public static Demo3 instance;

    private Demo3() {
    }

    public static synchronized Demo3 getInstance() {
        if (null == instance) {
            instance = new Demo3();
        }
        return instance;
    }

}
