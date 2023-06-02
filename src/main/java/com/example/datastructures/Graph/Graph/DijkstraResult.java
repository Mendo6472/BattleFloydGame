package com.example.datastructures.Graph.Graph;

import java.util.ArrayList;

public class DijkstraResult<V> {
    private final ArrayList<Vertex<V>> previous;
    private final ArrayList<Double> distances;

    public DijkstraResult(ArrayList<Vertex<V>>previous, ArrayList<Double> distances) {
        this.previous = previous;
        this.distances = distances;
    }

    public  ArrayList<Vertex<V>> getPrevious() {
        return previous;
    }

    public ArrayList<Double>  getDistances() {
        return distances;
    }
}