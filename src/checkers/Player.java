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
    
    
    Player(ArrayList<Checker> checkers, String colour) {
        this.checkers = checkers;
        this.colour = colour;
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
}
