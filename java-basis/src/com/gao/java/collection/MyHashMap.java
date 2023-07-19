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


    /**
     * 默认初始化容量 16
     * 为什么直接用16，而是1 << 4?
     * 根据源码注释的解释，用 1 << 4这种形式，是为了提示初始化的容量必须是2的n次幂
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INIT_CAPACITY = 1 << 4;


    /**
     * 最大容量
     * 为什么是 1 << 30 而不是 1 << 31 ？
     * 1为10进制数， << 十进制数用2进制表示时，向左迁移 30表示迁移30位
     * int 类型是8个字节，换算2进制后为一个 32位二进制数，二进制中最高位为符号位（1为负，0为正）
     * 1 << 30 是正数，1 << 31 负数，所以不是1 << 31
     * 10000000 00000000 00000000 00000000 1 << 31 = -2147483648
     * 01000000 00000000 00000000 00000000 1 << 30 = 1073741824
     * (1 << 31) -1 为int类型最大值，为什么没有用int类型最大值，
     * 猜想应该是为了HashMap的性能原因导致java采用了一个折中的最大值
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认负载因子
     *
     * 为什么不用0.5或者1？
     * 为了平衡hash冲突的概率和空间利用率而采用一个折中值。
     * 负载因子决定了扩容阈值（threshold），所以当负载因子越小，空间利用率越低，负载因子越大，发生hash冲突的概率越大
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 负载因子，可以在创建map实例指定，如果没有指定，采用默认负载因子
     */
    final float loadFactor;

    /**
     * 数组（桶）的扩容阈值
     */
    int threshold;

    /**
     * 链表树化的阈值
     *
     * 为什么是8，而不是9或7 ？
     *
     * 7: 0.0000 0094 千万分之九十四
     * 8: 0.0000 0006 千万分之六
     * more：小于千万分之一
     *
     * 链表达到7的概率是8的大约16倍，之所以采用8而不是7，是为了减小树化的概率，之所以不用9而不用8，
     * 是因为链表长度达到8的概率已经很低了，如果采用9链表树化几乎不可能发生，违背了链表树化来提高查询效率的初衷
     *
     * 树节点的大小是正常节点的两倍，之所以采用8作为阈值而不是7，
     * 是为了减少链表树化的概率，1，可以节省空间，2可以省去树化过程，提高效率
     *
     * 在正常情况下，节点中列表的长度遵循泊松分布，链表长度能达到8以上的长度的概率是小于千万分之一的，当链表长度 >= 8时，
     * 说明发生hash冲突的概率是很高的。所以需要将链表转为红黑树，来提高查询效率
     *
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 红黑树解除树化的阈值
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 链表树化的最小数组（桶）长度
     *
     * 链表是否树化有两个要素，1、链表长度 >= 8 时，
     * 看数组长度是否达到 64，如果未达到，做数组扩容，而不是树化
     * 如果达到，则进行链表转红黑树
     */
    static final int MIN_TREEIFY_THRESHOLD = 64;

    /**
     * 节点数组
     * 在map中插入key，value时，会构造构造一个Node实例，根据key的hash值决定节点在数组中的位置
     */
    transient Node<K, V>[] table;

    /**
     * 存储的键值对的个数
     */
    transient int size;

    /**
     * modify Count
     * 当改变map的结构（put，remove等）时，修改统计++
     * 在集合迭代的过程中，结构发生变化，会抛出异常ConCurrentModificationException
     */
    transient int modCount;


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
        System.out.println(Integer.toString(Integer.MAX_VALUE,10));
        System.out.println(1 << 31);
        System.out.println(1 << 30);


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
