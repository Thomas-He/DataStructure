package com.thomas.ds.list;

import java.util.*;

/**
 * 线性表的顺序映像实现
 * @param <E> 泛型, 元素的类型
 * @author Thomas_He
 * @version v1.0
 */
public class MyArrayList<E> implements MyList<E>{
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    // 属性
    private E[] elements;
    private int size;
    private int modCount; //集合结构被修改的次数

    // 构造方法
    @SuppressWarnings("unchecked")
    public MyArrayList() {
        elements = (E[])new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public MyArrayList(int initialCapacity) {
        if (initialCapacity <= 0 || initialCapacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("initialCapacity = " + initialCapacity);
        }
        elements = (E[])new Object[initialCapacity];
    }

    /**
     * 在线性表的末尾添加元素
     * @param e 要添加的元素
     * @return 添加成功返回true, 否则返回false.
     */
    @Override
    public boolean add(E e) {
        add(size, e);
        return true;
    }

    /**
     * 在指定的索引位置添加元素
     * @param index 指定的索引
     * @param element 要添加的元素
     */
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if(size == elements.length) {
            // 扩容策略
            int minCapacity = size + 1;
            int newLength = calculateCapacity(minCapacity);
            // 扩容
            grow(newLength);
        }
        // 添加元素
        for (int i = size; i > index; i--) {
            elements[i] = elements[i-1];
        }
        elements[index] = element;
        size++;
        modCount++;
    }

    @SuppressWarnings("unchecked")
    private void grow(int newLength) {
        E[] newArr = (E[]) new Object[newLength];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        // 把elements指向新数组
        elements = newArr;
    }

    private int calculateCapacity(int minCapacity) {
        if (minCapacity > MAX_CAPACITY || minCapacity < 0) {
            throw new ArrayListOverflowException();
        }
        // 一定能够容纳下这么多元素
        int newLength = elements.length + (elements.length >> 1);
        if (newLength > MAX_CAPACITY || newLength < 0) {
            newLength = MAX_CAPACITY;
        }
        // 返回minCapacity和newLength的最大值。
        return newLength > minCapacity ? newLength : minCapacity;
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    /**
     * 清空数组中的所有元素
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        modCount++;
    }

    /**
     * 判断线性表中是否有和 o 相等的元素
     * @param o 指定的对象
     * @return 如果存在和 o 相等的元素，返回true，否则返回 false.
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * 获取指定索引位置的元素
     * @param index 指定的索引位置
     * @return 指定索引位置的元素
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    /**
     * 获取线性表中第一个和指定对象 o 相等元素的索引
     * @param o 指定对象
     * @return 第一个和指定对象 o 相等元素的索引，如果线性表中没有和o相等的元素，返回-1.
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * 获取线性表中最后一个和指定对象 o 相等元素的索引
     * @param o 指定对象
     * @return 最后一个和指定对象 o 相等元素的索引，如果线性表中没有和o相等的元素，返回-1.
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * 判断线性表是否有元素
     * @return 如果线性表中没有元素返回true, 否则返回false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 删除指定索引位置的元素
     * @param index 指定的索引位置
     * @return 被删除的元素
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        E removeValue = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        modCount++;
        return removeValue;
    }

    /**
     * 删除线性表中第一个和指定对象o相等的元素
     * @param o 指定对象
     * @return 如果删除成功返回true, 否则返回false.
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * 替换指定索引位置的元素
     * @param index 指定的索引位置
     * @param element 新的值
     * @return 被替换的值
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * 获取线性表中元素的个数
     * @return 线性表中元素的个数
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if(i != 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public MyIterator<E> iterator() {
        return new Itr();
    }

    @Override
    public MyIterator<E> iterator(int index) { // index:[0, size]
        checkIndexForAdd(index);
        return new Itr(index);
    }

    private class Itr implements MyIterator<E>{
        // 属性
        int cursor; // 光标后面元素的索引位置
        int lastRet = -1; // 最近返回元素的索引, -1表示最近返回元素。
        int expModCount = modCount;
        // 构造方法
        Itr() {
        }
        Itr(int index) {
            cursor = index;
        }

        /**
         * 在光标后面添加元素
         * @param e 待添加的元素
         */
        @Override
        public void add(E e) {
            // 判断迭代器是否还有效
            checkConModException();
            MyArrayList.this.add(cursor, e);
            expModCount = modCount;
            lastRet = -1;
            cursor++;
        }

        private void checkConModException() {
            if (expModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        /**
         * 判断后面是否还有元素
         * @return 如果有返回true, 否则返回false
         */
        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        /**
         * 判断前面是否还有元素
         * @return 如果有返回true, 否则返回false
         */
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        /**
         * 将光标往后移动一个位置，并返回被越过的元素
         * @return 被光标越过的元素
         */
        @Override
        public E next() {
            checkConModException();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return elements[cursor++];
        }

        /**
         * 获取光标后面元素的索引
         * @return 光标后面元素的索引
         */
        @Override
        public int nextIndex() {
            return cursor;
        }

        /**
         * 将光标往前移动一个位置，并将被越过的元素返回
         * @return 被光标越过的元素
         */
        @Override
        public E previous() {
            checkConModException();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastRet = --cursor;
            return elements[cursor];
        }

        /**
         * 返回光标前面元素的索引
         * @return 光标前面元素的索引
         */
        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        /**
         * 删除最近返回的元素
         */
        @Override
        public void remove() {
            checkConModException();
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            MyArrayList.this.remove(lastRet);
            expModCount = modCount;
            cursor = lastRet; // Caution!
            lastRet = -1;
        }

        /**
         * 将最近返回的元素替换成e.
         * @param e 新的值
         */
        @Override
        public void set(E e) {
            checkConModException();
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            elements[lastRet] = e;
            lastRet = -1;
        }
    }

    public static void main(String[] args) {

        MyList<String> list = new MyArrayList<>();
        list.add("hello");
        list.add("world");
        list.add("java");

        for(String s : list) {
            System.out.println(s);
        }
    }
}

