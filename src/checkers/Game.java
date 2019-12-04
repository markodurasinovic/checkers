/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Marko
 */
public class Game {
    Board board;
    Player nextTurn;
    HumanPlayer player1;
    AIPlayer player2;
    HashMap<Tile, EventHandler> tileFilters = new HashMap<>();
    
    Game() {
        board = new Board();
        player1 = new HumanPlayer(board.blueCheckers, "blue");
        player2 = new AIPlayer(board.whiteCheckers, "white", player1);
        nextTurn = player1;
        
        calculateMoves();        
        addHandlers();
    }
    
    private void calculateMoves() {
        player1.calculateAllMoves();
        player2.calculateAllMoves();
    }
    
        
    private void addHandlers() {
        for(Checker checker : player1.checkers)
            addCheckerHandlers(checker);        
    }
    
    private void addCheckerHandlers(Checker checker) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {                
                clearTileHandlers();
                board.resetTileColours();
                
                checker.showPossibleMoves();
                checker.getPossibleMoveTiles().forEach((tile) -> {
                    addTileHandlers(tile, checker);
                });
            }
        };         
       
        checker.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }
    
    private void addTileHandlers(Tile tile, Checker checker) {      
        
        EventHandler<MouseEvent> moveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                performMove(checker, tile);
            }
        };
        
        tile.addEventFilter(MouseEvent.MOUSE_CLICKED, moveHandler);
        tileFilters.put(tile, moveHandler);
    }
    
    private void performMove(Checker checker, Tile tile) {
        moveChecker(checker, tile);
        
        board.resetTileColours();                
        clearTileHandlers();
        switchTurn();        
        checkGameFinished();
        
        calculateMoves();
        
        if(nextTurn == player2) {
            performAiMove();
        }
    }
    
    private void performAiMove() {
        Move mv = player2.getMove();
        performMove(mv.checker, mv.tile);
    }
    
    private void moveChecker(Checker checker, Tile tile) {
        move(checker, tile);
        
        if(checker.isTakingMove(tile)) {
            capture(checker, tile);
        }
        
        if(checker.inKingsRow()) {
            checker.crown();
        }
    }
    
    private void move(Checker checker, Tile tile) {
        checker.move(tile);
        
        board.getChildren().remove(checker);
        board.add(checker, tile.x, tile.y);
    }
    
    private void capture(Checker checker, Tile tile) {
        checker.performTakingMove(tile);        
        for(Checker c : checker.getTakenCheckers(tile)) {
            board.getChildren().remove(c);
            
            if(player1.ownsChecker(c)) {
                player1.removeChecker(c);
            } else {
                player2.removeChecker(c);
            }                
        }        
    }

    public void clearTileHandlers() {
        if(tileFilters.isEmpty())
            return;
        
        tileFilters.keySet().forEach((tile) -> {
            tile.removeEventFilter(MouseEvent.MOUSE_CLICKED, tileFilters.get(tile));
        });
    }
    
    private void switchTurn() {
        if(nextTurn == player1) {
            nextTurn = player2;
        } else {
            nextTurn = player1;
        }
    }
    
    private void checkGameFinished() {
        if(player1.hasLost())
            System.out.println(player1.colour + " has lost!");
        
        if(player2.hasLost())
            System.out.println(player2.colour + " has lost!");
    }
}
