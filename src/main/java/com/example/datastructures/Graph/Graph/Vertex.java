package com.example.datastructures.Graph.Graph;

import java.util.ArrayList;

public class Vertex<V> {
    public V value;
    Color color;
    int distance;
    double weight;

    Vertex<V> predecessor;
    public ArrayList<Pair<V>> adjacencyList;

    public Vertex(V value) {
        this.value = value;
        adjacencyList = new ArrayList<>();
    }

    public Vertex(V value, ArrayList<Pair<V>> adjacencyList) {
        this.value = value;
        this.adjacencyList = adjacencyList;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vertex<V> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex<V> predecessor) {
        this.predecessor = predecessor;
    }

    public ArrayList<Pair<V>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(ArrayList<Pair<V>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
