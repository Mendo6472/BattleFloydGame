package com.example.model;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Bullet{

    private Vector dir;
    private Vector pos;
    private int xPosition;
    private int yPosition;
    private int xSize;
    private int ySize;
    private ImageView sprite;
    private Rectangle hitbox;
    private boolean isRpg;

    public Bullet(int xPosition, int yPosition, int xSize, int ySize, ImageView sprite, Vector dir, boolean isRpg) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSize = 10;
        this.ySize = 10;
        this.sprite = sprite;
        this.hitbox = new Rectangle(xPosition, yPosition, 10, 10);
        this.dir = dir;
        this.isRpg = isRpg;
        this.pos = new Vector(xPosition, yPosition);
    }

    public void moveBullet(){
        xPosition = (int) ( xPosition + dir.getX() );
        yPosition = (int) ( yPosition + dir.getY() );
        sprite.setX(xPosition);
        sprite.setY(yPosition);
        hitbox.setX(xPosition);
        hitbox.setY(yPosition);
    }

    public boolean isRpg() {
        return isRpg;
    }

    public void setRpg(boolean rpg) {
        isRpg = rpg;
    }

    public Vector getDir() {
        return dir;
    }

    public void setDir(Vector dir) {
        this.dir = dir;
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

    public ImageView getSprite() {
        return sprite;
    }

    public void setSprite(ImageView sprite) {
        this.sprite = sprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
