/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
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
//    ArrayList<Tile> currentPossibleMoves;
    HashMap<Tile, Tile> currentPossibleMoves;
    Color colour, opposingColour;
    boolean canCapture;
    boolean isKing;
    
    
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        currentTile = tile;
        
        movingUp = fill == Color.BLUE;
        opposingColour = fill == Color.BLUE ? Color.WHITE : Color.BLUE;
        colour = (Color) fill;
        
        currentPossibleMoves = new HashMap<>();
        canCapture = false;
        isKing = false;
    }
    
    public void showPossibleMoves() {              
        currentPossibleMoves.keySet().forEach((tile) -> {
            tile.setColour(Color.GREEN);
        });
    }
    
    public Set<Tile> getPossibleMoves() {
        return currentPossibleMoves.keySet();
    }
    
    public Tile[] getMove(Tile tile) {
        Tile[] move = new Tile[2];
        move[0] = tile;
        move[1] = currentPossibleMoves.get(tile);
        
        return move;
    }
    
    public void crown() {
        isKing = true;
        if(colour == Color.BLUE) {
            setColour(Color.DARKBLUE);
        } else {
            setColour(Color.GRAY);
        }
    }
    
    private void setColour(Color colour) {
        setFill(colour);
    }
        
    public void calculatePossibleMoves() {
        currentPossibleMoves.clear();
        canCapture = false;
        
        if(isKing) {
            getMove(-1, -1);
            getMove(1, -1);
            getMove(-1, 1);
            getMove(1, 1);
        } else {
            if(movingUp) {
                getMove(-1, -1);
                getMove(1, -1);
            } else {            
                getMove(-1, 1);
                getMove(1, 1);
            }
        }     
        
        
        // Forcing a capturing move to be performed.
        if(canCapture) {
            currentPossibleMoves.values().removeIf(Objects::isNull);
        }        
    }
    
    private void getMove(int horizontalDirection, int verticalDirection) {
        Tile tile = Tile.getTile(currentTile.x + horizontalDirection, currentTile.y + verticalDirection);
       
        if(tile != null) {
            if(!tile.hasChecker()) {
                currentPossibleMoves.put(tile, null);
            } else if(tile.checker.colour == opposingColour) {
                Tile moveTile = getTakingMove(tile.x + horizontalDirection, tile.y + verticalDirection);
                if(moveTile != null) {
                    currentPossibleMoves.put(moveTile, tile);
                    canCapture = true;
                }
            }
        }
    }
    
    private Tile getTakingMove(int x, int y) {
        Tile tile = Tile.getTile(x, y);
        if(tile != null) {
            if(!tile.hasChecker()) {
                return tile;
            }
        }
        
        return null;
    }
    
    public void place(Tile tile) {
        currentTile = tile;
    }    
    
    public boolean isBlue() {
        return colour == Color.BLUE;
    }
    
    public boolean inKingsRow() {
        if(isBlue()) {
            return currentTile.y == 0;
        } else {
            return currentTile.y == 7;
        }
    }
}
