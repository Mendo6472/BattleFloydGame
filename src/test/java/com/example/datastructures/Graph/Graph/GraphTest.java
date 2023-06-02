package com.example.datastructures.Graph.Graph;

import com.example.datastructures.Graph.AdjacencyListGraph.AdjacencyListGraph;
import com.example.datastructures.Graph.AdjacencyMatrixGraph.AdjacencyMatrixGraph;
import com.example.datastructures.Graph.Graph.DijkstraResult;
import com.example.datastructures.Graph.Graph.IGraph;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.datastructures.Graph.Graph.Vertex;
import com.example.datastructures.NaryTree.NaryTree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;


public class GraphTest {

    public IGraph<String> graph;
    public Vertex<String> vertexP = new Vertex<>("C");
    public Vertex<String> vertexA = new Vertex<>("A");

    public void globalGraphDeclaration(){
        /*
        To tests the different kinds of graph implementation change the initialization

        Tests of adjacency list:
        graph = new AdjacencyListGraph<>(false, false);

        Tests of adjacency matrix:
        graph = new AdjacencyMatrixGraph<>(false, false);
         */
        graph = new AdjacencyMatrixGraph<>(false, false);
    }


    public void setupStage1() {
        globalGraphDeclaration();
    }

    public void setupStage2() {
        globalGraphDeclaration();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        Vertex<String> vertex4 = new Vertex<>("D");
        Vertex<String> vertex5 = new Vertex<>("E");
        Vertex<String> vertex6 = new Vertex<>("F");
        Vertex<String> vertex7 = new Vertex<>("G");
        Vertex<String> vertex8 = new Vertex<>("H");
        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        graph.insertVertex(vertexP);
        graph.insertVertex(vertex4);
        graph.insertVertex(vertex5);
        graph.insertVertex(vertex6);
        graph.insertVertex(vertex7);
        graph.insertVertex(vertex8);
    }

    public void setupStage3() {
        globalGraphDeclaration();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        Vertex<String> vertex4 = new Vertex<>("D");
        Vertex<String> vertex5 = new Vertex<>("E");
        Vertex<String> vertex6 = new Vertex<>("F");
        Vertex<String> vertex7 = new Vertex<>("G");
        Vertex<String> vertex8 = new Vertex<>("H");

        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        graph.insertVertex(vertexP);
        graph.insertVertex(vertex4);
        graph.insertVertex(vertex5);
        graph.insertVertex(vertex6);
        graph.insertVertex(vertex7);
        graph.insertVertex(vertex8);

        graph.insertEdge(vertex1, vertex2, 3);
        graph.insertEdge(vertex1, vertexP, 5);
        graph.insertEdge(vertex2, vertex4, 1);
        graph.insertEdge(vertex2, vertex5, 8);
        graph.insertEdge(vertexP, vertex6, 12);
        graph.insertEdge(vertexP, vertex7, 5);

    }

    public void setupStage4(){
        globalGraphDeclaration();
        Vertex<String> vertexB = new Vertex<>("B");
        Vertex<String> vertexC = new Vertex<>("C");
        Vertex<String> vertexD = new Vertex<>("D");
        Vertex<String> vertexE = new Vertex<>("E");
        Vertex<String> vertexZ = new Vertex<>("Z");

        graph.insertVertex(vertexA);
        graph.insertVertex(vertexB);
        graph.insertVertex(vertexC);
        graph.insertVertex(vertexD);
        graph.insertVertex(vertexE);
        graph.insertVertex(vertexZ);

        graph.insertEdge(vertexA, vertexB, 4);
        graph.insertEdge(vertexA, vertexC, 2);
        graph.insertEdge(vertexB, vertexD, 5);
        graph.insertEdge(vertexB, vertexC, 1);
        graph.insertEdge(vertexC, vertexD, 8);
        graph.insertEdge(vertexC, vertexE, 10);
        graph.insertEdge(vertexD, vertexE, 2);
        graph.insertEdge(vertexD, vertexZ, 6);
        graph.insertEdge(vertexE, vertexZ, 3);
    }

    // Insert vertex
    @Test
    public void testInsertVertex1() {
        setupStage1();
        Vertex<String> vertex = new Vertex<>("A");
        Vertex<String> v = graph.insertVertex(vertex);
        assertEquals(v, vertex);
    }

    @Test
    public void testInsertVertex2() {
        setupStage1();
        Vertex<String> vertex = new Vertex<>("A");

        graph.insertVertex(vertex);
        Vertex<String> v2 = graph.insertVertex(vertex);

        assertNull(v2);
    }

    @Test
    public void testInsertVertex3() {
        setupStage2();
        Vertex<String> vertex = new Vertex<>("Z");
        Vertex<String> v = graph.insertVertex(vertex);
        assertEquals(v, vertex);
    }

