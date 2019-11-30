/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Marko
 */
public class Tile extends Rectangle {
    Board board;
    Checker checker;
    int x, y;
    static Tile[][] TILES = new Tile[8][8];
    Paint originalColour, colour;
    
    Tile(double width, double height, Paint fill, int x, int y) {
        super(width, height, fill);
        this.x = x;
        this.y = y;
        originalColour = (Color) fill;
        colour = (Color) fill;
    }
    
    public void placeChecker(Checker c) {
        checker = c;
    }
    
    public void removeChecker() {
        checker = null;
    }
    
    public boolean hasChecker() {
        return checker != null;
    }
    
    public ArrayList<Tile> getSurroundingTiles() {
        ArrayList<Tile> surroundingTiles = new ArrayList<>();
        
        surroundingTiles.add(getTile(x + 1, y + 1));
        surroundingTiles.add(getTile(x + 1, y - 1));
        surroundingTiles.add(getTile(x - 1, y + 1));
        surroundingTiles.add(getTile(x - 1, y - 1));
        
        surroundingTiles.removeIf(Objects::isNull);
        return surroundingTiles;
    }
    
    public Tile getAdjacentTile(Tile tile) {
        int horDir = (x - tile.x) * -1;
        int vertDir = (y - tile.y) * -1;
        
        return getTile(tile.x + horDir, tile.y + vertDir);
    }
    
    static public Tile getTile(int x, int y) {
        if(x < 8 && y < 8 && x > -1 && y > -1) {
            return TILES[x][y];
        }
        
        return null;
    }
    
    public void setColour(Color colour) {
        setFill(colour);
        this.colour = colour;
    }
    
    public void resetColour() {
        if(colour == originalColour)
            return;
        
        setFill(originalColour);
    }    
 }
