package com.example.datastructures.UnionFind;

public class Node<V> {
    V value;
    Node<V> next;
    Set<V> set;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Node<V> getNext() {
        return next;
    }

    public void setNext(Node<V> next) {
        this.next = next;
    }

    public Set<V> getSet() {
        return set;
    }

    public void setSet(Set<V> set) {
        this.set = set;
    }
}
