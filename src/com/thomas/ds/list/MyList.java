package com.thomas.ds.list;

public interface MyList<E> extends Iterable<E> {
    boolean add(E e);

    void add(int index, E element);

    void clear();

    boolean contains(Object o);

    E get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    boolean isEmpty();

    MyIterator<E> iterator();

    MyIterator<E> iterator(int index);

    E remove(int index);

    boolean remove(Object o);

    E set(int index, E element);

    int size();
}

