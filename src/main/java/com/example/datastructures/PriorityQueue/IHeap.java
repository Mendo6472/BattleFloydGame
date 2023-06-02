package com.example.datastructures.PriorityQueue;

import java.util.ArrayList;

public interface IHeap<K extends Comparable <K>, V> {
    public void minHeapify(int i);
    public void buildMinHeap();
    public void heapSort();
    public int getDad(int i);
    public int getLeft(int i);
    public int getRight(int i);
    public ArrayList<Node<K,V>> getHeap();
}
