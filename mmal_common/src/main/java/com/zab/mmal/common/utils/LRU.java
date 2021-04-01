package com.zab.mmal.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LRU
 * @Description TODO
 * @Author zab
 * @Date 2021/4/1 17:39
 * @Company: 中冶赛迪重庆信息技术有限公司
 * Version 1.0
 **/
public class LRU<K, V> {

    /**
     * 存储K和Node
     */
    private Map<K, Node> map;
    /**
     * 容量
     */
    private int capacity;
    /**
     * 头节点
     */
    private Node<K, V> head;
    /**
     * 尾节点，最少使用的节点
     */
    private Node<K, V> tail;

    public LRU(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>(capacity);
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    public void put(K key, V value) {
        Node<K, V> node = map.get(key);
        if (null == node) {
            if (map.size() >= capacity) {
                map.remove(tail.prev.key);
                removeTail();
            }

            node = new Node<>(key, value);
            map.put(key, node);
            addToHead(node);
        } else {
            node.value = value;
            moveToHead(node);
        }
    }

    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (null == node) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node<K, V> newNode) {
        newNode.prev = head;
        newNode.next = head.next;
        head.next.prev = newNode;
        head.next = newNode;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void removeTail() {
        removeNode(tail.prev);
    }

    class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> prev, next;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LRU<Integer, String> lru = new LRU<>(3);
        lru.put(1, "abcd");
        lru.put(2, "efg");
        lru.put(3, "hijk");
        System.out.println(lru.get(1));
        lru.put(4, "lmn");
        System.out.println(lru);
    }

}
