package com.example.datastructures.PriorityQueue;

import java.util.ArrayList;

public class Heap<K extends Comparable <K>, V> implements IHeap<K, V>, IPriorityQueue<K, V> {
    int heapSize;
    ArrayList<Node<K,V>> arr;

    public Heap(){
        arr = new ArrayList<>();
    }

    @Override
    public void minHeapify(int i) {
        int largest = i;
        int l = getLeft(i);
        int r = getRight(i);
        if (l < heapSize && arr.get(l).getKey().compareTo(arr.get(i).getKey()) < 0){
            largest = l;
        }
        if (r < heapSize && arr.get(r).getKey().compareTo(arr.get(largest).getKey()) < 0){
            largest = r;
        }
        if (largest != i){
            Node<K, V> temp = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, temp);
            minHeapify(largest);
        }
    }

    @Override
    public void buildMinHeap() {
        heapSize = arr.size();
        for (int i = (arr.size() / 2) - 1; i >= 0; i--) {
            minHeapify(i);
        }
    }

    @Override
    public void heapSort() {
        buildMinHeap();
        for (int i = arr.size() - 1; i >= 1; i--) {
            Node<K, V> temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
            heapSize -= 1;
            minHeapify(0);
        }
    }

    public void showHeap(){
        for (Node<K,V> node : arr) {
            System.out.print(node.getValue() + " ");
        }
        System.out.println();
    }

    @Override
    public int getDad(int i) {
        i += 1;
        if(i == 0){
            return i;
        }else{
            return (i/2) - 1;
        }
    }

    @Override
    public int getLeft(int i) {
        i += 1;
        return (i*2) - 1;
    }

    @Override
    public int getRight(int i) {
        i += 1;
        return i*2;
    }

    @Override
    public ArrayList<Node<K,V>> getHeap(){
        return arr;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(int newHeapSize) {
        this.heapSize = newHeapSize;
    }

    @Override
    public Node<K, V> minimum() {
        return arr.get(0);
    }

    @Override
    public void insert(K key, V value) {
      heapSize += 1;
      arr.add(new Node<K, V>(key, value));  
      int currentIndex = heapSize - 1;
      int parentIndex = getDad(currentIndex);

      while (currentIndex > 0 && arr.get(currentIndex).getKey().compareTo(arr.get(parentIndex).getKey()) < 0) {
        Node<K, V> temp = arr.get(currentIndex);
        arr.set(currentIndex, arr.get(parentIndex));
        arr.set(parentIndex, temp);
        currentIndex = parentIndex;
        parentIndex = getDad(currentIndex);
      }
    }

    @Override
    public Node<K, V> extractMin() {
        if (heapSize <= 0){
            System.out.println("Heap underflow.");
            return null;
        }
        Node<K, V> min = arr.get(0);
        arr.set(0, arr.get(heapSize - 1));
        arr.remove(heapSize-1);
        heapSize -= 1;
        minHeapify(0);
        return min;
    }

    @Override
    public void decreaseKey(int i, K key) {
      if (key.compareTo(arr.get(i).getKey()) > 0) {
        System.out.println("New key is bigger than current key.");
        return;
      }
      arr.get(i).setKey(key);
      while (i > 0 && arr.get(getDad(i)).getKey().compareTo(arr.get(i).getKey()) < 0) {
        Node<K, V> temp = arr.get(getDad(i));
        arr.set(getDad(i), arr.get(i));
        arr.set(i, temp);
        i = getDad(i);
      }
    }

    public int searchByValue(V value){
        for (int i = 0; i < heapSize; i++) {
            if (arr.get(i).getValue() == value){
                return i;
            }
        }
        return -1;
    }
}
