package com.example.model;

public class Player extends Avatar{

    private boolean goUp = false;
    private boolean goDown = false;
    private boolean goLeft = false;
    private boolean goRight = false;
    private int keysCollected = 0;

    private int offsetX = 0;
    private int offsetY = 0;
    private Gun firstGun = null;
    private Gun secondGun = null;
    private boolean invincibilityFramesStatus = false;

    public Gun getFirstGun() {
        return firstGun;
    }

    public void setFirstGun(Gun firstGun) {
        this.firstGun = firstGun;
    }

    public Gun getSecondGun() {
        return secondGun;
    }

    public void setSecondGun(Gun secondGun) {
        this.secondGun = secondGun;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

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

    public void takeDamage(int damage){
        Thread takeDamage = new Thread(()->{
            if(invincibilityFramesStatus) return;
            invincibilityFramesStatus = true;
            health -= damage;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            invincibilityFramesStatus = false;
        });
        takeDamage.start();
    }
}
