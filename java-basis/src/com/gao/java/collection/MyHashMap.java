package com.gao.java.collection;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 自己手写map类
 *
 * @author tyrantGao
 * @date 2023/07/12
 */
public class MyHashMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

    private static long serialVersionUID = 1L;


    static final int DEFAULT_INIT_CAPACITY = 1 << 4;


    /**
     * 最大容量
     * 为什么是 1 << 30 而不是 1 << 31 ？
     * 1为10进制数， << 十进制数用2进制表示时向左迁移 30表示迁移30位
     * int 类型是8个字节，换算2进制后为一个 32位二进制数，二进制中最高位为符号位（1为负，0为正）
     * 1 << 30 位已经迁移到31位了，如果 1 << 31就迁移到符号位了，所以是 1 << 30,而不是 1 << 31
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final int TREEIFY_THRESHOLD = 8;

    static final int UNTREEIFY_THRESHOLD = 6;

    static final int MIN_TREEIFY_THRESHOLD = 64;

    transient Node<K, V>[] table;

    transient int size;

    transient int modCount;

    int threshold;

    final float loadFactor;


    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(int initCapacity) {
        this(initCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initCapacity, float loadFactor) {
        if (initCapacity < 0) {
            throw new IllegalArgumentException("初始化容量参数：" + initCapacity);
        }
        if (initCapacity > MAXIMUM_CAPACITY) {
            initCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("负载因子：" + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initCapacity);
    }

    /**
     * hashMap中数组容量计算
     * 1、为什么到 >>> 16位？
     * int 是4个字节，32位，右移16位后刚好高（16）低（16）位做 或（|）运算，得到最多的1；
     * 2、为什么要cappacity - 1？
     * 为了指定容量本身就是2的n次幂数字时，就用它本身数值，而不是一个大于它且最小的2的n次幂
     * 举例：8如果不 -1，则得到的是 16，当使用 8 -1 时，则为 8
     *
     * @param capacity 指定容量
     * @return int 大于指定容量且最小的2的n次幂数
     */
    static final int tableSizeFor(int capacity) {
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public MyHashMap(Map<K, V> map) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        // TODO
    }

    @Override
    public V put(K k, V val) {

        return null;
    }

    static class Node<K, V> implements Map.Entry<K, V> {

        final int hash;

        final K key;

        V value;

        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            return null;
        }

    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        super.replaceAll(function);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return super.replace(key, value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }
}

class Main {
    public static void main(String[] args) {
//        System.out.println(MyHashMap.tableSizeFor(8));
        HashMap<String,Integer> hashMap = new HashMap<>(1 << 30);
        for (int i = 0; i < (1 << 31) -1; i++) {
            hashMap.put("第"+i+"个参数",i);
        }



    }

    public static int getNumPower(int num) {
        int result = 0;
        while (num % 2 == 0) {
            num /= 2;
            result++;
        }
        return result;
    }

}
