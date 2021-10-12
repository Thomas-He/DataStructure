package com.thomas.ds.list;

import java.util.Iterator;

public interface MyIterator<E> extends Iterator {

    void add(E e);

    boolean hasNext();

    boolean hasPrevious();

    E next();

    int nextIndex();

    E previous();

    int previousIndex();

    void remove();

    void set(E e);
}
