package com.example.map;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageLoader {
    private static final String RESOURCE_DIR = "/";

    public static Image loadImage(String filename) {
        String imagePath = RESOURCE_DIR + filename;
        return new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream(imagePath)));
        //Este método recibe la ruta del archivo que representa la imagen y recibe una entrada
        //(InputStream) asociado a la imagen de la ruta especificada. Esto se hace siempre y cuando
        //la entrada no sea null. Al final, el método devuelve la imagen.
    }
}