/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Marko
 */
public class AIPlayer extends Player {
    Checker moveChecker;
    Tile moveTile;
    String difficulty;  
    Player opponent;
    HashMap<Move, Integer> moveScores = new HashMap<>();
    
    AIPlayer(ArrayList<Checker> checkers, String colour, Player opponent, String difficulty) {
        super(checkers, colour);
        this.opponent = opponent;
        moveChecker = null;
        moveTile = null;
        this.difficulty = difficulty;
    }
    
    public Move getMove() {
        switch(difficulty) {
            case "Easy":
                return getBestMove(0);
            case "Medium":
                return getBestMove(2);
            case "Hard":
                return getBestMove(7);
            default:
                return randomMove();
        }
    }
    
    public Move getBestMove(int depth) {
        evaluateMoves(depth);
        
        Move bestMove = allMoves.get(0);
        for(Move m : moveScores.keySet()) {
            if(moveScores.get(m) > moveScores.get(bestMove))
                bestMove = m;
        }
        System.out.println("bestmove score: " + moveScores.get(bestMove));
        
        return bestMove;
    }
    
    private void evaluateMoves(int depth) {
        moveScores.clear();
        for(Move m : allMoves) {
            m.addData(Board.tiles, checkers, opponent.checkers);
            moveScores.put(m, m.evaluateMove(depth));
        }
    }
   
    public Move randomMove() {
       for(Move m : allMoves) {
           if(m.checker.canMove()) {
               return m;
           }
       }
       
       return null;
   }      
}
