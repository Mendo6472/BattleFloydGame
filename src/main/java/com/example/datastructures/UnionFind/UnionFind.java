package com.example.datastructures.UnionFind;

import java.util.HashMap;
import java.util.Map;


public class UnionFind<V> {
    Map<V, Node<V>> representatives = new HashMap<>();

    public void makeSet(V value){
        Set<V> newSet = new Set<>();

        newSet.setHead(new Node<V>());
        newSet.setTail(newSet.getHead());
        representatives.put(value, newSet.getHead());
        newSet.getHead().setValue(value);
        newSet.getHead().setSet(newSet);
        newSet.getHead().setNext(null);
    }

    public Set<V> find(V key){
        Node<V> node = representatives.get(key);
        return node.getSet();
    }

    public void union(Set<V> set1, Set<V> set2){
        Node<V> current = set2.getHead();
        while(current!=null){
            current.setSet(set1);
            current = current.getNext();
        }

        set1.getTail().setNext(set2.getHead());
        set1.setTail(set2.getTail());
        set2 = null;
    }

}
