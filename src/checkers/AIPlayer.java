/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Marko
 */
public class AIPlayer extends Player {
    Checker moveChecker;
    Tile moveTile;
    
    AIPlayer(ArrayList<Checker> checkers, String colour) {
        super(checkers, colour);
        moveChecker = null;
        moveTile = null;
    }
    
   public void calculateMove() {
       for(Checker c : checkers) {
           if(c.canMove()) {
               moveChecker = c;
               break;
           }
       }       
       
       moveChecker.calculatePossibleMoves();
       Set<Tile> possibleMoves = moveChecker.getPossibleMoves();
       moveTile = (Tile) possibleMoves.toArray()[0];
   }
    
}
