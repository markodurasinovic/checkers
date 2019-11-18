/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Collections;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Marko
 */
public class Checker extends Circle {
    Tile currentTile;
    boolean movingUp;
    ArrayList<Tile> currentPossibleMoves;
    Color colour, opposingColour;
    
    
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        currentTile = tile;
        
        movingUp = fill == Color.BLUE;
        opposingColour = fill == Color.BLUE ? Color.WHITE : Color.BLUE;
        colour = (Color) fill;
        
//        System.out.println(currentTile.position);
    }
    
    public void showPossibleMoves() {              
        for(Tile tile : currentPossibleMoves) {
            tile.setFill(Color.GREEN);
        }
    }
    
    public void setPossibleMoves() {
        currentPossibleMoves = getPossibleMoves();
    }
    
    public void hidePossibleMoves() {
        for(Tile tile : currentPossibleMoves) {
            tile.setFill(tile.originalColour);
        }
    }
    
    private ArrayList<Tile> getPossibleMoves() {
        ArrayList<Tile> possibleMoves = new ArrayList<>();
        
        if(movingUp) {
            possibleMoves.add(getLeftMove(-1));
            possibleMoves.add(getRightMove(-1)); 
        } else {            
            possibleMoves.add(getLeftMove(1));
            possibleMoves.add(getRightMove(1));
        }
        
        // Removing 'null' moves i.e. moves which are illegal such as out of
        // board bounds or onto a tile containing another checker.
        possibleMoves.removeAll(Collections.singleton(null));
        
        return possibleMoves;
    }
    
    private Tile getLeftMove(int direction) {
        Tile tile = Tile.getTile(currentTile.x - 1, currentTile.y + direction);
        if(tile != null) {
            if(!tile.hasChecker()) {
                return tile;
            } else if(tile.checker.colour == opposingColour) {
                return getLeftTakingMove(direction, tile.x, tile.y);
            }
        }
        
        return null;
    }
    
    private Tile getLeftTakingMove(int direction, int x, int y) {
        Tile tile = Tile.getTile(x - 1, y + direction);
        if(tile != null) {
            if(!tile.hasChecker()) {
//                return getLeftMove(direction);
                return tile;
            } else {
                return null;
            }
        }
        
        return tile;
    }
    
    private Tile getRightMove(int direction) {
        Tile tile = Tile.getTile(currentTile.x + 1, currentTile.y + direction);
        if(tile != null) {
            if(!tile.hasChecker()) {
                return tile;
            } else if(tile.checker.colour == opposingColour) {
                return getRightTakingMove(direction, tile.x, tile.y);
            }
        }
        
        return null;
    }
    
    private Tile getRightTakingMove(int direction, int x, int y) {
        Tile tile = Tile.getTile(x + 1, y + direction);
        if(tile != null) {
            if(!tile.hasChecker()) {
//                return getRightMove(direction);
                return tile;
            } else {
                return null;
            }
        }
        
        return tile;
    }
    
//    private Tile getPossibleMove(int x, int y) {
//        Tile tile = Tile.getTile(x, y);
//        if(tile != null) {
//            if(!tile.hasChecker()) {
//                return tile;
//            } else {
//                
//            }
//        }
//        
//        
//        
//        return null;
//    }
    
    public void place(Tile tile) {
        currentTile = tile;
    }    
}
