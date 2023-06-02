package com.example.map;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageLoader {
    private static final String RESOURCE_DIR = "/";

    public static Image loadImage(String filename) {
        String imagePath = RESOURCE_DIR + filename;
        return new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream(imagePath)));
    }
}