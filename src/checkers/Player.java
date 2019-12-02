/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Marko
 */
public class Player {
    ArrayList<Checker> checkers;
    String colour;
    HashMap<Checker, ArrayList<Tile>> allMoves;
    
    
    Player(ArrayList<Checker> checkers, String colour) {
        this.checkers = checkers;
        this.colour = colour;
        
        allMoves = new HashMap<>();
    }
    
    public boolean hasLost() {
        return checkers.isEmpty() || !canMove();
    }
    
    private boolean canMove() {
        for(Checker c : checkers) {
            if(c.canMove())
                return true;
        }
        
        return false;
    }
    
    public boolean ownsChecker(Checker checker) {
        return checkers.contains(checker);
    }
    
    public void removeChecker(Checker checker) {
        checkers.remove(checker);
    }
    
    public void calculateAllMoves() {
        allMoves.clear();
        for(Checker c : checkers) {
            c.calculatePossibleMoves();
            for(Tile moveTile : c.getPossibleMoves()) {
                addMove(c, moveTile);
            }
        }
        forceCaptureMoves();
    }
    
    private void addMove(Checker checker, Tile tile) {
        if(allMoves.get(checker) == null)
            allMoves.put(checker, new ArrayList<>());
        
        allMoves.get(checker).add(tile);
    }
    
    private void forceCaptureMoves() {
        if(!hasTakingMove()) {
            return;
        }
        
        ArrayList<Checker> moveCheckers = new ArrayList<>();
        for(Checker c : allMoves.keySet()) {
            moveCheckers.add(c);
        }
        
        for(Checker c : moveCheckers) {
            if(c.hasNonTakingMoves()) {
                c.removeNonTakingMoves();
                allMoves.remove(c);
            }
        }
        
    }
    
    private boolean hasTakingMove() {
        for(Checker moveChecker : allMoves.keySet()) {
            for(Tile t : allMoves.get(moveChecker)) {
                if(moveChecker.isTakingMove(t)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
