/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Marko
 */
public class Board extends GridPane {
    ArrayList<Checker> blueCheckers = new ArrayList<>();
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    
    static public Tile[][] tiles = new Tile[8][8];
    
    Board() {        
        draw();
        populate();
        showCheckers();
    }
    
    private void draw() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        addTile("red", i, j);
                    } else {
                        addTile("black", i, j);
                    }
                } else {
                    if(j % 2 == 0) {
                        addTile("black", i, j);
                    } else {
                        addTile("red", i, j);
                    }
                }
            }
        }
    }
    
    private void populate() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 1)
                        addChecker("white", j, i);
                } else {
                    if(j % 2 == 0)
                        addChecker("white", j, i);
                }                
            }
        }
        
        for(int i = 7; i > 4; i--) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 1)
                        addChecker("blue", j, i);
                } else {
                    if(j % 2 == 0)
                        addChecker("blue", j, i);
                }
            }
        }
    }
    
    private void addTile(String colour, int x, int y) {
        Color tileColour = Color.BLACK;
        if(colour.equals("red"))
            tileColour = Color.RED;
        
        Tile tile = new Tile(75, 75, tileColour, x, y, tiles);
        add(tile, x, y);
        tiles[x][y] = tile;
    }
    
    private void addChecker(String colour, int x, int y) {
        Color checkerColour = Color.BLUE;
        if(colour.equals("white"))
            checkerColour = Color.WHITE;
        
        Tile tile = tiles[x][y];
        Checker checker = new Checker(75/2, checkerColour, tile);        
        tile.placeChecker(checker);
        
        if(colour.equals("blue")) {
            blueCheckers.add(checker);
        } else {
            whiteCheckers.add(checker);
        }
    }
    
    public void showCheckers() {        
        for(Tile[] tileRow : tiles) {
            for(Tile tile : tileRow) {
                if(tile.hasChecker()) {
                    add(tile.checker, tile.x, tile.y);
                }
            }
        }
    }         
    
    public void resetTileColours() {
        for(Tile[] tileRow : tiles) {
            for(Tile tile : tileRow) {
                tile.resetColour();
            }
        }
    }
}
