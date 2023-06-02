package com.example.datastructures.Graph.Graph;

public class Pair<V>{

  private Vertex<V> vertex;
  private double weight;
  public Vertex<V> getVertex() {
    return vertex;
  }

  public void setVertex(Vertex<V> vertex) {
    this.vertex = vertex;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }



  public Pair(Vertex<V> vertex, double weight){
    this.vertex = vertex;
    this.weight = weight;
  }

}