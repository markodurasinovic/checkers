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
public class Move {
    Checker checker;
    Tile tile;
    ArrayList<Checker> capturedCheckers;    
    
    Move parent;
    ArrayList<Move> children;
    
    // state representation
    Checker simChecker;
    Tile simTile;
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    ArrayList<Checker> blueCheckers = new ArrayList<>();
    Tile[][] tiles = new Tile[8][8];
    // ====================
    HashMap<Integer, ArrayList<Move>> movesAtDepth = new HashMap<>();
    
    Move(Checker checker, Tile tile) {
        this.checker = checker;
        this.tile = tile;
        capturedCheckers = new ArrayList<>();        
        
        parent = null;
        children = new ArrayList<>();
    }
    
    public int evaluateMove(int depth) {
        return minimax(this, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    }
    
    private Checker getChecker(Checker checker) {
        for(Checker c : whiteCheckers) {
            if(c.currentTile.x == checker.currentTile.x
                    && c.currentTile.y == checker.currentTile.y) {
                return c;
            }
        }
        for(Checker c : blueCheckers) {
            if(c.currentTile.x == checker.currentTile.x
                    && c.currentTile.y == checker.currentTile.y) {
                return c;
            }
        }
        return null;
    }
    
    private Tile getTile(Tile tile) {
        return tiles[tile.x][tile.y];
    }
    
//     player = 1 for AI, 0 for Human
    private int minimax(Move node, int depth, int alpha, int beta, int player) {
        performMove(simChecker, simTile);
                
        if(depth == 0) {
            return node.evaluateState();
        }
                
        if(player == 1) {
            int bestVal = Integer.MIN_VALUE;
            getAllMoves(blueCheckers);
            for(int i = 0; i < Math.min(children.size(), depth + 5); i++) {
                Move child = children.get(i);
                int val = minimax(child, depth - 1, alpha, beta, 0);
                bestVal = Math.max(bestVal, val);
                alpha = Math.max(val, alpha);                
                if(alpha >= beta)
                    break;
            }
            return bestVal;
        } else {
            int bestVal = Integer.MAX_VALUE;
            getAllMoves(whiteCheckers);
            for(int i = 0; i < Math.min(children.size(), depth + 5); i++) {
                Move child = children.get(i);
                int val = minimax(child, depth - 1, alpha, beta, 1);
                bestVal = Math.min(bestVal, val);
                beta = Math.min(val, beta);
                if(alpha >= beta)
                    break;
            }
            return bestVal;
        }        
    }
        
    private void performMove(Checker checker, Tile tile) {
        checker.move(tile);
        
        if(checker.isTakingMove(tile)) {
            capture(checker, tile);
        }
        
        if(checker.inKingsRow()) {
            checker.crown();
        }
    }
        
    private void capture(Checker checker, Tile tile) {
        checker.performTakingMove(tile);        
        for(Checker c : checker.getTakenCheckers(tile)) {            
            if(whiteCheckers.contains(c)) {
                whiteCheckers.remove(c);
            } else {
                blueCheckers.remove(c);
            }                
        }        
    }
    
    private void getAllMoves(ArrayList<Checker> checkers) {
        ArrayList<Move> moves = new ArrayList<>();
        for(Checker c : checkers) {
            c.calculatePossibleMoves();
            for(Move m : c.possibleMoves) {
                moves.add(m);
            }
        }        
        forceCaptureMoves(moves, checkers);
        
        for(Move m : moves) {
            m.addData(tiles, whiteCheckers, blueCheckers);
            addChild(m);
        }
    }
    
    private void forceCaptureMoves(ArrayList<Move> moves, ArrayList<Checker> checkers) {
        if(!hasTakingMove(moves))
            return;
        
        for(Checker c : checkers) {
            c.possibleMoves.removeIf(Move::noCapture);
        }
        moves.removeIf(Move::noCapture);
    }
    
    private boolean hasTakingMove(ArrayList<Move> moves) {
        for(Move m : moves) {
            if(!m.noCapture())
                return true;
        }
        
        return false;
    }
    
    private int evaluateState() {
        return getCheckerValue(whiteCheckers) - getCheckerValue(blueCheckers);
    }
    
    private int getCheckerValue(ArrayList<Checker> checkers) {
        int value = 0;
        for(Checker c : checkers) {
            
            value += 1;
            if(c.isKing)
                value += 2;
        }
        return value;
    }
    
    public void printState() {
        System.out.println(Arrays.toString(tiles));
        System.out.println(whiteCheckers);
        System.out.println(blueCheckers);
    }
    
    public void addData(Tile[][] oTiles, ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlueCheckers) {
        deepCopyTiles(oTiles);
        deepCopyCheckers(oWhiteCheckers, oBlueCheckers);
        associateCheckersAndTiles();
        simChecker = getChecker(checker);
        simTile = getTile(tile);
    }
    
    private void deepCopyTiles(Tile[][] oTiles) {
        for(Tile[] tileRow : oTiles) {
            for(Tile t : tileRow) {
                tiles[t.x][t.y] = t.deepCopy(tiles);
            }
        }
    }
    
    private void deepCopyCheckers(ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlueCheckers) {
        for(Checker c : oWhiteCheckers) {
            whiteCheckers.add(c.deepCopy());
        }
        for(Checker c : oBlueCheckers) {
            blueCheckers.add(c.deepCopy());
        }
    }
    
    private void associateCheckersAndTiles() {
        for(Checker c : whiteCheckers) {
            int x = c.currentTile.x;
            int y = c.currentTile.y;
            c.currentTile = tiles[x][y];
            tiles[x][y].placeChecker(c);
        }
        for(Checker c : blueCheckers) {
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
