package com.example.datastructures.Graph.Graph;

public class FloydWarshalResult<V> {


        private final Vertex<V>[][] previous;
        private final double[][] distances;

        public FloydWarshalResult(Vertex<V>[][] previous, double[][] distances) {
            this.previous = previous;
            this.distances = distances;
        }

        public Vertex<V>[][] getPrevious() {
            return previous;
        }

        public double[][] getDistances() {
            return distances;
        }
}
