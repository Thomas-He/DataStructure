package com.thomas.ds.stack;

import java.util.EmptyStackException;

public class MyStack<E> {
    private Node top;

    private class Node {
        E value;
        Node next;

        public Node(E value) {
            this.value = value;
        }

        public Node(E value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public void push(E e) {
        top = new Node(e, top);
    }

    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        E popValue = top.value;
        top = top.next;
        return popValue;
    }

    public E peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.value;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
