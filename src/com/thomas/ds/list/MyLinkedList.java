package com.thomas.ds.list;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements MyList<E> {
    // 属性
    private int size;
    private Node head; // dummy node
    private Node end; // dummy node
    private int modCount;

    // 构造方法
    public MyLinkedList() {
        head = new Node(null);
        end = new Node(null, head, null);
        head.next = end;
    }

    private class Node {
        E value;
        Node prev;
        Node next;

        Node(E value) {
            this.value = value;
        }

        public Node(E value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * 在线性表末尾添加元素
     *
     * @param e 待添加的元素
     * @return 如果添加成功返回true, 否则返回false.
     */
    @Override
    public boolean add(E e) {
        add(size, e);
        return true;
    }

    /**
     * 在指定的索引位置添加元素
     *
     * @param index   指定的索引位置
     * @param element 待添加的元素
     */
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        if (index == size) {
            Node nodeToAdd = new Node(element, end.prev, end);
            end.prev.next = nodeToAdd;
            end.prev = nodeToAdd;
        } else {
            Node node = getNode(index);
            Node nodeToAdd = new Node(element, node.prev, node);
            node.prev.next = nodeToAdd;
            node.prev = nodeToAdd;
        }
        size++;
        modCount++;
    }

    private Node getNode(int index) {
        if (2 * index < size) {
            Node x = head.next;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node x = end.prev;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    /**
     * 清空所有元素
     */
    @Override
    public void clear() {
        head.next = end;
        end.prev = head;
        size = 0;
    }

    /**
     * 判断线性表中是否有和指定对象o相等的元素
     *
     * @param o 指定对象
     * @return 如果有元素和o相等，返回true, 否则返回false
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * 获取指定索引位置的元素
     *
     * @param index 指定索引位置
     * @return 指定索引位置的元素
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).value;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    /**
     * 获取线性表中第一个和指定对象o相等元素的索引
     *
     * @param o 指定对象
     * @return 线性表中第一个和指定对象o相等元素的索引
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            Node x = head.next;
            for (int i = 0; i < size; i++) {
                if (x.value == null) return i;
                x = x.next;
            }
        } else {
            Node x = head.next;
            for (int i = 0; i < size; i++) {
                if (o.equals(x.value)) return i;
                x = x.next;
            }
        }
        return -1;
    }

    /**
     * 获取线性表中最后一个和指定对象o相等元素的索引
     *
     * @param o 指定对象
     * @return 线性表中最后一个和指定对象o相等元素的索引
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            Node x = end.prev;
            for (int i = size - 1; i >= 0; i--) {
                if (x.value == null) return i;
                x = x.prev;
            }
        } else {
            Node x = end.prev;
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(x.value)) return i;
                x = x.prev;
            }
        }
        return -1;
    }

    /**
     * 判断是否为空线性表
     *
     * @return 如果线性表为空，返回true, 否则返回false.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 删除指定索引位置的元素
     *
     * @param index 指定的索引位置
     * @return 被删除的元素
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        Node node = getNode(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        modCount++;
        return node.value;
    }

    /**
     * 删除线性表中第一个和指定对象o相等的元素。
     *
     * @param o 指定对象
     * @return 如果删除成功返回 true, 否则返回 false.
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            Node x = head.next;
            for (int i = 0; i < size; i++) {
                if (x.value == null) {
                    x.prev.next = x.next;
                    x.next.prev = x.prev;
                    size--;
                    modCount++;
                    return true;
                }
                x = x.next;
            }
        } else {
            Node x = head.next;
            for (int i = 0; i < size; i++) {
                if (o.equals(x.value)) {
                    x.prev.next = x.next;
                    x.next.prev = x.prev;
                    modCount++;
                    size--;
                    return true;
                }
                x = x.next;
            }
        }
        return false;
    }

    /**
     * 替换指定索引位置的元素
     *
     * @param index   指定索引位置
     * @param element 新的值
     * @return 返回旧的值
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node node = getNode(index);
        E oldValue = node.value;
        node.value = element;
        return oldValue;
    }

    /**
     * 获取线性表中元素的个数
     *
     * @return 线性表中元素的个数
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node x = head.next;
        for (int i = 0; i < size; i++) {
            sb.append(x.value).append(", ");
            x = x.next;
        }
        if (size != 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.append("]").toString();
    }

    @Override
    public MyIterator<E> iterator() {
        return new Itr();
    }

    @Override
    public MyIterator<E> iterator(int index) {
        checkIndexForAdd(index);
        return new Itr(index);
    }

    private class Itr implements MyIterator<E> {
        // 属性
        private int cursor;
        private int expModCount = modCount;
        Node lastRet;
        Node node; // 光标后面的结点

        Itr() {
            node = head.next;
        }

        Itr(int index) {
            cursor = index;
            node = MyLinkedList.this.getNode(index);
        }

        /**
         * 在光标后面添加元素
         *
         * @param e 待添加的元素
         */
        @Override
        public void add(E e) {
            checkConModException();
            Node nodeToAdd = new Node(e, node.prev, node);
            node.prev.next = nodeToAdd;
            node.prev = nodeToAdd;
            size++;
            expModCount = ++modCount;
            lastRet = null;
            cursor++;
        }

        private void checkConModException() {
            if (expModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        /**
         * 判断光标后面是否还有元素
         *
         * @return
         */
        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        /**
         * 判断光标前面是否有元素
         *
         * @return
         */
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        /**
         * 将光标往后移动一个位置，并将光标越过的元素返回
         *
         * @return 光标越过的元素
         */
        @Override
        public E next() {
            checkConModException();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = node;
            cursor++;
            node = node.next;
            return lastRet.value;
        }

        /**
         * 获取光标后面元素的索引
         *
         * @return 光标后面元素的索引
         */
        @Override
        public int nextIndex() {
            return cursor;
        }

        /**
         * 将光标往前移动一个位置，并将被光标越过的元素返回。
         *
         * @return 光标越过的元素
         */
        @Override
        public E previous() {
            checkConModException();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            cursor--;
            node = node.prev;
            lastRet = node;
            return lastRet.value;
        }

        /**
         * 获取光标前面元素的索引
         *
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
            if (lastRet == null) {
                throw new IllegalStateException();
            }
            lastRet.prev.next = lastRet.next;
            lastRet.next.prev = lastRet.prev;
            size--;
            expModCount = ++modCount;
            if (lastRet == node) {
                node = node.next;
            } else {
                cursor--;
            }
            lastRet = null;
        }

        /**
         * 替换最近返回的元素
         * @param e 新的值
         */
        @Override
        public void set(E e) {
            checkConModException();
            if (lastRet == null) {
                throw new IllegalStateException();
            }
            lastRet.value = e;
            lastRet = null;
        }
    }

    public static void main(String[] args) {
        MyList<String> list = new MyLinkedList<>();
        list.add("hello");
        list.add("world");
        list.add("java");
        for (String item : list) {
            System.out.println(item);
        }
        System.out.println(list.size());
    }

}

