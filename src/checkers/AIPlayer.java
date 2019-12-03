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
public class AIPlayer extends Player {
    Checker moveChecker;
    Tile moveTile;
    
    AIPlayer(ArrayList<Checker> checkers, String colour) {
        super(checkers, colour);
        moveChecker = null;
        moveTile = null;
    }
    
   public void calculateMove() {
       randomMove();
   }
   
   public void randomMove() {
       for(Move m : allMoves) {
           if(m.checker.canMove()) {
               moveChecker = m.checker;
               break;
           }
       }       
       
       ArrayList<Tile> possibleMoves = moveChecker.getPossibleMoveTiles();
       moveTile = (Tile) possibleMoves.toArray()[0];
   }
   
   public void negamax() {
       
   }
    
}
