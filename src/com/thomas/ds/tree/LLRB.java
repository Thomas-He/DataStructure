package com.thomas.ds.tree;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/*
API:
    void put(K key, V value)
    V get(K key)
    boolean contains(K key)
    void delete(K key)
    void clear()
    boolean isEmpty()
    int size()
    Set<K> keys()
    --------------------------
    K min()
    K max()
    void deleteMin()
    void deleteMax()
    K floor(K key)
    K ceiling(K key)
    int rank(K key)
    K select(int k)
    int size(K low, K high)
    Set<K> keys(K low, K high)
 */
public class LLRB<K extends Comparable<? super K>, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    // 属性
    private TreeNode root;

    private class TreeNode {
        // 属性
        K key;
        V value;
        boolean color;
        TreeNode left;
        TreeNode right;
        int size;

        public TreeNode(K key, V value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    // V get(K key)
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        TreeNode x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    // boolean contains(K key)
    public boolean contains(K key) {
        return get(key) != null;
    }

    // 避免判断空指针
    private int size(TreeNode x) {
        if (x == null) return 0;
        return x.size;
    }

    // void put(K key, V value)
    public void put(K key, V val) {
        if (key == null || val == null)
            throw new IllegalArgumentException("Key or value cannot be null");
        root = put(root, key, val);
        root.color = BLACK;
        check();
    }

    private TreeNode put(TreeNode x, K key, V val) {
        if (x == null) return new TreeNode(key, val, RED, 1);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) x.value = val;
        else if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);

        // 自底向上修复非法的3-node和分解4-node.
        if (!isRed(x.left) && isRed(x.right))
            x = rotateLeft(x);
        if (isRed(x.left) && isRed(x.left.left)) // Caution: 短路原则
            x = rotateRight(x);
        if (isRed(x.left) && isRed(x.right))
            flipColors(x);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        if (!contains(key)) return ;
        if (!isRed(root.left)) root.color = RED;
        root = delete(root, key);
        root.color = BLACK;
        check();
    }

    private TreeNode delete(TreeNode x, K key) {
        if (key.compareTo(x.key) < 0) {
            if (!isRed(x.left) && !isRed(x.left.left)) {
                x = moveRedLeft(x);
            }
            x.left = delete(x.left, key);
        } else {
            if (isRed(x.left)) x = rotateRight(x);
            if (key.compareTo(x.key) == 0 && x.right == null)
                return null;
            if (!isRed(x.right) && !isRed(x.right.left))
                x = moveRedRight(x);
            if (key.compareTo(x.key) == 0) {
                TreeNode minOfRight = min(x.right);
                x.key = minOfRight.key;
                x.value = minOfRight.value;
                x.right = deleteMin(x.right);
            } else x.right = delete(x.right, key);
        }
        return fixUp(x);
    }


    // boolean isEmpty()
    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    public void clear() {
        root = null;
    }

    // Set<K> keys()
    public Set<K> keys() {
        if (isEmpty()) new LinkedHashSet<>();
        return keys(min(), max());
    }

    /*****************************************************************
     * Order Symbol table methods
     *****************************************************************/
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        root.color = RED;
        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
        // check();
    }

    private TreeNode deleteMax(TreeNode x) {
        if (isRed(x.left)) x = rotateRight(x);
        if (x.right == null) return null;
        // x.right is 2-node
        if (!isRed(x.right) && !isRed(x.right.left)) {
            x = moveRedRight(x);
        }
        x.right = deleteMax(x.right);
        return fixUp(x);
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        if (!isRed(root.left)) root.color = RED;
        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
    }

    private TreeNode deleteMin(TreeNode x) {
        if (x.left == null) return null;
        if (!isRed(x.left) && !isRed(x.left.left)) {
            x = moveRedLeft(x);
        }
        x.left = deleteMin(x.left);
        return fixUp(x);
    }

    public K min() {
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        return min(root).key;
    }

    private TreeNode min(TreeNode x) {
        TreeNode t = x;
        while (t.left != null) t = t.left;
        return t;
    }

    public K max() {
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        return max(root).key;
    }

    private TreeNode max(TreeNode x) {
        while (x.right != null) x = x.right;
        return x;
    }

    //  K floor(K key)
    public K floor(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        TreeNode t = floor(root, key);
        if (t != null) return t.key;
        return null;
    }

    private TreeNode floor(TreeNode x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        TreeNode t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }
    //  K ceiling(K key)
    public K ceiling(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (isEmpty()) throw new NoSuchElementException("The tree is EMPTY");
        TreeNode t = ceiling(root, key);
        if (t != null) return t.key;
        return null;
    }

    private TreeNode ceiling(TreeNode x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0) return ceiling(x.right, key);
        TreeNode t = ceiling(x.left, key);
        if (t != null) return t;
        else return x;
    }

    // K select(int k) 获取排名为k的元素(索引) [0, n-1]
    public K select(int k) {
        if (isEmpty()) throw new NoSuchElementException("The is EMPTY");
        if (k < 0 || k >= root.size)
            throw new IllegalArgumentException("k=" + k + ", size=" + root.size);
        return select(root, k).key;
    }

    private TreeNode select(TreeNode x, int k) {
        int size = size(x.left);
        if (k == size) return x;
        else if (k < size) return select(x.left, k);
        else return select(x.right, k - size - 1);
    }

    // int rank(K key) 小于key值的元素个数
    public int rank(K key) {
        if (key == null) throw new IllegalArgumentException("The tree is EMPTY");
        return rank(root, key);
    }

    private int rank(TreeNode x, K key) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return size(x.left);
        else if (cmp < 0) return rank(x.left, key);
        else return 1 + size(x.left) + rank(x.right, key);
    }

    // int size(K low, K high)  [low, high]之间的元素个数
    public int size(K lo, K hi) {
        if (lo == null || hi == null)
            throw new IllegalArgumentException("Low or high cannot be null");
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    // Set<K> keys(K lo, K hi)
    public Set<K> keys(K lo, K hi) {
        if (lo == null || hi == null)
            throw new IllegalArgumentException("Low or high cannot be null");
        Set<K> set = new LinkedHashSet<>();
        if (lo.compareTo(hi) > 0) return set;
        keys(root, set, lo, hi);
        return set;
    }

    private void keys(TreeNode x, Set<K> set, K lo, K hi) {
        if (x == null) return ;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, set, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) set.add(x.key);
        if (cmphi > 0) keys(x.right, set, lo, hi);
    }

    /*****************************************************************
     * helper
     *****************************************************************/
    private TreeNode rotateLeft(TreeNode h) {
        TreeNode x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private TreeNode rotateRight(TreeNode h) {
        TreeNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private void flipColors(TreeNode x) {
        x.color = !x.color;
        x.left.color = !x.left.color;
        x.right.color = !x.right.color;
    }

    private boolean isRed(TreeNode x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private TreeNode moveRedRight(TreeNode x) {
        flipColors(x);
        if (isRed(x.left.left)) {
            x = rotateRight(x);
            flipColors(x);
        }
        return x;
    }

    private TreeNode moveRedLeft(TreeNode x) {
        flipColors(x);
        if (isRed(x.right.left)) {
            x.right = rotateRight(x.right);
            x = rotateLeft(x);
            flipColors(x);
        }
        return x;
    }

    private TreeNode fixUp(TreeNode x) {
        if (isRed(x.right)) x = rotateLeft(x);
        if (isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left) && isRed(x.right)) flipColors(x);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /***********************************************************
     * check
     ***********************************************************/
    private boolean check() {
        boolean is23 = is23();
        if (!is23) System.err.println("Tree is not 23!");
        boolean isBalanced = isBalanced();
        if (!isBalanced) System.err.println("Tree is not balanced!");
        return is23 && isBalanced;
    }

    private boolean is23() {
        return !isRed(root) && is23(root);
    }

    private boolean is23(TreeNode x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (isRed(x.left) && isRed(x.left.left)) return false;
        return is23(x.left) && is23(x.right);
    }

    private boolean isBalanced() {
        int black = 0;
        TreeNode x = root;
        while(x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    private boolean isBalanced(TreeNode x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

    public static void main(String[] args) {
        String val = "VALUE";
        LLRB<Character, String> tree = new LLRB<>();
        /*System.out.println(tree.size());
        System.out.println(tree.isEmpty());*/
        tree.put('A', val);
        tree.put('S', val);
        tree.put('E', val);
        tree.put('C', val);
        tree.put('D', val);
        tree.put('I', val);
        tree.put('N', val);
        tree.put('B', val);
        tree.put('X', val);

        // Set<K> keys()
        // System.out.println(tree.keys());

        // Set<K> keys(K lo, K hi)
        // System.out.println(tree.keys('B', 'M'));

        // int size(K lo, K hi)
        /*System.out.println(tree.size('A', 'X'));
        System.out.println(tree.size('A', 'A'));
        System.out.println(tree.size('F', 'F'));*/

        // int rank(K key)
        /*System.out.println(tree.rank('D')); // 3
        System.out.println(tree.rank('F')); // 5
        System.out.println(tree.rank('0')); // 0
        System.out.println(tree.rank('a')); // 9*/

        // K select(int k)
        // System.out.println(tree.select(9));
        /*System.out.println(tree.select(0));
        System.out.println(tree.select(8));
        System.out.println(tree.select(3));*/

        // floor(key)
       /* System.out.println(tree.floor('F')); // E
        System.out.println(tree.floor('D')); // D
        System.out.println(tree.floor('0')); // null*/

        // ceiling(key)
        /*System.out.println(tree.ceiling('F')); // I
        System.out.println(tree.ceiling('D')); // D
        System.out.println(tree.ceiling('a')); // null*/

        // System.out.println(tree.check());
        /*System.out.println(tree.size());
        System.out.println(tree.isEmpty());
        tree.clear();
        System.out.println(tree.size());
        System.out.println(tree.isEmpty());*/

        // deleteMax()
        /*System.out.println(tree.contains('X'));
        tree.deleteMax();
        // System.out.println(tree.check());
        tree.check();
        System.out.println(tree.contains('X'));*/

        // deleteMin()
        /*System.out.println(tree.contains('A'));
        tree.deleteMin();
        tree.check();
        System.out.println(tree.contains('A'));*/

        // delete()
        /*System.out.println(tree.size());
        System.out.println(tree.contains('A'));
        System.out.println(tree.contains('S'));
        System.out.println(tree.contains('E'));
        tree.delete('A');
        tree.delete('S');
        tree.delete('E');
        System.out.println(tree.contains('A'));
        System.out.println(tree.contains('S'));
        System.out.println(tree.contains('E'));
        System.out.println(tree.size());
        System.out.println(tree.size());
        System.out.println(tree.contains('Y'));
        tree.delete('Y');
        System.out.println(tree.size());
        System.out.println(tree.contains('Y'));*/

        // min() max()
        /*System.out.println(tree.min());
        System.out.println(tree.max());
        tree.clear();
        System.out.println(tree.min());
        System.out.println(tree.max());*/
    }
}

