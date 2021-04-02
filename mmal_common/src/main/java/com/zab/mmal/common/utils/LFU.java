package com.zab.mmal.common.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LFU
 * @Description TODO
 * @Author zab
 * @Date 2021/4/1 18:14
 * @Company: 中冶赛迪重庆信息技术有限公司
 * Version 1.0
 **/
public class LFU<K, V> {

    private int capacity;

    private Map<K, V> map;

    private Map<K, UseRate> rateMap;

    public LFU(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>(capacity);
        rateMap = new HashMap<>(capacity);
    }

    public void put(K key, V value) {
        V v = map.get(key);
        if (null == v) {
            if (map.size() >= capacity) {
                removeItem();
            }
            rateMap.put(key, new UseRate(key, 1, System.nanoTime()));
        } else {
            addItem(key);
        }

        map.put(key, value);
    }

    public V get(K key) {
        V v = map.get(key);
        if (null == v) {
            return null;
        }
        addItem(key);
        return v;
    }

    private void addItem(K key) {
        UseRate useRate = rateMap.get(key);
        useRate.count = useRate.count + 1;
        useRate.lastTime = System.nanoTime();
    }

    private void removeItem() {
        UseRate useRate = Collections.min(rateMap.values());
        map.remove(useRate.key);
        rateMap.remove(useRate.key);
    }

    class UseRate implements Comparable<UseRate> {
        private K key;
        private int count;
        private long lastTime;

        public UseRate(K key, int count, long lastTime) {
            this.key = key;
            this.count = count;
            this.lastTime = lastTime;
        }

        @Override
        public int compareTo(UseRate o) {
            int compare = Integer.compare(this.count, o.count);
            return compare == 0 ? Long.compare(this.lastTime, o.lastTime) : compare;
        }
    }

    public static void main(String[] args) {
        LFU<Integer, Integer> lfu = new LFU<>(3);
        lfu.put(1, 1);
        lfu.put(2, 2);
        lfu.put(3, 3);
        lfu.get(1);
        lfu.get(2);
        lfu.get(2);
        lfu.get(1);
        lfu.get(3);
        lfu.put(4, 4);
        System.out.println(lfu);
    }

}
