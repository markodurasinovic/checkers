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
    Tile[][] tiles = new Tile[8][8];
    ArrayList<Checker> blackCheckers = new ArrayList<>();
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    
    
    Board() {        
        draw();
        populate();
    }
    
    private void draw() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        addTile("white", i, j);
                    } else {
                        addTile("black", i, j);
                    }
                } else {
                    if(j % 2 == 0) {
                        addTile("black", i, j);
                    } else {
                        addTile("white", i, j);
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
                        addChecker("black", j, i);
                } else {
                    if(j % 2 == 0)
                        addChecker("black", j, i);
                }
            }
        }
    }
    
    private void addTile(String colour, int x, int y) {
        Color tileColour = Color.BLACK;
        if(colour.equals("white"))
            tileColour = Color.RED;
        
        Tile tile = new Tile(75, 75, tileColour);
        add(tile, x, y);
        tiles[x][y] = tile;
    }
    
    private void addChecker(String colour, int x, int y) {
        Color tileColour = Color.BLUE;
        if(colour.equals("white"))
            tileColour = Color.WHITE;
        
        Checker checker = new Checker(75/2, tileColour, tiles[x][y]);
        add(checker, x, y);
        
        //fix this up
        if(colour.equals("black")) {
            blackCheckers.add(checker);
        } else {
            whiteCheckers.add(checker);
        }
    }
}
