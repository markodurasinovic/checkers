/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
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
    Color colour, opposingColour;
    boolean isKing;  
    
    ArrayList<Move> possibleMoves;
        
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        currentTile = tile;
        
        movingUp = fill == Color.BLUE;
        opposingColour = fill == Color.BLUE ? Color.WHITE : Color.BLUE;
        colour = (Color) fill;
        
        isKing = false;
        
        possibleMoves = new ArrayList<>();
    }
    
    public void showPossibleMoves() {
        ArrayList<Tile> possibleMoveTiles = getPossibleMoveTiles();
        possibleMoveTiles.forEach((tile) -> {
            tile.setColour(Color.GREEN);
        });
    }
    
    public ArrayList<Tile> getPossibleMoveTiles() {
        ArrayList<Tile> possibleMoveTiles = new ArrayList<>();
        for(Move m : possibleMoves) {
            possibleMoveTiles.add(m.tile);
        }
        
        return possibleMoveTiles;
    }
    
    public boolean isTakingMove(Tile tile) {
        if(getTakenCheckers(tile) == null)
            return false;
        
        return !getTakenCheckers(tile).isEmpty();
    }
    
    public boolean hasNonTakingMoves() {
        for(Move m : possibleMoves) {
            if(m.noCapture())
                return true;
        }
        return false;
    }
    
    public void removeNonTakingMoves() {
        for(int i = 0; i < possibleMoves.size(); i++) {
            if(possibleMoves.get(i).noCapture())
                possibleMoves.remove(i);
        }
    }
    
    public void removeMove(Move move) {
        possibleMoves.remove(move);
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
        
        return !possibleMoves.isEmpty();
    }     
    
    public void calculatePossibleMoves() {
        possibleMoves.clear();
        calculateMovesForTile(currentTile);
    }
    
    private void calculateMovesForTile(Tile tile) {
        if(tile == null)
            return;
        
        ArrayList<Checker> enemyNeighbours = getEnemyNeighbours(tile);
        if(enemyNeighbours.isEmpty() || 
           !hasTakingMove(tile, enemyNeighbours) && !isTakingMove(tile)) {
            addForwardMove(tile);
        } else {
            enemyNeighbours.forEach((Checker c) -> {                
                if(canTake(tile, c)) {
                   addMove(tile, c);
                   Tile nextMoveTile = tile.getAdjacentTile(c.currentTile);
                   calculateMovesForTile(nextMoveTile);
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
            if(isKing && !alreadyVisited(moveTile))
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
    
    private boolean alreadyVisited(Tile tile) {
        for(Move m : possibleMoves) {
            if(m.tile == tile)
                return true;
        }
        
        return false;
    }
    
    private void addMove(Tile tile, Checker capturedChecker) {
        if(capturedChecker == null) {
            possibleMoves.add(new Move(this, tile));
        } else {
            addCaptureMove(tile, capturedChecker);
        }
    }
    
    private void addCaptureMove(Tile previousTile, Checker capturedChecker) {
        Tile moveTile = previousTile.getAdjacentTile(capturedChecker.currentTile);
        
        if(getMove(moveTile) == null) {
            possibleMoves.add(new Move(this, moveTile));
        }
        
        ArrayList<Checker> capturedCheckers = getTakenCheckers(moveTile);
        ArrayList<Checker> previouslyCapturedCheckers = getTakenCheckers(previousTile);
        if(previouslyCapturedCheckers != null) {
            for(Checker cc : previouslyCapturedCheckers) {
                capturedCheckers.add(cc);
            }
        }
        
        getMove(moveTile).addCapture(capturedChecker);
    }      
    
    public void performTakingMove(Tile tile) {
        for(Checker checker : getTakenCheckers(tile)) {
            // regicide
            if(checker.isKing)
                crown();
            //
            checker.currentTile.removeChecker();
            checker.currentTile = null;            
        }
    }
    
    public ArrayList<Checker> getTakenCheckers(Tile tile) {
        if(getMove(tile) != null) {
            return getMove(tile).capturedCheckers;
        }
        
        return null;
    }
    
    private Move getMove(Tile tile) {
        for(Move m : possibleMoves) {
            if(m.tile == tile) {
                return m;
            }
        }
        
        return null;
    }
    
    public void move(Tile tile) {
        if(!tile.hasChecker()) {
            currentTile.removeChecker();
            currentTile = tile;
            currentTile.placeChecker(this);
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
