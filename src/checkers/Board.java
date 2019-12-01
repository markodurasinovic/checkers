/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Marko
 */
public class Board extends GridPane {
    ArrayList<Checker> blackCheckers = new ArrayList<>();
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    HashMap<Tile, EventHandler> tileFilters = new HashMap<>();
    
    HumanPlayer humanPlayer;
    AIPlayer aiPlayer;
    Player nextTurn;
    
    
    Board() {        
        draw();
        populate();
        paint();
        addHandlers();
        
        humanPlayer = new HumanPlayer(blackCheckers, "black");
        aiPlayer = new AIPlayer(whiteCheckers, "white");
        
        nextTurn = humanPlayer;
    }
    
    private void draw() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        addTile("white", i, j);
                    } else {
                        addTile("black", i, j);
                    }
                } else {
                    if(j % 2 == 0) {
                        addTile("black", i, j);
                    } else {
                        addTile("white", i, j);
                    }
                }
            }
        }
    }
    
    private void populate() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 1)
                        addChecker("white", j, i);
                } else {
                    if(j % 2 == 0)
                        addChecker("white", j, i);
                }                
            }
        }
        
        for(int i = 7; i > 4; i--) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 1)
                        addChecker("black", j, i);
                } else {
                    if(j % 2 == 0)
                        addChecker("black", j, i);
                }
            }
        }
    }
    
    private void addTile(String colour, int x, int y) {
        Color tileColour = Color.BLACK;
        if(colour.equals("white"))
            tileColour = Color.RED;
        
        Tile tile = new Tile(75, 75, tileColour, x, y);
        add(tile, x, y);
        Tile.TILES[x][y] = tile;
    }
    
    private void addChecker(String colour, int x, int y) {
        Color checkerColour = Color.BLUE;
        if(colour.equals("white"))
            checkerColour = Color.WHITE;
        
        Tile tile = Tile.TILES[x][y];
        Checker checker = new Checker(75/2, checkerColour, tile);        
        tile.placeChecker(checker);
        
        //fix this up
        if(colour.equals("black")) {
            blackCheckers.add(checker);
        } else {
            whiteCheckers.add(checker);
        }
    }
    
    public void paint() {        
        for(Tile[] tileRow : Tile.TILES) {
            for(Tile tile : tileRow) {
                if(tile.hasChecker()) {
                    add(tile.checker, tile.x, tile.y);
                }
            }
        }
    }
    
    private void addHandlers() {
        for(Checker checker : whiteCheckers)
            addCheckerHandlers(checker);
        
        for(Checker checker : blackCheckers)
            addCheckerHandlers(checker);
    }
    
    private void addCheckerHandlers(Checker checker) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(nextTurn);
                if(!nextTurn.ownsChecker(checker))
                    return;
                
                clearTileHandlers();
                resetTileColours();
                
                checker.calculatePossibleMoves();
                checker.showPossibleMoves();
                checker.getPossibleMoves().forEach((tile) -> {
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
                moveChecker(checker, tile);
            }
        };
        
        tile.addEventFilter(MouseEvent.MOUSE_CLICKED, moveHandler);
        tileFilters.put(tile, moveHandler);
    }
    
    private void moveChecker(Checker checker, Tile tile) {
        move(checker, tile);
        
        if(checker.isTakingMove(tile)) {
            capture(checker, tile);
        }
        
        if(checker.inKingsRow()) {
            checker.crown();
        }        
        
        resetTileColours();                
        clearTileHandlers();
        switchTurn();
        
        checkGameFinished();
    }
    
    private void move(Checker checker, Tile tile) {
        checker.move(tile);
        
        getChildren().remove(checker);
        add(checker, tile.x, tile.y);
    }
    
    private void capture(Checker checker, Tile tile) {
        checker.performTakingMove(tile);
        for(Checker c : checker.getTakenCheckers(tile)) {
            getChildren().remove(c);
        }
    }
            
    private void clearTileHandlers() {
        if(tileFilters.isEmpty())
            return;
        
        tileFilters.keySet().forEach((tile) -> {
            tile.removeEventFilter(MouseEvent.MOUSE_CLICKED, tileFilters.get(tile));
        });
    }
    
    private void resetTileColours() {
        for(Tile[] tileRow : Tile.TILES) {
            for(Tile tile : tileRow) {
                tile.resetColour();
            }
        }
    }
    
    private void switchTurn() {
        if(nextTurn == humanPlayer) {
            nextTurn = aiPlayer;
        } else {
            nextTurn = humanPlayer;
        }
    }
    
    private void checkGameFinished() {
        if(humanPlayer.hasLost())
            System.out.println(humanPlayer.colour + " has lost!");
        
        if(aiPlayer.hasLost())
            System.out.println(aiPlayer.colour + " has lost!");
    }
}
