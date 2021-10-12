package com.thomas.ds.map;

/*
API:
    void set(int offset)
    void clear(int offset)
    boolean test(int offset)
    void clearAll()
    int size()
 */
public class MyBitMap {
    private static final int BITS_PER_WORD = 32;
    private static final int BYTES_PER_WORD = 4;

    private int size;
    private int[] data;

    public MyBitMap(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("capacity must be positive: " + capacity);
        size = capacity;
        // nofWords: number of words
        int nofWords = sizeInWords();
        data = new int[nofWords];
    }

    private int sizeInWords() {
        return (size + BITS_PER_WORD - 1) / BITS_PER_WORD;
    }

    public int size() {
        return size;
    }

    public void set(int offset) {
        if (offset < 0 || offset >= size) {
            throw new IndexOutOfBoundsException("size=" + size + ", offset=" + offset);
        }
        int index = offset / BITS_PER_WORD;
        int pos = offset % BITS_PER_WORD;
        // bitSet(index, pos);
    }
}
