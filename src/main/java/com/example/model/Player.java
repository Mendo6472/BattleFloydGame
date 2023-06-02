package com.example.model;

public class Player extends Avatar{

    private boolean goUp = false;
    private boolean goDown = false;
    private boolean goLeft = false;
    private boolean goRight = false;
    private int keysCollected = 0;

    public int getKeysCollected() {
        return keysCollected;
    }

    public void setKeysCollected(int keysCollected) {
        this.keysCollected = keysCollected;
    }

    public Player(int xPosition, int yPosition, String imagePath, int speed, double health) {
        super(xPosition, yPosition, imagePath, speed, health);
    }

    public void movePlayerW(){
        goUp = true;
    }

    public void movePlayerA() {
        goLeft = true;
    }

    public void movePlayerS(){
        goDown = true;
    }

    public void movePlayerD(){
        goRight = true;
    }

    public void stopMovePlayerW(){
        goUp = false;
    }

    public void stopMovePlayerA() {
        goLeft = false;
    }

    public void stopMovePlayerS(){
        goDown = false;
    }

    public void stopMovePlayerD(){
        goRight = false;
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp(boolean goUp) {
        this.goUp = goUp;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown(boolean goDown) {
        this.goDown = goDown;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }
}
