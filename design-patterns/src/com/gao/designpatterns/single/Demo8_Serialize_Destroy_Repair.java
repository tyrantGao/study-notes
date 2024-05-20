package com.gao.designpatterns.single;

import java.io.Serializable;

/**
 * 解决序列化对单例模式的破坏
 *
 * @author gaosicheng
 */
public class Demo8_Serialize_Destroy_Repair implements Serializable {
    private Demo8_Serialize_Destroy_Repair() {
        if (null != InsideClass.instance) {
            throw new RuntimeException("禁止非法访问");
        }
    }

    public static Demo8_Serialize_Destroy_Repair getInstance() {
        return InsideClass.instance;
    }

    public Object readResolve() {
        return InsideClass.instance;
    }

    private static class InsideClass {
        private static Demo8_Serialize_Destroy_Repair instance = new Demo8_Serialize_Destroy_Repair();
    }

}
