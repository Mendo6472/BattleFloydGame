package com.example.datastructures.Graph.AdjacencyListGraph;


import com.example.datastructures.Graph.Graph.*;
import com.example.datastructures.NaryTree.NaryTree;
import com.example.datastructures.NaryTree.Node;
import com.example.datastructures.PriorityQueue.Heap;
import com.example.datastructures.UnionFind.UnionFind;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AdjacencyListGraph<V> implements IGraph<V> {
    boolean addressed;
    boolean weighted;
    public ArrayList<Vertex<V>> vertexList;

    int time;

    public AdjacencyListGraph(boolean addressed, boolean weighted) {
        vertexList = new ArrayList<>();
        this.addressed = addressed;
        this.weighted = weighted;
    }


    @Override
    public Vertex<V> insertVertex(Vertex<V> vertex) {
        if(!vertexList.contains(vertex)){
            vertexList.add(vertex);
            return vertex;
        }
        return null;
    }

    @Override
    public boolean insertEdge(Vertex<V> v1, Vertex<V> v2, double weight) {
        if(!vertexList.contains(v1) || !vertexList.contains(v2)) return false;
        Pair<V> pairV1 = new Pair<>(v1,weight);
        Pair<V> pairV2 = new Pair<>(v2,weight);
        if(addressed){
            if(!searchVertexInAdjencyList(v1,v2)){
                v2.getAdjacencyList().add(pairV1);
                return true;
            }
        }else{
            if(!searchVertexInAdjencyList(v1,v2) && !searchVertexInAdjencyList(v2,v1) ){
                v1.getAdjacencyList().add(pairV2);
                v2.getAdjacencyList().add(pairV1);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteVertex(Vertex<V> vertex) {
        if (vertexList.contains(vertex)){
            for (Vertex<V> v: vertexList) {
                if (v != vertex){
                    if(searchVertexInAdjencyList(v, vertex)){
                        v.getAdjacencyList().remove(searchPosVertexInAdjencyList(v,vertex));
                    }
                }
            }
            vertexList.remove(vertex);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean deleteEdge(Vertex<V> v1, Vertex<V> v2) {
        if(addressed){
            if(searchVertexInAdjencyList(v1,v2)){
                v1.getAdjacencyList().remove(searchPosVertexInAdjencyList(v1,v2));
                return true;
            }else{
                return false;
            }
            
        }else{
            if(searchVertexInAdjencyList(v1,v2) && searchVertexInAdjencyList(v1,v2)){
                v1.getAdjacencyList().remove(searchPosVertexInAdjencyList(v1,v2));
                v2.getAdjacencyList().remove(searchPosVertexInAdjencyList(v2,v1));
                return true;
            }else{
                return false;
            }
        }
        
    }

    @Override
    public NaryTree<V> BFS(Vertex<V> s) {
        NaryTree<V> naryTree = new NaryTree<>();
        naryTree.setRoot(new Node<>(s.value));
        Queue<Vertex<V>> queue = new LinkedList<>();

        for (Vertex<V> u :vertexList) {
            u.setColor(Color.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setPredecessor(null);
        }
        s.setColor(Color.GRAY);
        s.setDistance(0);
        s.setPredecessor(null);
        queue.add(s);
        while(!queue.isEmpty()){
            Vertex<V> u = queue.poll();
            for (Pair<V> p : u.getAdjacencyList()){
                if(p.getVertex().getColor() == Color.WHITE){
                    p.getVertex().setColor(Color.GRAY);
                    p.getVertex().setDistance(u.getDistance()+1);
                    p.getVertex().setPredecessor(u);
                    naryTree.insertNode(p.getVertex().getValue(),p.getVertex().getPredecessor().getValue());
                    queue.add(p.getVertex());
                }
            }
            u.setColor(Color.BLACK);

        }
        return naryTree;
    }

    @Override
    public NaryTree<Vertex<V>> prim(Vertex<V> s) {
        NaryTree<Vertex<V>> naryTree = new NaryTree<>();
        naryTree.setRoot(new Node<>(s));
        Heap<Double, Vertex<V>> queue = new Heap<>();
        queue.insert(0.0, s);
        for(Vertex <V> vertex : vertexList){
            if(vertex != s){
                queue.insert(Double.MAX_VALUE, vertex);
                vertex.setColor(Color.WHITE);
            }
        }
        while (queue.getHeapSize() > 0){
            com.example.datastructures.PriorityQueue.Node<Double, Vertex<V>> u = queue.extractMin();
            for(Pair<V> adj : u.getValue().getAdjacencyList()){
                int heapNodePos = queue.searchByValue(adj.getVertex());
                if(heapNodePos != -1){
                    com.example.datastructures.PriorityQueue.Node<Double, Vertex<V>> v = queue.getHeap().get(heapNodePos);
                    if(v.getValue().getColor() == Color.WHITE && adj.getWeight() < v.getKey()){
                        v.setKey(adj.getWeight());
                        v.getValue().setWeight(adj.getWeight());
                        queue.decreaseKey(queue.getHeap().indexOf(v),adj.getWeight());
                        v.getValue().setPredecessor(u.getValue());
                    }
                }
            }
            queue.buildMinHeap();
            u.getValue().setColor(Color.BLACK);
        }
        Queue<Vertex<V>> treeQueue = new LinkedList<>();
        treeQueue.add(s);
        while (!treeQueue.isEmpty()){
            Vertex<V> vertex = treeQueue.poll();
            for(Pair<V> v : vertex.getAdjacencyList()){
                if(v.getVertex().getPredecessor() == vertex){
                    treeQueue.add(v.getVertex());
                    naryTree.insertNode(v.getVertex(), vertex);
                }
            }
        }
        return naryTree;
    }

    @Override
    public ArrayList<NaryTree<V>> DFS() {
        ArrayList<NaryTree<V>> naryTrees = new ArrayList<>();
        for (Vertex<V> u: vertexList) {
            u.setColor(Color.WHITE);
            u.setPredecessor(null);
        }
        time = 0;
        for (Vertex<V> u: vertexList) {
            if (u.getColor() == Color.WHITE){
                NaryTree<V> naryTree = new NaryTree<>();
                naryTree.setRoot(new Node<V>(u.getValue()));
                DFSV(u, naryTree);
                naryTrees.add(naryTree);
                
            }
        }

        return naryTrees;
    }

    public void DFSV(Vertex<V> u, NaryTree<V> n) {
        time += 1;
        u.setDistance(time);
        u.setColor(Color.GRAY);
        for (Pair<V> p: u.getAdjacencyList()){
            if(p.getVertex().getColor() == Color.WHITE){
                p.getVertex().setPredecessor(u);
                n.insertNode(p.getVertex().getValue(), u.getValue());
                DFSV(p.getVertex(),n);
            }
        }
        u.setColor(Color.BLACK);
        time = time + 1;
    }

    boolean searchVertexInAdjencyList(Vertex<V> vertex, Vertex<V> vertexToSearch){
        for (Pair<V> pair: vertex.adjacencyList) {
            if( pair.getVertex() == vertexToSearch){
                return true;
            }
        }
        return false;
    }

    int searchPosVertexInAdjencyList(Vertex<V> vertex, Vertex<V> vertexToSearch){
        for (int i = 0; i < vertex.adjacencyList.size(); i++) {
            if( vertex.getAdjacencyList().get(i).getVertex() == vertexToSearch){
                return i;
            }
        }
        return -1;
    }

    @Override
    public DijkstraResult<V> dijkstra (Vertex<V> source){
        ArrayList<Vertex<V>> previous = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        Heap<Double, Vertex<V>> queue = new Heap<>();
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i) != source){
                distances.add(Double.MAX_VALUE);
            }else{
                distances.add(0.0);
            }
            previous.add(null);
            queue.insert(distances.get(i),vertexList.get(i));
        }
        queue.buildMinHeap();

        while (queue.getHeapSize()>0){
            Vertex<V> u = queue.extractMin().getValue();
            for (Pair<V> pair : u.getAdjacencyList()) {
                double alt = distances.get(vertexList.indexOf(u)) + pair.getWeight();
                if (alt < distances.get(vertexList.indexOf(pair.getVertex()))){
                    distances.set(vertexList.indexOf(pair.getVertex()),alt);
                    previous.set(vertexList.indexOf(pair.getVertex()),u);
                    queue.decreaseKey(queue.searchByValue(pair.getVertex()),alt);
                }
                queue.buildMinHeap();

            }
        }
        return new DijkstraResult<V>(previous, distances);
    }




    @Override
    public FloydWarshalResult<V> floydWarshall() {
        double[][] distances = new double[vertexList.size()][vertexList.size()];
        Vertex<V>[][] previous = new Vertex[vertexList.size()][vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            for (int j = 0; j < vertexList.size(); j++) {
                if ( i == j ) {
                    distances[i][j] = 0.0;
                } else if ( searchVertexInAdjencyList(vertexList.get(i), vertexList.get(j)) ) {
                    distances[i][j] = vertexList.get(i).adjacencyList.get(searchPosVertexInAdjencyList(vertexList.get(i), vertexList.get(j))).getWeight();
                    previous[i][j] = vertexList.get(i);
                } else {
                    distances[i][j] = Double.MAX_VALUE;
                    previous[i][j] = vertexList.get(i);
                }

            }
        }

        for (int k = 0; k < vertexList.size(); k++) {
            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = 0; j < vertexList.size(); j++) {
                    if ( distances[i][j] > distances[i][k] + distances[k][j] ) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                        previous[i][j] = previous[k][j];
                    }
                }
            }
        }
        FloydWarshalResult<V> result = new FloydWarshalResult<>(previous, distances);
        return result;
    }



    public ArrayList<Vertex<V>[]> kruskal(){
        ArrayList<Vertex<V>[]> A = new ArrayList<>();
        UnionFind<V> unionFind = new UnionFind<>();
        Heap<Double, Vertex<V>[]> PQ = new Heap<>();
         for (Vertex<V> vertex: vertexList) {
            unionFind.makeSet(vertex.getValue());
             for (int i = 0; i < vertex.adjacencyList.size(); i++) {
                 Vertex<V>[] pairVertex = new Vertex[2];
                 pairVertex[0] = vertex;
                 pairVertex[1] = vertex.adjacencyList.get(i).getVertex();
                 PQ.insert(vertex.getAdjacencyList().get(i).getWeight(), pairVertex);
             }
        }
         PQ.buildMinHeap();
         while (PQ.getHeapSize()>0) {

             com.example.datastructures.PriorityQueue.Node<Double, Vertex<V>[]> tmp = PQ.extractMin();
             Vertex<V> u = tmp.getValue()[0];
             Vertex<V> v = tmp.getValue()[1];
             if (unionFind.find(u.getValue()) != unionFind.find(v.getValue())){
                 A.add(tmp.getValue());
                 unionFind.union(unionFind.find(u.getValue()),unionFind.find(v.getValue()));
             }
         }
         return A;
        }

    }


