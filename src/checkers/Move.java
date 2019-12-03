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
public class Move {
    Checker checker;
    Tile tile;
    Move parent;
    ArrayList<Move> children;
    ArrayList<Checker> capturedCheckers;
    
    Move(Checker checker, Tile tile) {
        this.checker = checker;
        this.tile = tile;
        capturedCheckers = new ArrayList<>();        
    }
    
    public void addCapture(Checker c) {
        capturedCheckers.add(c);
    }
    
    public boolean noCapture() {
        return capturedCheckers.isEmpty();
    }
}
