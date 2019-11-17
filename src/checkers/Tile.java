/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
    Paint originalColour;
    
    Tile(double width, double height, Paint fill, int x, int y) {
        super(width, height, fill);
        this.x = x;
        this.y = y;
        originalColour = fill;
    }
    
    public void placeChecker(Checker c) {
        if(!hasChecker()) { 
            checker = c;
            checker.place(this);
        }
    }
    
    public void removeChecker() {
        checker = null;
    }
    
    public boolean hasChecker() {
        return checker != null;
    }
    
    static public Tile getTile(int x, int y) {
        if(x < 8 && y < 8 && x > -1 && y > -1) {
            return TILES[x][y];
        }
        
        return null;
    }
    
    
 }
