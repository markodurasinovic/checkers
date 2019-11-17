/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Marko
 */
public class Board extends GridPane {
//    Tile[][] tiles = new Tile[8][8];
    ArrayList<Checker> blackCheckers = new ArrayList<>();
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    
    
    Board() {        
        draw();
        populate();
        paint();
        addHandlers();
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
    
    public void moveChecker(Checker checker, int x, int y) {
        getChildren().remove(checker);
        add(checker, x, y);
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
                checker.setPossibleMoves();
                checker.showPossibleMoves();
                ArrayList<Tile> tiles = checker.currentPossibleMoves;
                addTileHandlers(tiles.get(0), tiles.get(1), checker);
                addTileHandlers(tiles.get(1), tiles.get(0), checker);
//                checker.currentPossibleMoves.forEach((tile) -> {
//                    addTileHandlers(tile, checker);
//                });
            
                checker.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
            }
        };         
       
        checker.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }
    
    
    private void addTileHandlers(Tile tile, Tile otherTile, Checker checker) {      
        
        EventHandler<MouseEvent> moveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                checker.currentTile.removeChecker();                
                getChildren().remove(checker);
                tile.placeChecker(checker);
                add(checker, tile.x, tile.y);
                checker.hidePossibleMoves();
                
                Node node = (Node) tile;
                removeEventFilterFromNode(node, this);
                Node otherNode = (Node) otherTile;
                removeEventFilterFromNode(otherNode, this);
//                tile.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
//                otherTile.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
            }
        };
        
        tile.addEventFilter(MouseEvent.MOUSE_CLICKED, moveHandler);
    }
    
    private void removeEventFilterFromNode(Node node, EventHandler eh) {        
        node.removeEventFilter(MouseEvent.MOUSE_CLICKED, eh);
        System.out.println("removing filter from " + node);
    }
}
