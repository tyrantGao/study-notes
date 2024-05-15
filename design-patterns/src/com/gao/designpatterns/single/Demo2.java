package com.gao.designpatterns.single;

/**
 * 懒汉式单例
 * <p>
 * 特点：支持在使用的时候加载（延迟加载）
 * 缺点：在多线程环境中，会出现线程不安全的问题（不再是单例，有多个实例）
 *
 * @author tyrantGao
 * @date 2023/07/12
 */
public class Demo2 {

    private static Demo2 instance;

    private Demo2() {
    }

    public static Demo2 getInstance() {
        if (null == instance) {
            instance = new Demo2();
        }
        return instance;
    }

}
