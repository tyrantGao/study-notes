package com.gao.designpatterns.single;

/**
 * 单例模式-饿汉式
 * <p>
 * 在类加载得时候完成实例得初始化
 * 缺点：不支持延迟加载（没有使用也会加载）
 * 优点：获取实例得速度快
 *
 * @author tyrantGao
 */
public class Demo1 {

    // 2、在本类中创建一个全局得静态实例对象
    private static Demo1 INSTANCE = new Demo1();

    /**
     * 1、私有构造方法
     */
    private Demo1() {

    }

    // 3、提供一个公共的静态获取实例得方法
    public static Demo1 getInstance() {
        return INSTANCE;
    }

}
