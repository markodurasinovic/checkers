/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Marko
 */
public class AIPlayer extends Player {
    Checker moveChecker;
    Tile moveTile;
    
//    Tile[][] simulatedTiles = new Tile[8][8];
//    Tile[][] tempTiles;
//    ArrayList<Checker> mySimulatedCheckers = new ArrayList<>();
//    ArrayList<Checker> enemySimulatedCheckers = new ArrayList<>();    
    Player opponent;
    HashMap<Move, Integer> moveScores = new HashMap<>();
    
    AIPlayer(ArrayList<Checker> checkers, String colour, Player opponent) {
        super(checkers, colour);
        this.opponent = opponent;
        moveChecker = null;
        moveTile = null;
    }
    
    public Move getMove() {
        evaluateMoves();
        return getBestMove();
    }
    
    private void evaluateMoves() {
        moveScores.clear();
        for(Move m : allMoves) {
            m.addData(Board.tiles, checkers, opponent.checkers);
            moveScores.put(m, m.evaluateMove(3));
        }
    }
    
    public Move getBestMove() {
        Move bestMove = allMoves.get(0);
        for(Move m : moveScores.keySet()) {
            if(moveScores.get(m) > moveScores.get(bestMove))
                bestMove = m;
        }
        
        return bestMove;
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
       
       Move move = moveChecker.possibleMoves.get(0);
       /////
       move.addData(Board.tiles, checkers, opponent.checkers);
       System.out.println(move.evaluateMove(3));
//       move.printState();
//       System.out.println("============");
//       System.out.println(Arrays.toString(Board.tiles));
//       System.out.println(checkers);
//       System.out.println(opponent.checkers);
       /////
   }
   
    public void negamax() {
       
    }
    
}
