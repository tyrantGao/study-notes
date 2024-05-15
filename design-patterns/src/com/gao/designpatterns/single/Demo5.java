package com.gao.designpatterns.single;

/**
 * 静态内部类单例模式
 * 特点：
 * 由静态的内部类的加载机制，既可以实现线程安全的静态内部类，又实现了懒加载
 * 静态内部类中的静态对象，只有在调用的时候才会实例化
 *
 * @author gaosicheng
 */
public class Demo5 {

    private Demo5() {
    }

    public static Demo5 getInstance() {
        return InsideClass.instance;
    }

    private static class InsideClass {
        private static Demo5 instance = new Demo5();
    }

}
