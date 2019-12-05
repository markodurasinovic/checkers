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
    
    int maxEver;
    int movesConsidered;
    
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
    
    boolean visited = false;
    
    Move(Checker checker, Tile tile) {
        this.checker = checker;
        this.tile = tile;
        capturedCheckers = new ArrayList<>();        
        
        parent = null;
        children = new ArrayList<>();
    }
    
    public int evaluateMove(int depth) {
        movesConsidered = 0;
        int val = negamax(this, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        System.out.println("moves considered: " + movesConsidered);
        
        return val;
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
    
    private int negamax(Move node, int depth, int alpha, int beta, int player) {
        movesConsidered++;
        node.simulateMove();
        if(node.winningMove()) 
            return Integer.MAX_VALUE;
        
        if(node.losingMove())
            return Integer.MIN_VALUE;
        
        if(depth == 0)
            return node.evaluateState() * player;
        
        if(player == 1) {
            node.getAllMoves(blueCheckers);
        } else {
            node.getAllMoves(whiteCheckers);
        }
        
        int bestVal = Integer.MIN_VALUE;
        for(int i = 0; i < node.children.size(); i++) {
            Move child = node.children.get(i);
            int val = -node.negamax(child, depth - 1, -beta, -alpha, -player);
            bestVal = Math.max(bestVal, val);
            alpha = Math.max(alpha, val);
            if(alpha >= beta)
                break;
        }
        node.children.removeIf(Move::notVisited);
        
        return bestVal;
    }
    
    private boolean winningMove() {
        return blueCheckers.isEmpty();
    }
    
    private boolean losingMove() {
        return whiteCheckers.isEmpty();
    }
    
    private void simulateMove() {
        simChecker.move(simTile);
        
        if(simChecker.isTakingMove(simTile)) {
            capture(simChecker, simTile);
        }
        
        if(simChecker.inKingsRow()) {
            simChecker.crown();
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
        visited = true;
        return getCheckerValue(whiteCheckers) - getCheckerValue(blueCheckers);
    }
    
    public boolean notVisited() {
        return !visited;
    }
    
    private int getCheckerValue(ArrayList<Checker> checkers) {
        int value = 0;
        for(Checker c : checkers) {
            
            value += 1;
            if(c.isKing)
                value += 100;
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
