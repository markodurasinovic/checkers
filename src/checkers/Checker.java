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
    HashMap<Tile, Tile> currentPossibleMoves;
    HashMap<Tile, Tile> possibleTakingMoves;
    Color colour, opposingColour;
    boolean canCapture, justCaptured, isKing;  
    ArrayList<Checker> takenCheckers;
    ArrayList<Tile> jumpedTiles;
    
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        currentTile = tile;
        
        movingUp = fill == Color.BLUE;
        opposingColour = fill == Color.BLUE ? Color.WHITE : Color.BLUE;
        colour = (Color) fill;
        
        currentPossibleMoves = new HashMap<>();
        possibleTakingMoves = new HashMap<>();
        canCapture = false;
        justCaptured = false;
        isKing = false;
        
        takenCheckers = new ArrayList<>();
        jumpedTiles = new ArrayList<>();
    }
    
    public void showPossibleMoves() {              
        currentPossibleMoves.keySet().forEach((tile) -> {
            tile.setColour(Color.GREEN);
        });
    }
    
    public Set<Tile> getPossibleMoves() {
        return currentPossibleMoves.keySet();
    }
    
    public boolean isTakingMove(Tile tile) {
        return getMove(tile)[1] != null;
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
    
    public boolean canMove() {
        calculatePossibleMoves();
        return !currentPossibleMoves.isEmpty();
    }      
    
    public void calculatePossibleMoves() {
        currentPossibleMoves.clear();
        possibleTakingMoves.clear();        
        jumpedTiles.clear();
        
        getMoves(currentTile);
        
        if(!possibleTakingMoves.isEmpty()) {
            currentPossibleMoves = possibleTakingMoves;
        }
    }
    
    public void getMoves(Tile tile) {
        if(isKing) {
            addPossibleMove1(tile, -1, -1);
            addPossibleMove1(tile, 1, -1);
            addPossibleMove1(tile, -1, 1);
            addPossibleMove1(tile, 1, 1);
        } else {
            if(movingUp) {
                addPossibleMove1(tile, -1, -1);
                addPossibleMove1(tile, 1, -1);
            } else {            
                addPossibleMove1(tile, -1, 1);
                addPossibleMove1(tile, 1, 1);
            }
        }     
    }
    
    private void addPossibleMove1(Tile tile, int horDir, int vertDir) {
        Tile moveTile = Tile.getTile(tile.x + horDir, tile.y + vertDir);
        if(moveTile != null) {
            if(!moveTile.hasChecker()) {
                currentPossibleMoves.put(moveTile, null);
            } else if(moveTile.checker.colour == opposingColour) {
                addPossibleTakingMoves(tile, moveTile);
            }
        }
    }
    
    private void addPossibleTakingMoves(Tile tile, Tile moveTile) {
        int horDir = (tile.x - moveTile.x) * -1;
        if(isKing) {
            addTakingMove(moveTile, horDir, -1);
            addTakingMove(moveTile, horDir, 1);
        } else {
            if(movingUp) {
                addTakingMove(moveTile, horDir, -1);
            } else {
                addTakingMove(moveTile, horDir, 1);
            }                
        }
    }
    
    private void addTakingMove(Tile moveTile, int horDir, int vertDir) {
        Tile tile = Tile.getTile(moveTile.x + horDir, moveTile.y + vertDir);
        if(tile != null) {
            if(!tile.hasChecker()) {
                possibleTakingMoves.put(tile, moveTile);
                jumpedTiles.add(moveTile);
                getMoves(tile);
            }
        }
    }
            
    public boolean hasTurn(boolean turn) {
        // dont allow move if it's not my colour's turn
        if(!turn && !isBlue() || turn && isBlue()) {
            return false;
        }
        
        return true;
    }
    
    public void performTakingMove(Tile tile) {
        takenCheckers.clear();
        for(Tile jT : jumpedTiles) {
            Checker takenChecker = jT.checker;
            takenChecker.currentTile.removeChecker();
            takenCheckers.add(takenChecker);
        }
    }
    
    public ArrayList<Checker> getTakenCheckers() {
        return takenCheckers;
    }
    
    public void move(Tile tile) {
        if(!tile.hasChecker()) {
            currentTile.removeChecker();
            currentTile = tile;
            currentTile.placeChecker(this);
            justCaptured = false;
        }        
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