    // Insert edge
    @Test
    public void testInsertEdge1() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("D");
        assertFalse(graph.insertEdge(vertex1, vertex2, 0));
    }

    @Test
    public void testInsertEdge2() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        assertTrue(graph.insertEdge(vertex1, vertex2, 0));
    }

    @Test
    public void testInsertEdge3() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        assertTrue(graph.insertEdge(vertex1, vertex2, 0));
        assertFalse(graph.insertEdge(vertex2, vertex1, 0));
    }

    // Delete vertex

    @Test
    public void testDeleteVertex1() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        graph.insertVertex(vertex1);
        assertTrue(graph.deleteVertex(vertex1));
    }

    @Test
    public void testDeleteVertex2() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        assertTrue(graph.deleteVertex(vertex1));
        assertFalse(graph.deleteVertex(vertex1));
    }

    @Test
    public void testDeleteVertex3() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        assertFalse(graph.deleteVertex(vertex2));
    }

    // Delete edge

    @Test
    public void testDeleteEdge1() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        assertFalse(graph.deleteEdge(vertex1, vertex2));
    }

    @Test
    public void testDeleteEdge2() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        graph.insertEdge(vertex1, vertex2, 0);
        assertTrue(graph.deleteEdge(vertex1, vertex2));
    }

    @Test
    public void testDeleteEdge3() {
        setupStage1();
        Vertex<String> vertex1 = new Vertex<>("A");
        Vertex<String> vertex2 = new Vertex<>("B");
        graph.insertVertex(vertex1);
        assertFalse(graph.deleteEdge(vertex1, vertex2));
    }


    // BFS
    @Test
    public void testBFS1() {
        setupStage3();
        NaryTree<String> naryTreeBFS = graph.BFS(vertexP);
        assertEquals("CABDEFG",naryTreeBFS.preorder(naryTreeBFS.getRoot()));
    }

    @Test
    public void testBFS2() {
        setupStage3();
        NaryTree<String> naryTreeBFS = graph.BFS(vertexP);
        assertNotEquals("CABDEFGH",naryTreeBFS.preorder(naryTreeBFS.getRoot()));
    }

    @Test
    public void testBFS3() {
        setupStage2();
        NaryTree<String> naryTreeBFS = graph.BFS(vertexP);
        assertEquals("C",naryTreeBFS.preorder(naryTreeBFS.getRoot()));

    }
    //DFS

    @Test
    public void testDFS1() {
        setupStage3();
        ArrayList<NaryTree<String>> naryTreeDFS = graph.DFS();
        assertEquals("ABDECFG",naryTreeDFS.get(0).preorder(naryTreeDFS.get(0).getRoot()));
        assertEquals("H",naryTreeDFS.get(1).preorder(naryTreeDFS.get(1).getRoot()));
    }

    @Test
    public void testDFS2() {
        setupStage3();
        ArrayList<NaryTree<String>> naryTreeDFS = graph.DFS();
        assertNotEquals("ABDECFGH",naryTreeDFS.get(0).preorder(naryTreeDFS.get(0).getRoot()));
    }

    @Test
    public void testDFS3() {
        setupStage2();
        ArrayList<NaryTree<String>> naryTreeDFS = graph.DFS();
        assertEquals("A",naryTreeDFS.get(0).preorder(naryTreeDFS.get(0).getRoot()));
        assertEquals("B",naryTreeDFS.get(1).preorder(naryTreeDFS.get(1).getRoot()));
        assertEquals("C",naryTreeDFS.get(2).preorder(naryTreeDFS.get(2).getRoot()));
        assertEquals("D",naryTreeDFS.get(3).preorder(naryTreeDFS.get(3).getRoot()));
        assertEquals("E",naryTreeDFS.get(4).preorder(naryTreeDFS.get(4).getRoot()));
        assertEquals("F",naryTreeDFS.get(5).preorder(naryTreeDFS.get(5).getRoot()));
        assertEquals("G",naryTreeDFS.get(6).preorder(naryTreeDFS.get(6).getRoot()));
        assertEquals("H",naryTreeDFS.get(7).preorder(naryTreeDFS.get(7).getRoot()));

    }

    @Test
    public void testDijkstraResult1(){
        setupStage3();
        DijkstraResult<String> result = graph.dijkstra(vertexP);
        String resultString = "";
        String distances = "";
        for(Vertex<String> v : result.getPrevious()){
            if(v != null){
                resultString += v.getValue() + " ";
            } else {
                resultString += "nill" + " ";
            }
        }
        for(Double distance : result.getDistances()){
            if(distance == Double.MAX_VALUE) distances += "inf ";
            else distances += distance + " ";
        }
        String expectedResult = "C A nill B B C C nill";
        String expectedDistances = "5.0 8.0 0.0 9.0 16.0 12.0 5.0 inf";
        assertEquals(expectedResult, resultString.trim());
        assertEquals(expectedDistances, distances.trim());
    }

    @Test
    public void testDijkstraResult2(){
        setupStage2();
        DijkstraResult<String> result = graph.dijkstra(vertexP);
        String resultString = "";
        String distances = "";
        for(Vertex<String> v : result.getPrevious()){
            if(v != null){
                resultString += v.getValue() + " ";
            } else {
                resultString += "nill" + " ";
            }
        }
        for(Double distance : result.getDistances()){
            if(distance == Double.MAX_VALUE) distances += "inf ";
            else distances += distance + " ";
        }
        String expectedResult = "nill nill nill nill nill nill nill nill";
        String expectedDistances = "inf inf 0.0 inf inf inf inf inf";
        assertEquals(expectedResult, resultString.trim());
        assertEquals(expectedDistances, distances.trim());
    }

    @Test
    public void testDijkstraResult3(){
        setupStage4();
        DijkstraResult<String> result = graph.dijkstra(vertexA);
        String resultString = "";
        String distances = "";
        for(Vertex<String> v : result.getPrevious()){
            if(v != null){
                resultString += v.getValue() + " ";
            } else {
                resultString += "nill" + " ";
            }
        }
        for(Double distance : result.getDistances()){
            if(distance == Double.MAX_VALUE) distances += "inf ";
            else distances += distance + " ";
        }
        String expectedResult = "nill C A B D E";
        String expectedDistances = "0.0 3.0 2.0 8.0 10.0 13.0";
        assertEquals(expectedResult, resultString.trim());
        assertEquals(expectedDistances, distances.trim());
    }

    @Test
    public void testFloydWarshall1(){
        setupStage3();
        double[][] expectedResult = {
                {0, 3, 5, 4, 11, 17, 10, Double.MAX_VALUE},
                {3, 0, 8, 1, 8, 20, 13, Double.MAX_VALUE},
                {5, 8, 0, 9, 16, 12, 5, Double.MAX_VALUE},
                {4, 1, 9, 0, 9, 21, 14, Double.MAX_VALUE},
                {11, 8, 16, 9, 0, 28, 21, Double.MAX_VALUE},
                {17, 20, 12, 21, 28, 0, 17, Double.MAX_VALUE},
                {10, 13, 5, 14, 21, 17, 0, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0}
        };
        double[][] result = graph.floydWarshall().getDistances();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expectedResult[i][j], result[i][j], 0);
            }
        }
    }

    @Test
    public void testFloydWarshall2(){
        setupStage2();
        double[][] expectedResult = {
                {0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, 0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, 0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0, Double.MAX_VALUE, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0, Double.MAX_VALUE},
                {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0}
        };
        double[][] result = graph.floydWarshall().getDistances();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expectedResult[i][j], result[i][j], 0);
            }
        }
    }
    @Test
    public void testFloydWarshall3(){
        setupStage4();
        double[][] expectedResult = {
                {0, 3, 2, 8, 10, 13},
                {3, 0, 1, 5, 7, 10},
                {2, 1, 0, 6, 8, 11},
                {8, 5, 6, 0, 2, 5},
                {10, 7, 8, 2, 0, 3},
                {13, 10, 11, 5, 3, 0}
        };
        double[][] result = graph.floydWarshall().getDistances();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expectedResult[i][j], result[i][j], 0);
            }
        }
    }

    @Test
    public void testPrim1(){
        setupStage3();
        String[] expectedResult = {"C","A","B","D","E","F","G"};

        NaryTree<Vertex<String>> result = graph.prim(vertexP);
        ArrayList<Vertex<String>> resultPreOrder = result.preorderList(result.getRoot()) ;
        for (int i = 0; i < resultPreOrder.size(); i++) {
            assertEquals(expectedResult[i],resultPreOrder.get(i).getValue());
        }
    }

    @Test
    public void testPrim2(){
        setupStage2();

        NaryTree<Vertex<String>> result = graph.prim(vertexP);
        assertEquals(result.weight(),1);
    }
    @Test
    public void testPrim3(){
        setupStage4();
        String[] expectedResult = {"A","C","B","D","E","Z"};
        NaryTree<Vertex<String>> result = graph.prim(vertexA);
        ArrayList<Vertex<String>> resultPreOrder = result.preorderList(result.getRoot()) ;
        for (int i = 0; i < resultPreOrder.size(); i++) {
            assertEquals(expectedResult[i],resultPreOrder.get(i).getValue());
        }

    }

    @Test
    public void testKruskal1(){
        setupStage3();
        String[][] expectedResult = {
                {"B","D"},
                {"A","B"},
                {"A","C"},
                {"G","C"},
                {"B","E"},
                {"F","C"},
        };
        ArrayList<Vertex<String>[]> paths = graph.kruskal();

        for (int i = 0; i < paths.size(); i++) {
            for (int j = 0; j < paths.get(0).length; j++) {
                assertEquals(expectedResult[i][j], paths.get(i)[j].getValue());

            }

        }
    }

    @Test
    public void testKruskal2(){
        setupStage2();

        ArrayList<Vertex<String>[]> paths = graph.kruskal();
        assertTrue(paths.isEmpty());
    }
    @Test
    public void testKruskal3(){
        setupStage4();
        String[][] expectedResult = {
                {"B","C"},
                {"A","C"},
                {"D","E"},
                {"E","Z"},
                {"B","D"},
        };
        ArrayList<Vertex<String>[]> paths = graph.kruskal();

        for (int i = 0; i < paths.size(); i++) {
            for (int j = 0; j < paths.get(0).length; j++) {
                assertEquals(expectedResult[i][j], paths.get(i)[j].getValue());

            }

        }
    }


   
}