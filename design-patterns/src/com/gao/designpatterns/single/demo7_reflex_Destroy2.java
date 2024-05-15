package com.gao.designpatterns.single;

/**
 * 解决反射破坏单例模式的问题2
 *
 * @author gaosicheng
 */
public class demo7_reflex_Destroy2 {
    private demo7_reflex_Destroy2() {
        if (null != InsideClass.instance) {
            throw new RuntimeException("禁止非法访问");
        }
    }

    public static demo7_reflex_Destroy2 getInstance() {
        return InsideClass.instance;
    }

    private static class InsideClass {
        private static demo7_reflex_Destroy2 instance = new demo7_reflex_Destroy2();
    }

}
