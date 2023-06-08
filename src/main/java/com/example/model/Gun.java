package com.example.model;


import com.example.map.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Gun{
    private ImageView floorSprite;
    private ImageView sprite;
    private ImageView bulletSprite;
    private Rectangle hitbox;
    private boolean isExplosive;
    private int ammo;
    private int currentAmmo;
    private double fireRate;
    private double reloadSpeed;
    private int xSpawn;
    private int ySpawn;
    private boolean canShoot = true;
    private boolean reloading = false;

    public Gun(int xSpawn, int ySpawn, String sprite, String floorSprite, String bulletSprite, boolean isExplosive, int ammo, double fireRate, double reloadSpeed) {
        this.xSpawn = xSpawn;
        this.ySpawn = ySpawn;
        this.sprite = new ImageView(ImageLoader.loadImage(sprite));
        this.floorSprite = new ImageView(ImageLoader.loadImage(floorSprite));
        this.bulletSprite = new ImageView(ImageLoader.loadImage(bulletSprite));
        hitbox = new Rectangle(xSpawn, ySpawn, 32, 32);
        this.isExplosive = isExplosive;
        this.ammo = ammo;
        this.fireRate = fireRate;
        this.reloadSpeed = reloadSpeed;
        currentAmmo = ammo;
    }

    public ImageView getFloorSprite() {
        return floorSprite;
    }

    public void setFloorSprite(ImageView floorSprite) {
        this.floorSprite = floorSprite;
    }

    public ImageView getBulletSprite() {
        return bulletSprite;
    }

    public void setBulletSprite(ImageView bulletSprite) {
        this.bulletSprite = bulletSprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public int getxSpawn() {
        return xSpawn;
    }

    public void setxSpawn(int xSpawn) {
        this.xSpawn = xSpawn;
    }

    public int getySpawn() {
        return ySpawn;
    }

    public void setySpawn(int ySpawn) {
        this.ySpawn = ySpawn;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void setSprite(ImageView sprite) {
        this.sprite = sprite;
    }

    public boolean isExplosive() {
        return isExplosive;
    }

    public void setExplosive(boolean explosive) {
        isExplosive = explosive;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getReloadSpeed() {
        return reloadSpeed;
    }

    public void setReloadSpeed(double reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    public boolean canShoot(){
        return canShoot;
    }

    public void reload(){
        Thread reload = new Thread(()->{
            if(reloading) return;
            reloading = true;
            canShoot = false;
            try {
                Thread.sleep((long)reloadSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.currentAmmo = ammo;
            canShoot = true;
            reloading = false;
        });
        reload.start();
    }
}
