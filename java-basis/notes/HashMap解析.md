1、hashMap的属性解析

```java
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
```

