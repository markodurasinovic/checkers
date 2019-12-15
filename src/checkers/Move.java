package checkers;

import java.util.ArrayList;

/**
 * The Move class holds each state of the game. It contains the data required to
 * determine the state of the board after each move which is performed. This
 * class also implements a negamax variation of the minimax algorithm to
 * determine the value of a move.
 */
public class Move {

    Checker checker;
    Tile tile;
    ArrayList<Checker> capturedCheckers;

    Move parent;
    ArrayList<Move> children;
    Checker simChecker;
    Tile simTile;
    ArrayList<Checker> whiteCheckers = new ArrayList<>();
    ArrayList<Checker> blueCheckers = new ArrayList<>();
    Tile[][] tiles = new Tile[8][8];

    /**
     * Create a new Move object, containing the checker which is moving and the
     * tile which it is moving to.
     *
     * @param checker : Checker to move.
     * @param tile : Tile to move the checker to.
     */
    Move(Checker checker, Tile tile) {
        this.checker = checker;
        this.tile = tile;
        capturedCheckers = new ArrayList<>();

        parent = null;
        children = new ArrayList<>();
    }

    /**
     * Uses a negamax algorithm to determine the value for this particular move,
     * looking forward a "depth" number of moves.
     *
     * @param depth : Depth of recursion of the negamax algorithm.
     * @return A value of this move, based on some heuristic evaluations.
     */
    public int evaluateMove(int depth) {
        int val = negamax(this, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);

        return val;
    }

    /**
     * An implementation of the negamax variation of the minimax algorithm. Due
     * to the complexity of the game state representation, each unvisited child
     * of a move (i.e. after loop breaks because alpha>=beta) is thrown away to
     * reduce the memory usage of this method.
     *
     * @param node
     * @param depth
     * @param alpha
     * @param beta
     * @param player
     * @return Best value for a particular move
     */
    private int negamax(Move node, int depth, int alpha, int beta, int player) {
        node.simulateMove();
        if (node.winningMove(player)) {
            return Integer.MAX_VALUE;
        }

        if (node.losingMove(player)) {
            return Integer.MIN_VALUE;
        }

        if (depth == 0) {
            return node.evaluateState() * player;
        }

        if (player == 1) {
            node.getAllMoves(blueCheckers);
        } else {
            node.getAllMoves(whiteCheckers);
        }

        int bestVal = Integer.MIN_VALUE;
        for (int i = 0; i < node.children.size(); i++) {
            Move child = node.children.get(i);
            int val = -node.negamax(child, depth - 1, -beta, -alpha, -player);
            bestVal = Math.max(bestVal, val);
            alpha = Math.max(alpha, val);
            if (alpha >= beta) {
                break;
            }
        }
        node.children.clear();

        return bestVal;
    }
    
     /**
     * Simulates this move so that subsequent child moves can be calculated.
     */
    private void simulateMove() {
        simChecker.move(simTile);

        if (simChecker.isCapturingMove(simTile)) {
            capture(simChecker, simTile);
        }

        if (simChecker.inKingsRow()) {
            simChecker.crown();
        }
    }

    /**
     * Checks if this is the winning move for a player.
     *
     * @param player : 1 = AI player
     * @return T/F
     */
    private boolean winningMove(int player) {
        if (player == 1) {
            return blueCheckers.isEmpty() || !canMove(blueCheckers);
        } else {
            return whiteCheckers.isEmpty() || !canMove(whiteCheckers);
        }
    }

    /**
     * Checks if this is the losing move for a player.
     *
     * @param player : 1 = AI player
     * @return T/F
     */
    private boolean losingMove(int player) {
        if (player == 1) {
            return whiteCheckers.isEmpty() || !canMove(whiteCheckers);
        } else {
            return blueCheckers.isEmpty() || !canMove(blueCheckers);
        }
    }

