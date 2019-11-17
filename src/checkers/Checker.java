/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
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
    boolean movingUp, justMoved;
    ArrayList<Tile> currentPossibleMoves;
    
    
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        currentTile = tile;
        
        movingUp = fill == Color.BLUE;
        justMoved = false;
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
        int x = currentTile.x;
        int y = currentTile.y;
        
        if(movingUp) {
            possibleMoves.add(getPossibleMove(x - 1, y - 1));
            possibleMoves.add(getPossibleMove(x + 1, y - 1)); 
        } else {            
            possibleMoves.add(getPossibleMove(x - 1, y + 1));
            possibleMoves.add(getPossibleMove(x + 1, y + 1));
        }
        
        return possibleMoves;
    }
    
    private Tile getPossibleMove(int x, int y) {
        Tile tile = Tile.getTile(x, y);
        if(tile != null && !tile.hasChecker()) {
            return tile;
        }        
        
        return null;
    }
    
    public void place(Tile tile) {
        currentTile = tile;
        justMoved = true;
    }
    
    public boolean justMoved() {
        return justMoved;
    }
}
