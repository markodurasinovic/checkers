package checkers;

import java.util.ArrayList;

/**
 * The Player class provides various methods to facilitate the game's
 * functionality. It acts as a game-playing agent.
 */
public class Player {

    ArrayList<Checker> checkers;
    String colour;
    ArrayList<Move> allMoves;

    /**
     * Creates a new Player object, containing its owned checkers and the
     * player's checker colour.
     *
     * @param checkers
     * @param colour
     */
    Player(ArrayList<Checker> checkers, String colour) {
        this.checkers = checkers;
        this.colour = colour;

        allMoves = new ArrayList<>();
    }

    /**
     * Check if the player has lost.
     *
     * @return T/F
     */
    public boolean hasLost() {
        return checkers.isEmpty() || !canMove();
    }

    /**
     * Check if any of these checkers can move.
     *
     * @return T/F
     */
    private boolean canMove() {
        return checkers.stream().anyMatch((Checker c) -> (c.canMove()));
    }

    /**
     * Check if player owns this checker.
     *
     * @param checker
     * @return T/F
     */
    public boolean ownsChecker(Checker checker) {
        return checkers.contains(checker);
    }

    /**
     * Remove the checker from this player's checkers.
     *
     * @param checker
     */
    public void removeChecker(Checker checker) {
        checkers.remove(checker);
    }

    /**
     * Calculate all moves this player can make in the current game state.
     */
    public void calculateAllMoves() {
        allMoves.clear();
        for (Checker c : checkers) {
            c.calculatePossibleMoves();
            c.possibleMoves.forEach((Move m) -> {
                allMoves.add(m);
            });
        }
        forceCaptureMoves();
    }

    /**
     * If any capturing moves exist, then a capturing move must be performed.
     */
    private void forceCaptureMoves() {
        if (!hasCapturingMove()) {
            return;
        }

        checkers.forEach((Checker c) -> {
            c.possibleMoves.removeIf(Move::noCapture);
        });
        allMoves.removeIf(Move::noCapture);
    }

    /**
     * Check if this player has any capturing moves.
     *
     * @return T/F
     */
    private boolean hasCapturingMove() {
        return allMoves.stream().anyMatch((Move m) -> (!m.noCapture()));
    }
}
