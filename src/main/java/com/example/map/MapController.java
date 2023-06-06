package com.example.map;

import com.example.datastructures.Graph.AdjacencyListGraph.AdjacencyListGraph;
import com.example.datastructures.Graph.Graph.*;
import com.example.model.*;
import com.example.model.Vector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MapController implements Initializable {
    @FXML
    public Canvas canvas;
    public Pane pane;
    public Label keysPositions;
    private GraphicsContext gc;
    private Image mapImage;
    private final int SCREEN_WIDHT = 1920;
    private final int SCREEN_HEIGHT = 1080;

    private int CANVAS_WIDTH;
    private int CANVAS_HEIGHT;

    private final ArrayList<Rectangle> collisions = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Key> keys = new ArrayList<>();
    private Player player;
    private int[][] map;
    private int numberOfKeys = 3;
    private HashMap<Integer, Image> images = new HashMap<>();
    private AdjacencyListGraph<Intersection> pathFindingGraph = new AdjacencyListGraph<>(false, false);
    private HashMap<String, Vertex<Intersection>> intersections = new HashMap<>();
    private DijkstraResult<Intersection> currentPath = null;
    private Vertex<Intersection> currentPlayerVertex = null;
    boolean lost = false;
    boolean win = false;

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
        map = readMatrix("matrix.txt");
        CANVAS_WIDTH = map[0].length*32;//Se obtiene el total de pixeles en el ancho, teniendo en cuenta que cada
        //rectángulo será de 32*32.
        CANVAS_HEIGHT = map.length*32;
        gc = canvas.getGraphicsContext2D();
        Random random = new Random();
        for (int row = 0; row < map.length; row++) {//Se recorre la matriz para encontrar colisiones
            for (int col = 0; col < map[row].length; col++) {
                int number = map[row][col];
                if(number != 14 && number != 56 && number != 67){ //Si no es alguno de estos números, hay una
                    //colisión
                    Rectangle obstacle = new Rectangle(32*col, 32*row, 32, 32);
                    collisions.add(obstacle);
                }
                if(number == 56){ //Si el cuadro está marcado con una A
                    int randomNumber = random.nextInt(20); //Se toma la probabilidad 1/20 por vértice para
                    //añadir un enemigo.
                    Enemy enemy = null;
                    Vertex<Intersection> vertex = new Vertex<>(new Intersection(32*col, 32*row));
                    //Se crea un vértice como intersección en ese cuadro.
                    if (randomNumber == 0) {
                        enemy = new Enemy(32*col, 32*row, "enemies/zombie.png", 3, 0, vertex);
                        enemies.add(enemy);
                    }
                    pathFindingGraph.insertVertex(vertex); //Se añade la intersección (vértice) dentro del grafo.
                    intersections.put(vertex.getValue().getiPosition() + " " + vertex.getValue().getjPosition(),vertex);
                }
                Image image = images.get(number);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(32);
                imageView.setFitHeight(32);
                gc.drawImage(imageView.getImage(), 32*col, 32*row);
            }
        }
        for(int i = 0; i < numberOfKeys; i++){ //Código que genera en lugares aleatorios las llaves en el mapa.
            //Las llaves son intersecciones.
            int randomIndex = random.nextInt(pathFindingGraph.vertexList.size());
            Vertex<Intersection> vertex = pathFindingGraph.vertexList.get(randomIndex);
            if(!vertex.getValue().isHasKey()){
                vertex.getValue().setHasKey(true);
                Key key = new Key(vertex.getValue().getxPosition(), vertex.getValue().getyPosition(), "specialElements/key.png", vertex.getValue(), i + "");
                keys.add(key);
            } else {
                i--;
            }
        }
        // Combine the images onto a single image
        mapImage = canvas.snapshot(null, null);
        player = new Player(2050, 2050, "player/playerSprite.png", 5, 0);
        gc.drawImage(player.getSprite().getImage(), player.getxPosition(), player.getyPosition());
        draw.start();
        movePlayer.start();
        generateEdgesForPathFinding(); //Se crean todas las aristas entre los vértices.
        trackPlayer.start();
    }

    public void generateEdgesForPathFinding(){
        ArrayList<Vertex<Intersection>> vertexList = pathFindingGraph.vertexList;
        for(Vertex<Intersection> v : vertexList){
            int i = v.getValue().getiPosition(); //Posiciones del vértice en el mapa.
            int j = v.getValue().getjPosition();
            boolean finishLoop = false;
            for(int loop = j+1; loop < map[i].length && !finishLoop; loop++){
                if(map[i][loop] != 14){//Se pregunta si es diferente al cuadro del camino.
                    if(map[i][loop] == 56){//Se pregunta si es igual a una A.
                        pathFindingGraph.insertEdge(v, intersections.get(i+ " " + loop),loop-j+1);
                    }
                    finishLoop = true;
                }
            }
            finishLoop = false;
            for(int loop = j-1; loop >= 0 && !finishLoop; loop--){
                if(map[i][loop] != 14){
                    if(map[i][loop] == 56){
                        pathFindingGraph.insertEdge(v, intersections.get(i + " " + loop),j-loop+1);
                    }
                    finishLoop = true;
                }
            }
            finishLoop = false;
            for(int loop = i+1; loop < map.length && !finishLoop; loop++){
                if(map[loop][j] != 14){
                    if(map[loop][j] == 56){
                        pathFindingGraph.insertEdge(v, intersections.get(loop + " " + j),loop-i+1);
                    }
                    finishLoop = true;
                }
            }
            finishLoop = false;
            for(int loop = i-1; loop >= 0 && !finishLoop; loop--){
                if(map[loop][j] != 14){
                    if(map[loop][j] == 56){
                        pathFindingGraph.insertEdge(v, intersections.get(loop + " " + j),i-loop+1);
                    }
                    finishLoop = true;
                }
            }
        }
    }

    public void moveEnemiesToPlayer(){
        calculateCurrentPlayerVertex();
        if(currentPlayerVertex == null) {
            System.out.println("No vertex found");
            return;
        }
        currentPath = pathFindingGraph.dijkstra(currentPlayerVertex); //Se usa Dijkstra para hallar el
        //camino mínimo desde el enemigo hasta el jugador.
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

    public void win(){

    }

    public void lose(){

    }

    Thread draw = new Thread(()->{
        double offsetX = 0;
        double offsetY = 0;
        while (!win && !lost){
            try {
                Thread.sleep((long) 1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            offsetX = player.getxPosition() - SCREEN_WIDHT / 2;
            offsetY = player.getyPosition() - SCREEN_HEIGHT / 2;
            if(offsetX > CANVAS_WIDTH - SCREEN_WIDHT) offsetX -= Math.abs(offsetX-(CANVAS_WIDTH-SCREEN_WIDHT));
            if(offsetY > CANVAS_HEIGHT - SCREEN_HEIGHT) offsetY -= Math.abs(offsetY-(CANVAS_HEIGHT - SCREEN_HEIGHT));
            if(offsetX < 0) offsetX = 0;
            if(offsetY < 0) offsetY = 0;
            gc.drawImage(mapImage, offsetX, offsetY, 1980, 1080, offsetX, offsetY, 1980, 1080);
            for(Key key : keys){
                gc.drawImage(key.getSprite().getImage(), key.getxPosition(), key.getyPosition());
            }
            gc.drawImage(player.getSprite().getImage(), player.getxPosition(), player.getyPosition());
            for(Enemy enemy : enemies){
                gc.drawImage(enemy.getSprite().getImage(), enemy.getxPosition(), enemy.getyPosition());
            }
            if(offsetX > 0 && offsetX < CANVAS_WIDTH - SCREEN_WIDHT) canvas.setTranslateX(-offsetX);
            if(offsetY > 0 && offsetY < CANVAS_HEIGHT - SCREEN_HEIGHT) canvas.setTranslateY(-offsetY);
            if(keys.size() == 0){
                win = true;
            }
        }
        if(win){
            gc.drawImage(ImageLoader.loadImage("ui/Confetti.png"),offsetX, offsetY, SCREEN_WIDHT, SCREEN_HEIGHT);
        } else {
            gc.drawImage(ImageLoader.loadImage("ui/BloodOverlay.png"), offsetX, offsetY, SCREEN_WIDHT, SCREEN_HEIGHT);
        }
    });

    public void calculateDistancesKeys() {
        calculateCurrentPlayerVertex();
        FloydWarshalResult<Intersection> result = pathFindingGraph.floydWarshall(); //Se sua Floyd-Warshall
        //para encontrar el camino mínimo entre el jugador y las llaves del juego.
        for(Key key : keys){
            Vertex<Intersection> keyVertex = intersections.get(key.getIntersection().getiPosition() + " " + key.getIntersection().getjPosition());
            //Se obtiene el vértice de intersección de cada llave.
            int keyPos = pathFindingGraph.vertexList.indexOf(keyVertex); //Se buscan las posiciones de la llave y
            //del jugador en la lista de vértices.
            int playerPos = pathFindingGraph.vertexList.indexOf(currentPlayerVertex);
            if(playerPos == -1) System.out.println("no player?");
            if(keyPos == -1) System.out.println("no key");
            double totalDistance = 0;
            Vertex<Intersection> previous = result.getPrevious()[playerPos][keyPos];
            //Se obtiene el vértice anterior en el camino más corto desde el jugador hasta la llave, usando la
            //matriz de previos que está en el resultado de Floyd-Warshall.
            while(previous != null) {
                totalDistance += result.getDistances()[playerPos][keyPos]; //Se suma la distancia entre el
                //jugador y la llave actual.
                keyPos = pathFindingGraph.vertexList.indexOf(previous);
                //Se actualiza la posición de la llave con la posición del vértice anterior.
                previous = result.getPrevious()[playerPos][keyPos];
                //Se actualiza el valor de previous con el vértice anterior al vértice actual.
            }
            //totalDistance += calculateDistance(currentPlayerVertex.getValue().getxPosition(), player.getxPosition(), currentPlayerVertex.getValue().getyPosition(), player.getyPosition());
            key.setDistanceToPlayer((int) totalDistance); // Se actualiza la distancia desde la llave al
            //jugador.
        }
        keys.sort(Key::compareToDistance); //Se organiza las distancias de las llaves de menor a mayor distancia
        //hasta el jugador.
        String msj = "";
        for(Key key : keys){
            msj += "Shortest path to reach key "  + key.getId() + ": " + key.getDistanceToPlayer() + "\n";
            //Mensaje con distancia más corta para alcanzar la llave actual.
        }
        Collections.reverse(keys);
        if(keys.size() > 1){ //Si hay más de una llave.
            msj += "Shortest path to keys in following order:";
            for(Key key : keys){
                msj += " " + key.getId();
            }
            msj += ": ";
            double distance = 0;
            for(int i = 0; i < keys.size()-1; i++){
                Key key = keys.get(i);
                Key nextKey = keys.get(i+1);
                //Se obtienen los vértices de intersección de la llave actual y de la llave siguiente.
                Vertex<Intersection> keyVertex = intersections.get(key.getIntersection().getiPosition() + " " + key.getIntersection().getjPosition());
                Vertex<Intersection> nextKeyVertex = intersections.get(nextKey.getIntersection().getiPosition() + " " + nextKey.getIntersection().getjPosition());
                int keyPos = pathFindingGraph.vertexList.indexOf(keyVertex);
                int nextKeyPos = pathFindingGraph.vertexList.indexOf(currentPlayerVertex);
                double totalDistance = 0;
                //Se obtiene el vértice anterior en el camino más corto desde la siguiente llave hasta la llave
                //actual usando la matriz de previous.
                Vertex<Intersection> previous = result.getPrevious()[nextKeyPos][keyPos];
                while(previous != null) {
                    totalDistance += result.getDistances()[nextKeyPos][keyPos]; //Se suma la distancia entre la
                    //llave actual y la siguiente.
                    keyPos = pathFindingGraph.vertexList.indexOf(previous);
                    //Se actualiza la posición de la llave actual con la posición del vértice anterior.
                    previous = result.getPrevious()[nextKeyPos][keyPos];
                    //Se actualiza el valor de previous con el vértice anterior al vértice actual.
                }
                distance += totalDistance;
                //Se añade la distancia total recorrida entre las llaves.
            }
            msj += distance;
            //Se concatena la distancia total recorrida para alcanzar las llaves en el orden especificado.
        }

        keysPositions.setText(msj);
    }

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
            for(Rectangle obstacle : collisions){ //Por cada colisión
                if(player.getHitbox().intersects(obstacle.getBoundsInLocal())){
                    //Si el box del jugador inserseca con algún box de un obstáculo, se vuelve a poner al
                    //jugador en la posición anterior.
                    player.setxPosition(oldPlayerX);
                    player.setyPosition(oldPlayerY);
                }
            }
            for(int i = 0; i < keys.size(); i++){
                Key key = keys.get(i);
                if(player.getHitbox().intersects(key.getHitbox().getBoundsInLocal())){
                    player.setKeysCollected(player.getKeysCollected() + 1);
                    keys.remove(key);
                    System.out.println("found key");
                }
            }
            oldPlayerX = player.getxPosition();
            oldPlayerY = player.getyPosition();
            for(Enemy enemy : enemies){
                enemy.moveEnemy(currentPath, player, pathFindingGraph.vertexList, collisions, intersections);
                if(player.getHitbox().intersects(enemy.getHitbox().getBoundsInLocal())){
                    lost = true;
                }
            }
        }
    });

    Thread trackPlayer = new Thread(() -> {
        while (!win && !lost){
            try {
                Thread.sleep((long)10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            moveEnemiesToPlayer();
        }
    });



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

}