package com.example.model;

public class Key extends Avatar{

    public Key(int xPosition, int yPosition, String imagePath, Intersection intersection, String id) {
        super(xPosition, yPosition, imagePath, 0, 0);
        this.intersection = intersection;
        this.id = id;
    }

    private Intersection intersection;
    private boolean picked = false;
    private int distanceToPlayer;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int compareToDistance(Key key){
        return Integer.compare(this.distanceToPlayer, key.distanceToPlayer);
    }

    public int getDistanceToPlayer() {
        return distanceToPlayer;
    }

    public void setDistanceToPlayer(int distanceToPlayer) {
        this.distanceToPlayer = distanceToPlayer;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }
}