    /**
     * Check if any of these checkers can move.
     *
     * @param checkers
     * @return T/F
     */
    private boolean canMove(ArrayList<Checker> checkers) {
        return checkers.stream().anyMatch((Checker c) -> (c.canMove()));
    }

    /**
     * Simulates a capturing move.
     *
     * @param checker
     * @param tile
     */
    private void capture(Checker checker, Tile tile) {
        checker.performCapturingMove(tile);
        checker.getTakenCheckers(tile).forEach((Checker c) -> {
            if (whiteCheckers.contains(c)) {
                whiteCheckers.remove(c);
            } else {
                blueCheckers.remove(c);
            }
        });
    }

    /**
     * A successor function, calculating all states (moves) which may follow on
     * from this one.
     *
     * @param checkers
     */
    private void getAllMoves(ArrayList<Checker> checkers) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Checker c : checkers) {
            c.calculatePossibleMoves();
            c.possibleMoves.forEach((Move m) -> {
                moves.add(m);
            });
        }
        forceCaptureMoves(moves, checkers);

        for (Move m : moves) {
            m.addData(tiles, whiteCheckers, blueCheckers);
            addChild(m);
        }
    }

    /**
     * If any capturing moves exist, then a capturing move must be performed.
     *
     * @param moves
     * @param checkers
     */
    private void forceCaptureMoves(ArrayList<Move> moves, ArrayList<Checker> checkers) {
        if (!hasCapturingMove(moves)) {
            return;
        }

        checkers.forEach((Checker c) -> {
            c.possibleMoves.removeIf(Move::noCapture);
        });
        moves.removeIf(Move::noCapture);
    }

    /**
     * Checks if any capturing moves are present in moves.
     *
     * @param moves
     * @return T/F
     */
    private boolean hasCapturingMove(ArrayList<Move> moves) {
        for (Move m : moves) {
            if (!m.noCapture()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Evaluates the state of the game based on this move, using some
     * heuristics.
     *
     * @return
     */
    private int evaluateState() {
        return getCheckerValue(whiteCheckers) - getCheckerValue(blueCheckers);
    }

    /**
     * The value of a checker is calculated, giving a higher value to king
     * checkers. Each checker has additional value based on its position on the
     * board.
     *
     * @param checkers
     * @return
     */
    private int getCheckerValue(ArrayList<Checker> checkers) {
        int value = 0;
        for (Checker c : checkers) {
            value += 8;
            if (c.isKing) {
                value += 12;
                value += positionValueKing(c);
            } else {
                value += positionValue(c);
            }

        }
        return value;
    }

    /**
     * A heuristic assigning higher value to a checker depending on its position
     * on the board.
     *
     * @param checker
     * @return
     */
    private int positionValue(Checker checker) {
        int[][] positionValues = new int[][]{
            new int[]{0, 7, 0, 7, 0, 7, 0, 7},
            new int[]{3, 0, 3, 0, 3, 0, 3, 0},
            new int[]{0, 1, 0, 1, 0, 1, 0, 1},
            new int[]{1, 0, 5, 0, 5, 0, 1, 0},
            new int[]{0, 1, 0, 5, 0, 5, 0, 1},
            new int[]{1, 0, 1, 0, 1, 0, 1, 0},
            new int[]{0, 3, 0, 3, 0, 3, 0, 3},
            new int[]{7, 0, 7, 0, 7, 0, 7, 0}
        };

        int x = checker.currentTile.x;
        int y = checker.currentTile.y;

        return positionValues[x][y];
    }

    /**
     * A heuristic assigning higher value to a king checker depending on its
     * position on the board.
     *
     * @param checker
     * @return
     */
    private int positionValueKing(Checker checker) {
        int[][] positionValues = new int[][]{
            new int[]{0, 1, 0, 1, 0, 1, 0, 1},
            new int[]{3, 0, 3, 0, 3, 0, 3, 0},
            new int[]{0, 5, 0, 5, 0, 5, 0, 1},
            new int[]{1, 0, 7, 0, 7, 0, 1, 0},
            new int[]{0, 1, 0, 7, 0, 7, 0, 1},
            new int[]{1, 0, 5, 0, 5, 0, 5, 0},
            new int[]{0, 3, 0, 3, 0, 3, 0, 3},
            new int[]{1, 0, 1, 0, 1, 0, 1, 0}
        };

        int x = checker.currentTile.x;
        int y = checker.currentTile.y;

        return positionValues[x][y];
    }

    /**
     * Adds data to the Move object. This data is required to represent a
     * particular state. The data provided through this method is used for
     * simulating successive moves.
     *
     * @param oTiles : Original tiles.
     * @param oWhiteCheckers : Original white checkers.
     * @param oBlueCheckers : Original blue checkers.
     */
    public void addData(Tile[][] oTiles, ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlueCheckers) {
        deepCopyTiles(oTiles);
        deepCopyCheckers(oWhiteCheckers, oBlueCheckers);
        associateCheckersAndTiles();
        simChecker = getSimulatedChecker(checker);
        simTile = getSimulatedTile(tile);
    }

    /**
     * Creates an identical copy of the tiles array on which simulated moves can
     * be performed to enable the successor function to do its work. The tiles
     * array is a representation of the board.
     *
     * @param oTiles
     */
    private void deepCopyTiles(Tile[][] oTiles) {
        for (Tile[] tileRow : oTiles) {
            for (Tile t : tileRow) {
                tiles[t.x][t.y] = t.deepCopy(tiles);
            }
        }
    }

    /**
     * Creates an identical copy of both players' checkers on which simulated
     * moves can be performed to enable the successor function to do its work.
     *
     * @param oWhiteCheckers
     * @param oBlueCheckers
     */
    private void deepCopyCheckers(ArrayList<Checker> oWhiteCheckers, ArrayList<Checker> oBlueCheckers) {
        oWhiteCheckers.forEach((Checker c) -> {
            whiteCheckers.add(c.deepCopy());
        });
        
        oBlueCheckers.forEach((Checker c) -> {
            blueCheckers.add(c.deepCopy());
        });
    }

    /**
     * Connects simulated checkers to simulated tiles.
     */
    private void associateCheckersAndTiles() {
        for (Checker c : whiteCheckers) {
            int x = c.currentTile.x;
            int y = c.currentTile.y;
            c.currentTile = tiles[x][y];
            tiles[x][y].placeChecker(c);
        }
        
        for (Checker c : blueCheckers) {
            int x = c.currentTile.x;
            int y = c.currentTile.y;
            c.currentTile = tiles[x][y];
            tiles[x][y].placeChecker(c);
        }
    }

    /**
     * Gets a simulated checker based on this move's actual checker.
     *
     * @param checker : The checker of this move.
     * @return A simulated copy of checker.
     */
    private Checker getSimulatedChecker(Checker checker) {
        for (Checker c : whiteCheckers) {
            if (c.currentTile.x == checker.currentTile.x
                    && c.currentTile.y == checker.currentTile.y) {
                return c;
            }
        }
        for (Checker c : blueCheckers) {
            if (c.currentTile.x == checker.currentTile.x
                    && c.currentTile.y == checker.currentTile.y) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets a simulated tile based on this move's actual tile.
     *
     * @param tile : The tile of this move.
     * @return A simulated copy of tile.
     */
    private Tile getSimulatedTile(Tile tile) {
        return tiles[tile.x][tile.y];
    }

    /**
     * Add a child Move to this move.
     *
     * @param child
     */
    public void addChild(Move child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Set the parent Move of this move.
     *
     * @param parent
     */
    public void setParent(Move parent) {
        this.parent = parent;
    }

    /**
     * Add a captured checker to this move.
     *
     * @param c
     */
    public void addCapture(Checker c) {
        capturedCheckers.add(c);
    }

    /**
     * Checks if this move has any captures.
     *
     * @return
     */
    public boolean noCapture() {
        return capturedCheckers.isEmpty();
    }
}
