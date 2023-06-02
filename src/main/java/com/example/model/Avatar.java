package com.example.model;

import com.example.map.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Avatar {
    protected int xPosition;
    protected int yPosition;
    protected ImageView sprite;
    protected int speed;
    protected Rectangle hitbox;
    protected double health;

    public Avatar(int xPosition, int yPosition, String imagePath, int speed, double health) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.speed = speed;
        this.health = health;
        sprite = new ImageView(ImageLoader.loadImage(imagePath));
        sprite.setX(xPosition);
        sprite.setY(yPosition);
        hitbox = new Rectangle(xPosition, yPosition, 32, 32);
        hitbox.xProperty().bind(sprite.xProperty());
        hitbox.yProperty().bind(sprite.yProperty());
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
        this.sprite.setX(xPosition);
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
        this.sprite.setY(yPosition);
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void setSprite(ImageView sprite) {
        this.sprite = sprite;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
