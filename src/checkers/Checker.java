package checkers;

import java.util.ArrayList;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;

/**
 * Checker objects hold information which assists the calculation of each move
 * available to the checker for a particular game state. It provides methods to
 * calculate the moves available to the checker. In combination with Tile
 * objects, Checker objects represent the state of the game.
 */
public class Checker extends Circle {

    Tile currentTile;
    boolean movingUp;
    Color colour, opposingColour;
    double radius;
    boolean isKing = false;
    ArrayList<Move> possibleMoves = new ArrayList<>();

    /**
     * Creates a checker of specified size and colour. Associates it to the tile
     * which it is initially placed on.
     *
     * @param radius : Radius of the checker to be seen in the GUI.
     * @param fill : Colour of the checker in the GUI.
     * @param tile : Tile which the checker is placed on.
     */
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        this.radius = radius;
        currentTile = tile;
        movingUp = fill == Color.BLUE;
        opposingColour = fill == Color.BLUE ? Color.WHITE : Color.BLUE;
        colour = (Color) fill;
    }

    /**
     * Creates an identical copy of this checker.
     *
     * @return A checker.
     */
    public Checker deepCopy() {
        Checker copy = new Checker(radius, colour, currentTile);
        copy.isKing = isKing;

        return copy;
    }

    /**
     * Highlight this checker's possible moves in the GUI.
     */
    public void showPossibleMoves() {
        ArrayList<Tile> possibleMoveTiles = getPossibleMoveTiles();
        possibleMoveTiles.forEach((Tile t) -> {
            t.setColour(Color.GREEN);
        });
    }

    /**
     * Retrieve all tiles which this checker can move to.
     *
     * @return ArrayList of tiles.
     */
    public ArrayList<Tile> getPossibleMoveTiles() {
        ArrayList<Tile> possibleMoveTiles = new ArrayList<>();
        possibleMoves.forEach((Move m) -> {
            possibleMoveTiles.add(m.tile);
        });

        return possibleMoveTiles;
    }

    /**
     * Check if checker has a move.
     *
     * @return T/F
     */
    public boolean canMove() {
        calculatePossibleMoves();

        return !possibleMoves.isEmpty();
    }

    /**
     * Calculates all possible moves for this checker. All moves are added to
     * possibleMoves.
     */
    public void calculatePossibleMoves() {
        possibleMoves.clear();
        calculateMovesForTile(currentTile);
    }

    /**
     * Calculate all possible moves for the checker from this tile.
     *
     * @param tile : Tile to move from.
     */
    private void calculateMovesForTile(Tile tile) {
        if (tile == null) {
            return;
        }

        ArrayList<Checker> enemyNeighbours = getEnemyNeighbours(tile);
        if (enemyNeighbours.isEmpty()
                || !hasCapturingMove(tile) && !isCapturingMove(tile)) {
            addForwardMove(tile);
        } else {
            enemyNeighbours.forEach((Checker c) -> {
                if (canCapture(tile, c)) {
                    addMove(tile, c);
                    Tile nextMoveTile = tile.getAdjacentTile(c.currentTile);
                    calculateMovesForTile(nextMoveTile);
                }
            });
        }
    }

    /**
     * Check if this checker can capture any opposing checkers surrounding this
     * tile.
     *
     * @param fromTile : Tile to check for opposing neighbours which can be
     * captured.
     * @return T/F
     */
    private boolean hasCapturingMove(Tile tile) {
        ArrayList<Checker> enemyNeighbours = getEnemyNeighbours(tile);
        
        return enemyNeighbours.stream().anyMatch((Checker c) -> (canCapture(tile, c)));
    }

    private ArrayList<Checker> getEnemyNeighbours(Tile tile) {
        ArrayList<Checker> neighbours = new ArrayList<>();
        ArrayList<Tile> neighbourTiles = tile.getSurroundingTiles();

        neighbourTiles.forEach((Tile t) -> {
            if (t.hasChecker() && t.checker.colour == opposingColour) {
                neighbours.add(t.checker);
            }
        });

        return neighbours;
    }

    /**
     * Check if a move ending at this tile is a capturing move.
     *
     * @param tile : Tile to move to.
     * @return T/F
     */
    public boolean isCapturingMove(Tile tile) {
        if (getTakenCheckers(tile) == null) {
            return false;
        }

        return !getTakenCheckers(tile).isEmpty();
    }

    /**
     * Adds available forward-moves for the checker.
     *
     * @param tile : Tile to move from.
     */
    private void addForwardMove(Tile tile) {
        ArrayList<Tile> surroundingTiles = tile.getSurroundingTiles();
        if (isKing) {
            surroundingTiles.forEach((Tile t) -> {
                if (!t.hasChecker()) {
                    addMove(t, null);
                }
            });
        } else {
            if (movingUp) {
                surroundingTiles.forEach((Tile t) -> {
                    if (tile.y > t.y && !t.hasChecker()) {
                        addMove(t, null);
                    }
                });
            } else {
                surroundingTiles.forEach((Tile t) -> {
                    if (tile.y < t.y && !t.hasChecker()) {
                        addMove(t, null);
                    }
                });
            }
        }
    }

    /**
     * Check if this checker can capture a specific checker from a specific
     * tile.
     *
     * @param fromTile : Tile to perform the capturing move from.
     * @param checker : Checker to capture.
     * @return T/F
     */
    private boolean canCapture(Tile fromTile, Checker checker) {
        Tile moveTile = fromTile.getAdjacentTile(checker.currentTile);
        if (moveTile != null && !moveTile.hasChecker()) {
            if (isKing && !alreadyVisited(moveTile)) {
                return true;
            }

            if (movingUp) {
                if (fromTile.y > checker.currentTile.y) {
                    return true;
                }
            } else {
                if (fromTile.y < checker.currentTile.y) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if this tile has already been visited by a previously calculated
     * move. (This fixes a bug where if a king does a multi-move capture, it is
     * able to repeatedly move between the tiles passed on its capture without
     * relinquishing control).
     *
     * @param tile : Tile to check for previous visitation.
     * @return T/F
     */
    private boolean alreadyVisited(Tile tile) {
        return possibleMoves.stream().anyMatch((Move m) -> (m.tile == tile));
    }

    /**
     * Create a Move and add it to possibleMoves.
     *
     * @param tile : Tile to move to.
     * @param capturedChecker : Checker captured via this move.
     */
    private void addMove(Tile tile, Checker capturedChecker) {
        if (capturedChecker == null) {
            possibleMoves.add(new Move(this, tile));
        } else {
            addCaptureMove(tile, capturedChecker);
        }
    }

    /**
     * Adds a captured checker to the Move which captures it. Finds "moveTile" -
     * the tile where capturing checker ends up after the capturing move.
     * Creates a new Move containing the moveTile and checker doing the
     * capturing.
     *
     * @param previousTile : Tile from which the checker was captured.
     * @param capturedChecker : Checker which was captured.
     */
    private void addCaptureMove(Tile previousTile, Checker capturedChecker) {
        Tile moveTile = previousTile.getAdjacentTile(capturedChecker.currentTile);
        if (getMove(moveTile) == null) {
            possibleMoves.add(new Move(this, moveTile));
        }

        ArrayList<Checker> capturedCheckers = getTakenCheckers(moveTile);
        ArrayList<Checker> previouslyCapturedCheckers = getTakenCheckers(previousTile);
        if (previouslyCapturedCheckers != null) {
            previouslyCapturedCheckers.forEach((Checker c) -> {
                capturedCheckers.add(c);
            });
        }

        getMove(moveTile).addCapture(capturedChecker);
    }

    /**
     * Gets checkers captured via the move of this checker ending at a tile.
     *
     * @param tile : Tile which the move ends on.
     * @return An ArrayList of captured checkers (or null if no checkers
     * captured via move)
     */
    public ArrayList<Checker> getTakenCheckers(Tile tile) {
        if (getMove(tile) != null) {
            return getMove(tile).capturedCheckers;
        }

        return null;
    }

    /**
     * Gets Move which ends at tile.
     *
     * @param tile : Tile at which the move ends.
     * @return Move corresponding to tile.
     */
    private Move getMove(Tile tile) {
        for (Move m : possibleMoves) {
            if (m.tile == tile) {
                return m;
            }
        }

        return null;
    }

    /**
     * Checker performs a capturing move. If a king is captured, the capturing
     * checker becomes king.
     *
     * @param tile : Tile to move to.
     */
    public void performCapturingMove(Tile tile) {
        for (Checker checker : getTakenCheckers(tile)) {
            if (checker.isKing) {
                crown();
            }

            checker.currentTile.removeChecker();
            checker.currentTile = null;
        }
    }

    /**
     * Moves checker to a tile.
     *
     * @param tile : Tile to move to.
     */
    public void move(Tile tile) {
        if (!tile.hasChecker()) {
            currentTile.removeChecker();
            currentTile = tile;
            currentTile.placeChecker(this);
        }

        if (inKingsRow()) {
            crown();
        }
    }

    /**
     * Turn this checker into a king. The king can move backwards and forwards.
     * White checkers are turned grey. Blue checkers are turned dark blue.
     */
    public void crown() {
        isKing = true;
        if (colour == Color.BLUE) {
            setFill(Color.DARKBLUE);
        } else {
            setFill(Color.GRAY);
        }
    }

    /**
     * Checks if checker is in kings row (for crowning purposes).
     *
     * @return T/F
     */
    public boolean inKingsRow() {
        if (isBlue()) {
            return currentTile.y == 0;
        } else {
            return currentTile.y == 7;
        }
    }

    /**
     * Checks if checker is blue.
     *
     * @return T/F
     */
    public boolean isBlue() {
        return colour == Color.BLUE;
    }
}
