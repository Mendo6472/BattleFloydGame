package com.example.map;

import com.example.datastructures.Graph.AdjacencyListGraph.AdjacencyListGraph;
import com.example.datastructures.Graph.AdjacencyMatrixGraph.AdjacencyMatrixGraph;
import com.example.datastructures.Graph.Graph.*;
import com.example.datastructures.NaryTree.NaryTree;
import com.example.model.*;
import com.example.model.Vector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MapController implements Initializable {
    public Label ammoLabel;
    boolean nextLevel = false;
    boolean retry = false;
    @FXML
    public Canvas canvas;
    public Pane pane;
    public Label keysPositions;
    private GraphicsContext gc;
    private Image mapImage;
    private int SCREEN_WIDHT = 1920;
    private int SCREEN_HEIGHT = 1080;
    private int currentGun = 1;

    private int CANVAS_WIDTH;
    private int CANVAS_HEIGHT;

    private ArrayList<Rectangle> collisions = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Gun> guns = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<ImageView> explosions = new ArrayList<>();
    private Player player;
    private int[][] map;
    private HashMap<Integer, Image> images = new HashMap<>();
    private AdjacencyListGraph<Intersection> pathFindingGraph = new AdjacencyListGraph<>(false, false);
    private HashMap<String, Vertex<Intersection>> intersections = new HashMap<>();
    private NaryTree<Intersection> currentPath = null;
    private Vertex<Intersection> currentPlayerVertex = null;
    boolean lost = false;
    boolean win = false;


    double mouseX = 0;
    double mouseY = 0;
    double angle = 0;

    boolean shooting;
    int currentLevel = 1;
    //MediaPlayer mediaPlayer;

    Image healthImage = ImageLoader.loadImage("ui/heart.png");
    Image reticleImage = ImageLoader.loadImage("ui/reticle.png");
    Image explosionImage = ImageLoader.loadImage("ui/explosion.png");
    Image portalImage = ImageLoader.loadImage("ui/portal.png");
    ImageView portalImageView = null;

    public static int[][] readMatrix(String filePath) {
        int[][] matrix = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //Este método lee la matriz que representa el mapa del juego.
            String line;
            int rows = 0;
            int columns = 0;

            // Contar el número de filas y columnas
            while ((line = br.readLine()) != null) { //Mientras haya algo por leer
                String[] values = line.trim().split("\\s+"); // Dividir la línea por espacios

                if (columns == 0) {
                    columns = values.length; // Número de columnas en la primera fila
                }

                rows++; // Contar el número de filas
            }

            // Inicializar la matriz con el tamaño correcto
            matrix = new int[rows][columns];

            // Leer los valores y almacenarlos en la matriz
            br.close(); // Cerrar el archivo

            BufferedReader br2 = new BufferedReader(new FileReader(filePath)); // Volver a abrir el archivo

            int currentRow = 0;
            while ((line = br2.readLine()) != null) { //Mientras haya algo para leer
                String[] values = line.trim().split("\\s+"); // Dividir la línea por espacios

                for (int column = 0; column < values.length; column++) {
                    matrix[currentRow][column] = Integer.parseInt(values[column]);
                }

                currentRow++;
            }//Se llena la matriz con los valores de las columnas y filas.

        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    public void changeMap(int levelNumber){

        player = new Player(2050, 2050, "player/playerSprite.png", 5, 3);
        int ammountOfEnemies = 0;
        switch (levelNumber) {
            case 1 -> {
                map = readMatrix("Map1.txt");
                ammountOfEnemies = 10;
                //File audioFile = new File("/music/firstLevel.mp3");
                //Media media = new Media(audioFile.toURI().toString());
                //mediaPlayer = new MediaPlayer(media);
            }
            case 2 -> {
                map = readMatrix("Map2.txt");
                ammountOfEnemies = 20;
                //File audioFile = new File("/music/secondLevel.mp3");
                //Media media = new Media(audioFile.toURI().toString());
                //mediaPlayer = new MediaPlayer(media);
            }
            case 3 -> {
                map = readMatrix("Map3.txt");
                ammountOfEnemies = 30;
                player = new Player(2000, 2000,"player/playerSprite.png", 5, 3 );
                //File audioFile = new File("/music/thirdLevel.mp3");
                //Media media = new Media(audioFile.toURI().toString());
                //mediaPlayer = new MediaPlayer(media);
            }
        }
        portalImageView = null;
        win = false;
        lost = false;
        pathFindingGraph = new AdjacencyListGraph<>(false, false);
        currentPath = null;
        collisions = new ArrayList<>();
        enemies = new ArrayList<>();
        guns = new ArrayList<>();
        bullets = new ArrayList<>();
        CANVAS_WIDTH = map[0].length*32;
        CANVAS_HEIGHT = map.length*32;
        gc = canvas.getGraphicsContext2D();
        Random random = new Random();
        Canvas largeCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext largeGC = largeCanvas.getGraphicsContext2D();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int number = map[row][col];
                if(number != 14 && number != 56 && number != 67){ //Si no es alguno de estos números, hay una
                    //colisión
                    Rectangle obstacle = new Rectangle(32*col, 32*row, 32, 32);
                    collisions.add(obstacle);
                }
                if(number == 56 || number == 14){
                    Vertex<Intersection> vertex = new Vertex<>(new Intersection(32*col, 32*row));
                    pathFindingGraph.insertVertex(vertex);
                    intersections.put(vertex.getValue().getiPosition() + " " + vertex.getValue().getjPosition(),vertex);
                }
                Image image = images.get(number);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(32);
                imageView.setFitHeight(32);
                largeGC.drawImage(imageView.getImage(), 32*col, 32*row);
            }
        }
        for(int i = 0; i < ammountOfEnemies; i++){
            int randomIndex = random.nextInt(pathFindingGraph.vertexList.size());
            Vertex<Intersection> vertex = pathFindingGraph.vertexList.get(randomIndex);
            if(!vertex.getValue().isHasKey()){
                vertex.getValue().setHasKey(true);
                Enemy enemy = new Enemy(vertex.getValue().getxPosition(), vertex.getValue().getyPosition(), "enemies/zombie.png", 5, 3, vertex);
                enemies.add(enemy);
            } else {
                i--;
            }
        }
        for(int i = 0; i < 2; i++){
            int randomIndex = random.nextInt(pathFindingGraph.vertexList.size());
            Vertex<Intersection> vertex = pathFindingGraph.vertexList.get(randomIndex);
            Gun gun;
            if(i == 0){
                gun = new Gun(vertex.getValue().getxPosition(), vertex.getValue().getyPosition(), "guns/m134.png", "guns/m134_d.png","guns/bullets/gold.png",false, 30, 100, 1000);
            } else {
                gun = new Gun(vertex.getValue().getxPosition(), vertex.getValue().getyPosition(), "guns/rpglauncher.png", "guns/rpglauncher_d.png", "guns/bullets/rpg.png",true, 1, 1000, 3000);
            }
            guns.add(gun);
        }

        //Threads

        Thread draw = new Thread(()->{
            double offsetX = 0;
            double offsetY = 0;
            while (!win && !lost){
                SCREEN_HEIGHT = (int) pane.getHeight();
                SCREEN_WIDHT = (int) pane.getWidth();
                try {
                    Thread.sleep((long) 1000/60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                offsetX = player.getxPosition() - SCREEN_WIDHT / 2 + 16;
                offsetY = player.getyPosition() - SCREEN_HEIGHT / 2 + 16;
                player.setOffsetX((int) offsetX);
                player.setOffsetY((int) offsetY);
                int playerXdraw = SCREEN_WIDHT/2-16;
                int playerYdraw = SCREEN_HEIGHT/2-16;
                if(offsetX < 0){
                    playerXdraw += offsetX;
                    offsetX = 0;
                }
                if(offsetY < 0){
                    playerYdraw += offsetY;
                    offsetY = 0;
                }
                if(offsetX > CANVAS_WIDTH - SCREEN_WIDHT){
                    playerXdraw += Math.abs(offsetX - (CANVAS_WIDTH - SCREEN_WIDHT));
                    offsetX = CANVAS_WIDTH - SCREEN_WIDHT;
                }
                if(offsetY > CANVAS_HEIGHT - SCREEN_HEIGHT){
                    playerYdraw += Math.abs(offsetY - (CANVAS_HEIGHT - SCREEN_HEIGHT));
                    offsetY = CANVAS_HEIGHT - SCREEN_HEIGHT;
                }
                gc.drawImage(mapImage, offsetX, offsetY, 1920, 1080, 0, 0, 1920, 1080);
                for(Gun gun : guns){
                    gc.drawImage(gun.getFloorSprite().getImage(), gun.getxSpawn() - offsetX, gun.getySpawn() - offsetY);
                }
                gc.save();
                gc.translate(playerXdraw+16, playerYdraw+16);
                gc.rotate(player.getSprite().getRotate());
                gc.drawImage(player.getSprite().getImage(), -16 , -16);
                switch (currentGun) {
                    case 1 -> {
                        if (player.getFirstGun() != null) {
                            gc.drawImage(player.getFirstGun().getSprite().getImage(), 0, 0);
                        }
                    }
                    case 2 -> {
                        if (player.getSecondGun() != null) {
                            gc.drawImage(player.getSecondGun().getSprite().getImage(), 0, 0);
                        }
                    }
                }
                gc.restore();
                for(int i = 0; i < bullets.size(); i++){
                    gc.save();
                    gc.translate(bullets.get(i).getxPosition() - offsetX, bullets.get(i).getyPosition() - offsetY);
                    gc.rotate(bullets.get(i).getSprite().getRotate());
                    gc.drawImage(bullets.get(i).getSprite().getImage(), 0, 0);
                    gc.restore();
                }
                for(Enemy enemy : enemies){
                    gc.drawImage(enemy.getSprite().getImage(), enemy.getxPosition() - offsetX, enemy.getyPosition() - offsetY);
                }
                for(ImageView explosion : explosions){
                    gc.drawImage(explosion.getImage(), explosion.getX() - offsetX, explosion.getY() - offsetY, 200, 200);
                }
                if(portalImageView != null){
                    gc.drawImage(portalImageView.getImage(), portalImageView.getX() - offsetX, portalImageView.getY() - offsetY, 32, 32);
                }
                int startingXHealth = 10;
                for(int i = 0; i < player.getHealth(); i++){
                    gc.drawImage(healthImage, startingXHealth, 0, 60, 60);
                    startingXHealth += 70;
                }
                gc.drawImage(reticleImage, mouseX-32, mouseY-32, 64, 64);
                if(player.getFirstGun() != null){
                    gc.drawImage(player.getFirstGun().getSprite().getImage(), 20, 70, 100, 100);
                }
                if(player.getSecondGun() != null){
                    gc.drawImage(player.getSecondGun().getSprite().getImage(), 140, 70, 200, 100);
                }
                switch (currentGun){
                    case 1 ->{
                        if(player.getFirstGun()!=null){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    ammoLabel.setText(player.getFirstGun().getCurrentAmmo() + "/" + player.getFirstGun().getAmmo());
                                }
                            });
                        }
                    }
                    case 2 ->{
                        if(player.getSecondGun()!=null){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    ammoLabel.setText(player.getSecondGun().getCurrentAmmo() + "/" + player.getSecondGun().getAmmo());
                                }
                            });

                        }
                    }
                }
            }
        });
        Thread shoot = new Thread(()->{ //Hilo que permite realizar los disparos de las armas por parte del jugador.
            while (!win && !lost){
                if(shooting){
                    double timeToWait = -1; //Velocidad de fuego del arma.
                    ImageView bulletImage = null;
                    boolean isRpg = false;
                    boolean canShoot = false;
                    Gun gun = null;
                    switch (currentGun) {
                        case 1 -> {
                            if (player.getFirstGun() != null) {
                                timeToWait = player.getFirstGun().getFireRate();
                                bulletImage = player.getFirstGun().getBulletSprite();
                                canShoot = player.getFirstGun().canShoot();
                                gun = player.getFirstGun();
                            }
                        }
                        case 2 -> {
                            if (player.getSecondGun() != null) {
                                timeToWait = player.getSecondGun().getFireRate();
                                bulletImage = player.getSecondGun().getBulletSprite();
                                isRpg = true;
                                canShoot = player.getSecondGun().canShoot();
                                gun = player.getSecondGun();
                            }
                        }
                    }
                    if(timeToWait != -1 && canShoot){
                        int offsetX = player.getOffsetX();
                        int offsetY = player.getOffsetY();
                        int screenWidthCenter = SCREEN_WIDHT/2;
                        int screenHeightCenter = SCREEN_HEIGHT/2;
                        if(offsetX < 0){
                            screenWidthCenter += offsetX;
                        }
                        if(offsetY < 0){
                            screenHeightCenter += offsetY;
                        }
                        if(offsetX > CANVAS_WIDTH - SCREEN_WIDHT){
                            screenWidthCenter += Math.abs(offsetX - (CANVAS_WIDTH - SCREEN_WIDHT));
                        }
                        if(offsetY > CANVAS_HEIGHT - SCREEN_HEIGHT){
                            screenHeightCenter += Math.abs(offsetY - (CANVAS_HEIGHT - SCREEN_HEIGHT));
                        }
                        double diffX = mouseX - screenWidthCenter; //Se calcula la distancia que hay entre el mouse y el centro
                        //de la pantalla con respecto a X e Y.
                        double diffY = mouseY - screenHeightCenter;
                        Vector diff = new Vector(diffX, diffY);
                        diff.normalize(); //Se normaliza el vector con la intención que no cambie la velocidad de la bala dependiendo
                        //de la distancia.
                        diff.setMag(10); //velocidad con la que se moverá la bala.
                        Bullet bullet = new Bullet(player.getxPosition()+16, player.getyPosition()+16,(int) bulletImage.getFitWidth(),(int) bulletImage.getFitHeight() , bulletImage, diff, isRpg);
                        angle = Math.toDegrees(Math.atan2(mouseY - screenHeightCenter, mouseX - screenWidthCenter));
                        bullet.getSprite().setRotate(angle);
                        bullets.add(bullet);
                        gun.setCurrentAmmo(gun.getCurrentAmmo()-1);
                        if(gun.getCurrentAmmo() <= 0){
                            gun.reload();
                        }
                        try {
                            Thread.sleep((long) timeToWait); //Se dispara teniendo en cuenta la cadencia o velocidad de fuego
                            //del arma.
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            Thread.sleep((long) 16.666);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    try {
                        Thread.sleep((long) 16.666);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Thread movePlayer = new Thread(()->{
            int oldPlayerX = player.getxPosition();
            int oldPlayerY = player.getyPosition();
            while (!lost && !win){
                try {
                    Thread.sleep((long) 1000/60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(player.isGoUp()){
                    player.setyPosition(player.getyPosition() - player.getSpeed());
                }
                if(player.isGoDown()){
                    player.setyPosition(player.getyPosition() + player.getSpeed());
                }
                if(player.isGoLeft()){
                    player.setxPosition(player.getxPosition() - player.getSpeed());
                }
                if(player.isGoRight()){
                    player.setxPosition(player.getxPosition() + player.getSpeed());
                }
                for(Rectangle obstacle : collisions){
                    if(player.getHitbox().intersects(obstacle.getBoundsInLocal())){
                        player.setxPosition(oldPlayerX);
                        player.setyPosition(oldPlayerY);
                    }
                }
                oldPlayerX = player.getxPosition();
                oldPlayerY = player.getyPosition();
                for(int i = 0; i < guns.size(); i++){
                    Gun gun = guns.get(i);
                    if(player.getHitbox().intersects(gun.getHitbox().getBoundsInLocal())){
                        if(gun.isExplosive()){
                            player.setSecondGun(gun);
                        } else {
                            player.setFirstGun(gun);
                        }
                        guns.remove(gun);
                    }
                }
                for(int i = 0; i < bullets.size(); i++){
                    Bullet bullet = bullets.get(i);
                    bullet.moveBullet();
                    boolean colliding = false;
                    for(int j = 0; j < enemies.size() && !colliding; j++){
                        Enemy enemy = enemies.get(j);
                        if(bullet.getHitbox().intersects(enemy.getHitbox().getBoundsInLocal())){
                            if(bullet.isRpg()){
                                explodeRpg(bullet);
                            } else{
                                enemy.takeDamage(1);
                            }
                            bullets.remove(bullet);
                            colliding = true;
                        }
                    }
                    for(int j = 0; j < collisions.size() && !colliding; j++){
                        Rectangle rectangle = collisions.get(j);
                        if(bullet.getHitbox().intersects(rectangle.getBoundsInLocal())){
                            if(bullet.isRpg()){
                                explodeRpg(bullet);
                            }
                            bullets.remove(bullet);
                            colliding = true;
                        }
                    }
                }
                for(int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if(enemy.getHealth() <= 0){
                        enemies.remove(enemy);
                    } else {
                        enemy.moveEnemy(currentPath, player, pathFindingGraph.vertexList, collisions, intersections);
                        if(player.getHitbox().intersects(enemy.getHitbox().getBoundsInLocal())){
                            player.takeDamage(1);
                        }
                    }
                }
                if(player.getHealth() <= 0){
                    lost = true;
                }
                if(enemies.size() == 0 && portalImageView == null){
                    portalImageView = new ImageView(portalImage);
                    int randomIndex = random.nextInt(pathFindingGraph.vertexList.size());
                    Vertex<Intersection> vertex = pathFindingGraph.vertexList.get(randomIndex);
                    portalImageView.setX(vertex.getValue().getxPosition());
                    portalImageView.setY(vertex.getValue().getyPosition());
                }
                if(portalImageView != null){
                    Rectangle rectangle = new Rectangle(portalImageView.getX(), portalImageView.getY(), 32, 32);
                    if(player.getHitbox().intersects(rectangle.getBoundsInLocal())){
                        win = true;
                    }
                }
            }
        });

        Thread trackPlayer = new Thread(() -> {
            while (!win && !lost){
                try {
                    Thread.sleep((long)1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                moveEnemiesToPlayer();
            }
        });
        movePlayer.start();
        generateEdgesForPathFinding(); //Se crean todas las aristas entre los vértices.
        trackPlayer.start();
        draw.start();
        shoot.start();
        // Combine the images onto a single image
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mapImage = largeCanvas.snapshot(null, null);
            }
        });
        //mediaPlayer.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File directory = new File("src/main/resources/numbers");
        File[] allFiles = directory.listFiles();
        assert allFiles != null;
        for (File file : allFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                int filePos = Integer.parseInt(file.getName().split("\\.")[0]);
                Image image = ImageLoader.loadImage("numbers/" + fileName);
                images.put(filePos, image);
            }
        }
        changeMap(1);
        gc.drawImage(player.getSprite().getImage(), player.getxPosition(), player.getyPosition());
        canvas.setOnMouseMoved(this::handleMouseMove);
        canvas.setFocusTraversable(true);
        winOrLose.start();
    }

    Thread winOrLose = new Thread(()->{
        while (currentLevel <= 3){
            try {
                Thread.sleep((long)16.66);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(win){
                currentLevel++;
                if(currentLevel == 4){
                    gc.drawImage(ImageLoader.loadImage("ui/confetti.png"), 0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);    
                }
                gc.drawImage(ImageLoader.loadImage("ui/confetti.png"), 0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);
                gc.drawImage(ImageLoader.loadImage("ui/nextLevel.png"), SCREEN_WIDHT/2-200, SCREEN_HEIGHT/2-200, 400, 400);
                while (!nextLevel){
                    try {
                        Thread.sleep((long)16.66);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                win = false;
                nextLevel = false;
                changeMap(currentLevel);
            } else if (lost){
                gc.drawImage(ImageLoader.loadImage("ui/BloodOverlay.png"), 0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);
                gc.drawImage(ImageLoader.loadImage("ui/retry.png"), SCREEN_WIDHT/2-200, SCREEN_HEIGHT/2-200, 400, 400);
                while (!retry){
                    try {
                        Thread.sleep((long)16.66);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                lost = false;
                retry = false;
                changeMap(currentLevel);
            }
        }
    });

    public void generateEdgesForPathFinding(){
        ArrayList<Vertex<Intersection>> vertexList = pathFindingGraph.vertexList;
        for(Vertex<Intersection> v : vertexList) {
            int i = v.getValue().getiPosition();
            int j = v.getValue().getjPosition();
            if(!(j-1 == -1)) {
                if (map[i][j - 1] == 14 || map[i][j - 1] == 56) {
                    pathFindingGraph.insertEdge(v, intersections.get(i + " " + (j - 1)), 0);
                }
            }

            if(!(j+1 >= map[i].length)){
                if(map[i][j+1] == 14 || map[i][j+1] == 56){
                    pathFindingGraph.insertEdge(v,intersections.get(i+" "+(j+1)),0);
                }
            }
            if(map[i-1][j] == 14 || map[i-1][j] == 56){
                pathFindingGraph.insertEdge(v, intersections.get((i+1)+" "+j),0);
            }
            if(map[i+1][j] == 14 || map[i+1][j] == 56){
                pathFindingGraph.insertEdge(v, intersections.get((i-1)+" "+j),0);
            }
        }
    }

    public void moveEnemiesToPlayer(){
        calculateCurrentPlayerVertex();
        if(currentPlayerVertex == null) {
            System.out.println("No vertex found");
            return;
        }
        currentPath = pathFindingGraph.BFS(currentPlayerVertex);
    }

    private void calculateCurrentPlayerVertex(){
        int playerX = player.getxPosition();
        int playerY = player.getyPosition();
        ArrayList<Intersection> intersectionList = new ArrayList<>();
        for (Vertex<Intersection> vertex : pathFindingGraph.vertexList) { //Se calcula la distancia del jugador a cada
            //vértice
            double distance = calculateDistance(playerX, vertex.getValue().getxPosition(), playerY, vertex.getValue().getyPosition());
            vertex.getValue().setDistanceToPlayer(distance);
            intersectionList.add(vertex.getValue());
        }
        intersectionList.sort(Intersection::compareToDistance); //Se ordenan los vértices con base en sus
        //distancias de manera ascendente (orden creciente)
        boolean foundVertex = false;
        Vertex<Intersection> vertex = null;
        for(int i = 0; i < intersectionList.size() && !foundVertex; i++){
            Intersection intersection = intersectionList.get(i);
            Line line = new Line(playerX, playerY, intersection.getxPosition(), intersection.getyPosition());
            //Línea que representa las coordenadas del jugador hasta las coordenadas del vértice intersección.
            boolean lineIntersects = false;
            //Booleano que comprueba si la linea interseca con alguna de las colisiones.
            for(int j = 0; j < collisions.size() && !lineIntersects; j++){
                if(line.intersects(collisions.get(j).getBoundsInLocal())){//Se comprueba si la línea interseca
                    //con los límites del objeto colisión actual.
                    lineIntersects = true;
                }
            }
            if(!lineIntersects) { //La línea no interseca con ninguna de las colisiones.
                foundVertex = true; //Se ha encontrado un vértice válido.
                vertex = intersections.get(intersection.getiPosition() + " " + intersection.getjPosition());
            }
        }
        if(vertex != null){ //Se encuentra un vértice válido que no interseca con ninguna colisión por donde
            //puede pasar una ruta o un camino válido hacia el punto.
            currentPlayerVertex = vertex;
        }
    }

    private double calculateDistance(int x1, int x2, int y1, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void explodeRpg(Bullet bullet){ //quiero ver como vas a hacer el explotar
        //TODO: LOS COMENTS SI TE ACUERDAS En español, yo ya estoy cabeceando mucho, yo creo que me despierto tipo 5 am o por ahí para terminar de estudiar el codigo. :D
        Circle explosion = new Circle(bullet.getxPosition(), bullet.getyPosition(), 100);
        Thread keepExplosion = new Thread(()->{
            ImageView explosionRpg = new ImageView(explosionImage);
            explosionRpg.setX(explosion.getCenterX()-100);
            explosionRpg.setY(explosion.getCenterY()-100);
            explosions.add(explosionRpg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            explosions.remove(explosionRpg);
        });
        keepExplosion.start();
        for(int i = 0 ; i < enemies.size(); i++){
            Enemy enemy = enemies.get(i);
            if(explosion.intersects(enemy.getHitbox().getBoundsInLocal())){
                enemy.takeDamage(10);
            }
        }
        ArrayList<Rectangle> wallsToDestroy = new ArrayList<>();
        for(int i = 0; i < collisions.size(); i++){
            Rectangle wall = collisions.get(i);
            if(explosion.intersects(wall.getBoundsInLocal())){
                int iPosition = (int) wall.getY()/32;
                int jPosition = (int) wall.getX()/32;
                boolean canBeRemoved = true;
                if(map[iPosition][jPosition-1] == 67){
                    canBeRemoved = false;
                }
                if(!(jPosition+1 >= map[iPosition].length)){
                    if(map[iPosition][jPosition+1] == 67){
                        canBeRemoved = false;
                    }
                }
                if(map[iPosition-1][jPosition] == 67){
                    canBeRemoved = false;
                }
                if(map[iPosition+1][jPosition] == 67){
                    canBeRemoved = false;
                }
                if(canBeRemoved) wallsToDestroy.add(wall);
            }
        }
        for (Rectangle wall : wallsToDestroy){
            collisions.remove(wall);
            int iPosition = (int) wall.getY()/32;
            int jPosition = (int) wall.getX()/32;
            map[iPosition][jPosition] = 14;
        }
        if(wallsToDestroy.isEmpty()) return;
        Canvas largeCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext largeGC = largeCanvas.getGraphicsContext2D();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int number = map[row][col];
                if(number == 56 || number == 14){
                    Vertex<Intersection> vertex = new Vertex<>(new Intersection(32*col, 32*row));
                    if(intersections.get(vertex.getValue().getiPosition() + " " + vertex.getValue().getjPosition()) == null){
                        pathFindingGraph.insertVertex(vertex);
                        intersections.put(vertex.getValue().getiPosition() + " " + vertex.getValue().getjPosition(),vertex);
                    }
                }
                Image image = images.get(number);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(32);
                imageView.setFitHeight(32);
                largeGC.drawImage(imageView.getImage(), 32*col, 32*row);
            }
        }
        generateEdgesForPathFinding();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mapImage = largeCanvas.snapshot(null, null);
            }
        });
    }

    public void retry(){
        if(!lost) return;
        retry = true;
    }

    public void nextLevel() {
        if(!win) return;
        nextLevel = true;
    }

    public void movePlayerW(){
        player.movePlayerW();
    }

    public void movePlayerA() {
        player.movePlayerA();
    }

    public void movePlayerS(){
        player.movePlayerS();
    }

    public void movePlayerD(){
        player.movePlayerD();
    }

    public void stopMovePlayerW(){
        player.stopMovePlayerW();
    }

    public void stopMovePlayerA() {
        player.stopMovePlayerA();
    }

    public void stopMovePlayerS(){
        player.stopMovePlayerS();
    }

    public void stopMovePlayerD(){
        player.stopMovePlayerD();
    }

    public void handleMouseMove(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        int offsetX = player.getOffsetX();
        int offsetY = player.getOffsetY();
        int x = SCREEN_WIDHT/2;
        int y = SCREEN_HEIGHT/2;
        if(offsetX < 0){
            x += offsetX;
        }
        if(offsetY < 0){
            y += offsetY;
        }
        if(offsetX > CANVAS_WIDTH - SCREEN_WIDHT){
            x += Math.abs(offsetX - (CANVAS_WIDTH - SCREEN_WIDHT));
        }
        if(offsetY > CANVAS_HEIGHT - SCREEN_HEIGHT){
            y += Math.abs(offsetY - (CANVAS_HEIGHT - SCREEN_HEIGHT));
        }
        angle = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x));
        player.getSprite().setRotate(angle);
    }

    public void changeToFirstGun(){
        currentGun = 1;
    }

    public void changeToSecondGun(){
        currentGun = 2;
    }

    public void shoot(){
        shooting = true;
    }

    public void stopShooting(){
        shooting = false;
    }

}