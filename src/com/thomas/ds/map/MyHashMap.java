package com.thomas.ds.map;

import java.util.LinkedHashSet;
import java.util.Set;

/*
API:
    void put(K key, V value)
    V get(K key)
    boolean contains(K key)
    void delete(K key)
    boolean isEmpty()
    int size()
    Set<K> keys()
 */
public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    // 属性
    private Entry<K, V>[] table;
    private int size;
    private double loadFactor;
    private int threshold;

    private static class Entry<K, V> {
        K key;
        V value;
        int hash;
        Entry next;
        public Entry(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    // 构造方法
    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity=" + initialCapacity);
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("loadFactor" + loadFactor);
        }
        int len = tableLength(initialCapacity);
        table = new Entry[len];
        this.loadFactor = loadFactor;
        threshold = (int) (table.length * loadFactor);
        threshold = threshold > initialCapacity ? threshold : initialCapacity;
    }

    private int tableLength(int capacity) {
        if (capacity >= MAX_CAPACITY) return MAX_CAPACITY;
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return  n + 1;
    }

    // void put(K key, V value)
    public V put(K key, V val) {
        if (key == null || val == null) {
            throw new IllegalArgumentException("Key or value can not be null");
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for(Entry<K, V> e = table[index]; e != null; e = e.next) {
            if ((hash == e.hash) && ((key == e.key) || key.equals(e.key))) {
                V oldValue = e.value;
                e.value = val;
                return oldValue;
            }
        }
        // 在头结点插入(key, val)
        addEntry(key, val, hash, index);
        return null;
    }

    private void addEntry(K key, V val, int hash, int index) {
        // 什么情况下需要扩容?
        if (size == threshold) {
            if (table.length == MAX_CAPACITY) {
                threshold = Integer.MAX_VALUE;
            } else {
                grow(table.length << 1);
                index = indexFor(hash, table.length);
            }
        }
        // 添加元素
        Entry<K, V> entryToAdd = new Entry<>(key, val, hash);
        entryToAdd.next = table[index];
        table[index] = entryToAdd;
        size++;
    }

    private void grow(int newLength) {
        Entry[] newTable = new Entry[newLength];
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> e = table[i];
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(e.hash, newLength);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
        table = newTable;
        threshold = (int) (loadFactor * table.length);
    }

    // V get(K key)
    public V get(K key) {
        if (key == null)
            throw new IllegalArgumentException("Key can not be null");
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for(Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (hash == e.hash && (key == e.key || key.equals(e.key))) {
                return e.value;
            }
        }
        return null;
    }

    // boolean contains(K key)
    public boolean contains(K key) {
        return get(key) != null;
    }

    // V delete(K key)
    public V delete(K key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for(Entry<K, V> e = table[index]; e != null; e = e.next) {
            Entry prev = null;
            if (hash == e.hash && (key == e.key || key.equals(e.key))) {
                if (prev == null) table[index] = e.next;
                else prev.next = e.next;
                size--;
                return e.value;
            }
            prev = e;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Set<K> keys()
    public Set<K> keys() {
        Set<K> keys = new LinkedHashSet<>();
        for(Entry<K, V> e : table) {
            while (e != null) {
                keys.add(e.key);
                e = e.next;
            }
        }
        return keys;
    }

    private int hash(K key) {
        int h = key.hashCode();
        return (h >>> 16) ^ (h << 16);
    }

    private int indexFor(int hash, int n) {
        return hash & (n - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for(Entry<K,V> e : table) {
            while (e != null) {
                sb.append(e).append(", ");
                e = e.next;
            }
        }
        if (!isEmpty()) sb.delete(sb.length() - 2, sb.length());
        return sb.append("}").toString();
    }
}

