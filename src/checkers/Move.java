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
    ArrayList<Checker> capturedCheckers;    
    
    Move parent;
    ArrayList<Move> children;
    
    // state representation
    ArrayList<Checker> whiteCheckers;
    ArrayList<Checker> blackCheckers;
    Tile[][] tiles = new Tile[8][8];
    // ====================
    
    Move(Checker checker, Tile tile) {
        this.checker = checker;
        this.tile = tile;
        capturedCheckers = new ArrayList<>();        
        
        parent = null;
        children = new ArrayList<>();
    }
    
    public void addData(Tile[][] oTiles, ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlackCheckers) {
        deepCopyTiles(oTiles);
        deepCopyCheckers(oWhiteCheckers, oBlackCheckers);
        associateCheckersAndTiles();
    }
    
    private void deepCopyTiles(Tile[][] oTiles) {
        for(Tile[] tileRow : oTiles) {
            for(Tile t : tileRow) {
                tiles[t.x][t.y] = t.deepCopy(tiles);
            }
        }
    }
    
    private void deepCopyCheckers(ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlackCheckers) {
        for(Checker c : oWhiteCheckers) {
            whiteCheckers.add(c.deepCopy());
        }
        for(Checker c : oBlackCheckers) {
            blackCheckers.add(c.deepCopy());
        }
    }
    
    private void associateCheckersAndTiles() {
        for(Checker c : whiteCheckers) {
            int x = c.currentTile.x;
            int y = c.currentTile.y;
            c.currentTile = tiles[x][y];
            tiles[x][y].placeChecker(c);
        }
        for(Checker c : blackCheckers) {
            int x = c.currentTile.x;
            int y = c.currentTile.y;
            c.currentTile = tiles[x][y];
            tiles[x][y].placeChecker(c);
        }
    }
    
    public void addChild(Move child) {
        children.add(child);
        child.setParent(this);
    }
    
    public void setParent(Move parent) {
        this.parent = parent;
    }
    
    public void addCapture(Checker c) {
        capturedCheckers.add(c);
    }
    
    public boolean noCapture() {
        return capturedCheckers.isEmpty();
    }
}
