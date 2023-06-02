package com.example.model;

public class Intersection {
    private int xPosition;
    private int yPosition;
    private int iPosition;
    private int jPosition;

    private double distanceToPlayer;
    private double distanceToEnemy;
    private boolean hasKey = false;

    public boolean isHasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public double getDistanceToEnemy() {
        return distanceToEnemy;
    }

    public void setDistanceToEnemy(double distanceToEnemy) {
        this.distanceToEnemy = distanceToEnemy;
    }

    public Intersection(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.iPosition = yPosition/32;
        this.jPosition = xPosition/32;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getiPosition() {
        return iPosition;
    }

    public void setiPosition(int iPosition) {
        this.iPosition = iPosition;
    }

    public int getjPosition() {
        return jPosition;
    }

    public void setjPosition(int jPosition) {
        this.jPosition = jPosition;
    }

    public double getDistanceToPlayer() {
        return distanceToPlayer;
    }

    public void setDistanceToPlayer(double distanceToPlayer) {
        this.distanceToPlayer = distanceToPlayer;
    }

    public int compareToDistance(Intersection intersection){
        return Double.compare(this.distanceToPlayer, intersection.getDistanceToPlayer());
    }

    public int compareToDistanceEnemy(Intersection intersection){
        return Double.compare(this.distanceToEnemy, intersection.getDistanceToEnemy());
    }
}