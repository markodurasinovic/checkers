/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;

/**
 *
 * @author Marko
 */
public class Player {
    ArrayList<Checker> checkers;
    String colour;
    ArrayList<Move> allMoves;
    
    
    Player(ArrayList<Checker> checkers, String colour) {
        this.checkers = checkers;
        this.colour = colour;
        
        allMoves = new ArrayList<>();
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
            for(Move move : c.possibleMoves) {
                allMoves.add(move);
            }
        }
        forceCaptureMoves();
    }
    
    private void forceCaptureMoves() {
        if(!hasTakingMove()) {
            return;
        }
        
        for(Checker c : checkers) {
            c.possibleMoves.removeIf(Move::noCapture);
        }
        allMoves.removeIf(Move::noCapture);        
    }
    
    private boolean hasTakingMove() {
        for(Move m : allMoves) {
            if(!m.noCapture()) {
                return true;
            }
        }
        
        return false;
    }
}
