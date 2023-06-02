package com.example.datastructures.UnionFind;

class Set<V> {
    Node<V> head;
    Node<V> tail;

    public Node<V> getHead() {
        return head;
    }

    public void setHead(Node<V> head) {
        this.head = head;
    }

    public Node<V> getTail() {
        return tail;
    }

    public void setTail(Node<V> tail) {
        this.tail = tail;
    }
}
