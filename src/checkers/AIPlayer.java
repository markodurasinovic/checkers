/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.Arrays;

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
    
    AIPlayer(ArrayList<Checker> checkers, String colour, Player opponent) {
        super(checkers, colour);
        this.opponent = opponent;
        moveChecker = null;
        moveTile = null;
    }
    
//    private void createSimulation() {
//        simulateTiles();
//        mySimulatedCheckers = simulateCheckers(checkers);
//        enemySimulatedCheckers = simulateCheckers(opponent.checkers);
//        matchCheckersToTiles(mySimulatedCheckers);
//        matchCheckersToTiles(enemySimulatedCheckers);        
//    }
//    
//    private void endSimulation() {
//        mySimulatedCheckers.clear();
//        enemySimulatedCheckers.clear();
//    }
//    
//    private void simulateTiles() {
//        for(Tile[] tileRow : Board.tiles) {
//            for(Tile t : tileRow) {
//                simulatedTiles[t.x][t.y] = t.deepCopy(simulatedTiles);
//            }
//        }
//    }
//    
//    private ArrayList<Checker> simulateCheckers(ArrayList<Checker> checks) {
//        ArrayList<Checker> simCheckers = new ArrayList<>();
//        for(Checker c : checks) {
//            simCheckers.add(c.deepCopy());
//        }
//        
//        return simCheckers;
//    }
//    
//    private void matchCheckersToTiles(ArrayList<Checker> checks) {
//        for(Checker c : checks) {
//            int x = c.currentTile.x;
//            int y = c.currentTile.y;
//            c.currentTile = simulatedTiles[x][y];
//            simulatedTiles[x][y].placeChecker(c);
//        }
//    }
    
   public void calculateMove() {
//       createSimulation();
       randomMove();
       
//       endSimulation();
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
       move.printState();
       System.out.println("============");
       System.out.println(Arrays.toString(Board.tiles));
       System.out.println(checkers);
       System.out.println(opponent.checkers);
       /////
   }
   
   public void negamax() {
       
   }
    
}
