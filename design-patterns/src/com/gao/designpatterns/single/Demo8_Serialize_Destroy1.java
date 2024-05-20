package com.gao.designpatterns.single;

import java.io.Serializable;

/**
 * 序列话对单例模式的破坏
 *
 * @author gaosicheng
 */
public class Demo8_Serialize_Destroy1 implements Serializable {
    private Demo8_Serialize_Destroy1() {
        if (null != InsideClass.instance) {
            throw new RuntimeException("禁止非法访问");
        }
    }

    public static Demo8_Serialize_Destroy1 getInstance() {
        return InsideClass.instance;
    }

    private static class InsideClass {
        private static Demo8_Serialize_Destroy1 instance = new Demo8_Serialize_Destroy1();
    }

}
