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
    
    HashMap<Tile, ArrayList<Checker>> possibleMoves;
        
    
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
        
        possibleMoves = new HashMap<>();
    }
    
    public void showPossibleMoves() {              
        possibleMoves.keySet().forEach((tile) -> {
            tile.setColour(Color.GREEN);
        });
    }
    
    public Set<Tile> getPossibleMoves() {
        return possibleMoves.keySet();
    }
    
    public boolean isTakingMove(Tile tile) {
        if(getTakenCheckers(tile) == null)
            return false;
        
        return !getTakenCheckers(tile).isEmpty();
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
        possibleMoves.clear();
        calculateMovesForTile(currentTile);
    }
    
    private void calculateMovesForTile(Tile tile) {
        if(tile == null)
            return;
        
        ArrayList<Checker> enemyNeighbours = getEnemyNeighbours(tile);
        if(enemyNeighbours.isEmpty() || !hasTakingMove(tile, enemyNeighbours)) {
            addForwardMove(tile);
        } else {
            enemyNeighbours.forEach((Checker c) -> {                
                if(canTake(tile, c)) {
                    //
                    c.currentTile.setColour(Color.YELLOW);
                    //
                   addMove(tile, c);
                   Tile nextMoveTile = tile.getAdjacentTile(c.currentTile);
//                   calculateMovesForTile(nextMoveTile);
               } 
            });  
        }     
    }
    
    private boolean hasTakingMove(Tile fromTile, ArrayList<Checker> enemyNeighbours) {
        for(Checker c : enemyNeighbours) {
            if(canTake(fromTile, c))
                return true;
        }
        
        return false;
    }
    
    private void addForwardMove(Tile tile) {
        ArrayList<Tile> surroundingTiles = tile.getSurroundingTiles();
        if(isKing) {
            surroundingTiles.forEach((Tile t) -> {
                if(!t.hasChecker())
                    addMove(t, null);
            });
        } else {
            if(movingUp) {
                surroundingTiles.forEach((Tile t) -> {
                    if(tile.y > t.y && !t.hasChecker())
                            addMove(t, null);
                });
            } else {
                surroundingTiles.forEach((Tile t) -> {
                    if(tile.y < t.y && !t.hasChecker())
                        addMove(t, null);
                });
            }
        }
    }
    
    private ArrayList<Checker> getEnemyNeighbours(Tile tile) {
        ArrayList<Checker> neighbours = new ArrayList<>();
        ArrayList<Tile> neighbourTiles = tile.getSurroundingTiles();
        
        neighbourTiles.forEach((Tile t) -> {
            if(t.hasChecker() && t.checker.colour == opposingColour)
                neighbours.add(t.checker);
        });
        
        return neighbours;
    }
    
    private boolean canTake(Tile fromTile, Checker checker) {
        Tile moveTile = fromTile.getAdjacentTile(checker.currentTile);        
        
        if(moveTile != null && !moveTile.hasChecker()) {
            if(isKing)
                return true;
            
            if(movingUp) {
                if(fromTile.y > checker.currentTile.y)
                    return true;
            } else {
                if(fromTile.y < checker.currentTile.y)
                    return true;
            }
        }
        
        return false;
    }
    
    private void addMove(Tile tile, Checker capturedChecker) {
        if(capturedChecker == null) {
            possibleMoves.put(tile, null);
        } else {
            addCaptureMove(tile, capturedChecker);
        }
    }
    
    private void addCaptureMove(Tile previousTile, Checker capturedChecker) {
        Tile moveTile = previousTile.getAdjacentTile(capturedChecker.currentTile);
        
        if(possibleMoves.get(moveTile) == null) {
            possibleMoves.put(moveTile, new ArrayList<>());
        }
        
        possibleMoves.get(moveTile).add(capturedChecker);
//        
//        if(possibleMoves.get(previousTile) == null) {
//            possibleMoves.get(moveTile).add(capturedChecker);
//        } else {
//            possibleMoves.get(previousTile).forEach((Checker c) -> {
//                possibleMoves.get(moveTile).add(c);
//            });
//            possibleMoves.get(moveTile).add(capturedChecker);
//        }
    }      
        
    public boolean hasTurn(boolean turn) {
        // dont allow move if it's not my colour's turn
        if(!turn && !isBlue() || turn && isBlue()) {
            return false;
        }
        
        return true;
    }
    
    public void performTakingMove(Tile tile) {
        for(Checker checker : possibleMoves.get(tile)) {
            checker.currentTile.removeChecker();
        }
    }
    
    public ArrayList<Checker> getTakenCheckers(Tile tile) {
        return possibleMoves.get(tile);
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
