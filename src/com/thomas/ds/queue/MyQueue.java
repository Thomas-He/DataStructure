package com.thomas.ds.queue;

import java.util.NoSuchElementException;

public class MyQueue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;

    private int size;
    private E[] elements;
    private int front;
    private int rear;

    @SuppressWarnings("unchecked")
    public MyQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public MyQueue(int initialCapacity) {
        if (initialCapacity <= 0 || initialCapacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("initialCapacity=" + initialCapacity);
        }
        elements = (E[]) new Object[initialCapacity];
    }
    // API
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elements[front];
    }

    public void enqueue(E e) {
        if (size == elements.length) {
            // 扩容
            int newLength = calculateCapacity();
            grow(newLength);
        }
        elements[rear] = e;
        rear = (rear + 1) % elements.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void grow(int newLength) {
        E[] newArr = (E[]) new Object[newLength];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(front + i) % elements.length];
        }
        elements = newArr;
        front = 0;
        rear = size;
    }

    private int calculateCapacity() {
        if (elements.length == MAX_CAPACITY) {
            throw new ArrayStoreException("Array overflow");
        }
        int newLength = elements.length + (elements.length >> 1);
        if (newLength < 0 || newLength > MAX_CAPACITY) {
            newLength = MAX_CAPACITY;
        }
        return newLength;
    }

    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E removeValue = elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;
        return removeValue;
    }
}

