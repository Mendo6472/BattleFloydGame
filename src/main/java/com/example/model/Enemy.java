package com.example.model;

import com.example.datastructures.Graph.Graph.DijkstraResult;
import com.example.datastructures.Graph.Graph.Vertex;
import com.example.datastructures.NaryTree.NaryTree;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.sqrt;

public class Enemy extends Avatar{
    public Enemy(int xPosition, int yPosition, String imagePath, int speed, double health, Vertex<Intersection> currentLocation) {
        super(xPosition, yPosition, imagePath, speed, health);
        this.currentLocation = currentLocation;
    }
    Vertex<Intersection> currentlyTravelingVertex = null;
    boolean hasTraveled = false;
    boolean followedPlayer = false;
    boolean goingToVertex = false;
    boolean lostTrackOfPlayer = false;
    private Vertex<Intersection> currentLocation;

    public void moveInVectorDirection(Vector dir) {
        this.setxPosition((int) (xPosition + dir.getX()));
        this.setyPosition((int) (yPosition + dir.getY()));
    }

    public void moveEnemy(NaryTree<Intersection> path, Player player, ArrayList<Vertex<Intersection>> vertexList, ArrayList<Rectangle> obstacles, HashMap<String, Vertex<Intersection>> intersections) {
        Line line = new Line(xPosition+16, yPosition+16, player.getxPosition()+16, player.getyPosition()+16);
        line.setStrokeWidth(25);
        boolean lineIntersects = false;
        for(int j = 0; j < obstacles.size() && !lineIntersects; j++){
            if(line.intersects(obstacles.get(j).getBoundsInLocal())){
                lineIntersects = true;
            }
        }
        if(!lineIntersects) {
            currentlyTravelingVertex = new Vertex<>(new Intersection(player.getxPosition(), player.getyPosition()));
            followedPlayer = true;
            lostTrackOfPlayer = false;
            actuallyMoveEnemy();
            return;
        } else {
            if(followedPlayer) lostTrackOfPlayer = true;
            followedPlayer = false;
        }
        if(lostTrackOfPlayer){
            calculateCurrentEnemyVertex(obstacles, vertexList, intersections);
            actuallyMoveEnemy();
            return;
        }
        if(path == null) return;
        if(!hasTraveled){
            if(path.searchValue(currentLocation.getValue()).getDad() == null){

            } else {
                Vertex<Intersection> vertexToMoveTo = vertexList.get(searchIntersectionInVertexList(path.searchValue(currentLocation.getValue()).getDad().getValue(), vertexList));
                if(vertexToMoveTo == null) return;
                //currentLocation = vertexToMoveTo;
                currentlyTravelingVertex = vertexToMoveTo;
                hasTraveled = true;
            }

        }
        if(currentlyTravelingVertex == null){
            calculateCurrentEnemyVertex(obstacles, vertexList, intersections);
            return;
        }
        if(goingToVertex){
            actuallyMoveEnemy();
            return;
        }
        if(Math.abs(xPosition - currentlyTravelingVertex.getValue().getxPosition()) < 5 && Math.abs(yPosition - currentlyTravelingVertex.getValue().getyPosition()) < 5){
            currentLocation = currentlyTravelingVertex;
            int pathPos = vertexList.indexOf(currentLocation);
            if(path.searchValue(currentLocation.getValue()) != null){
                if(path.searchValue(currentLocation.getValue()).getDad() != null) {
                    currentlyTravelingVertex = vertexList.get(searchIntersectionInVertexList(path.searchValue(currentLocation.getValue()).getDad().getValue(), vertexList));
                }
            }
            if(currentlyTravelingVertex == null) return;
        }
        actuallyMoveEnemy();
    }

    private void actuallyMoveEnemy(){
        if(currentlyTravelingVertex == null) return;
        if(Math.abs(xPosition - currentlyTravelingVertex.getValue().getxPosition()) < 5 && Math.abs(yPosition - currentlyTravelingVertex.getValue().getyPosition()) < 5){
            goingToVertex = false;
            currentLocation = currentlyTravelingVertex;
            if(lostTrackOfPlayer) lostTrackOfPlayer = false;
            return;
        }
        double diffX = currentlyTravelingVertex.getValue().getxPosition() - xPosition;
        double diffY = currentlyTravelingVertex.getValue().getyPosition() - yPosition;
        Vector diff = new Vector(diffX, diffY);
        diff.normalize();
        diff.setMag(2);
        moveInVectorDirection(diff);
    }

    private void calculateCurrentEnemyVertex(ArrayList<Rectangle> obstacles, ArrayList<Vertex<Intersection>> vertexList, HashMap<String, Vertex<Intersection>> intersections){
        ArrayList<Intersection> intersectionList = new ArrayList<>();
        for (Vertex<Intersection> vertex : vertexList) {
            double distance = calculateDistance(xPosition, vertex.getValue().getxPosition(), yPosition, vertex.getValue().getyPosition());
            vertex.getValue().setDistanceToEnemy(distance);
            intersectionList.add(vertex.getValue());
        }
        intersectionList.sort(Intersection::compareToDistanceEnemy);
        boolean foundVertex = false;
        Vertex<Intersection> vertex = null;
        for(int i = 0; i < intersectionList.size() && !foundVertex; i++){
            Intersection intersection = intersectionList.get(i);
            Line line = new Line(xPosition+16, yPosition+16, intersection.getxPosition(), intersection.getyPosition());
            boolean lineIntersects = false;
            for(int j = 0; j < obstacles.size() && !lineIntersects; j++){
                if(line.intersects(obstacles.get(j).getBoundsInLocal())){
                    lineIntersects = true;
                }
            }
            if(!lineIntersects) {
                foundVertex = true;
                vertex = intersections.get(intersection.getiPosition() + " " + intersection.getjPosition());
            }
        }
        if(!foundVertex) System.out.println("bad nono bad");
        currentlyTravelingVertex = vertex;
    }

    private double calculateDistance(int x1, int x2, int y1, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private int searchIntersectionInVertexList(Intersection i, ArrayList<Vertex<Intersection>> vertexList){
        for (int j = 0; j < vertexList.size(); j++) {
            if(vertexList.get(j).getValue()==i){
                return j;
            }
        }

        return -1;
    }

    public void takeDamage(int damage){
        health -= damage;
    }


}
