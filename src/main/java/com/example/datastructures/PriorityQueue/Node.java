package com.example.datastructures.PriorityQueue;

public class Node<K extends Comparable <K>,V> {
    private V value;
    private K key;
    private Node<K, V> previous;
    private Node<K, V> next;

    public Node(K key, V value) {
        this.value = value;
        this.key = key;
        this.previous = null;
        this.next = null;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Node<K, V> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<K, V> previous) {
        this.previous = previous;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }
}
