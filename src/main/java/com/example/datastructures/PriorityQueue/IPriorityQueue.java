package com.example.datastructures.PriorityQueue;

public interface IPriorityQueue<K extends Comparable <K>,V>{
    public Node<K,V> minimum();
    public void insert(K key, V value);
    public Node<K, V> extractMin();
    public void decreaseKey(int i, K key);
}
