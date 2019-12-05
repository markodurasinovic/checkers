package checkers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AIPlayer is a subclass of Player. It acts as the autonomous game-playing
 * agent.
 */
public class AIPlayer extends Player {

    Checker moveChecker;
    Tile moveTile;
    String difficulty;
    Player opponent;
    HashMap<Move, Integer> moveScores = new HashMap<>();

    /**
     * Create a new AIPlayer, containing its owned checkers, the player's
     * checker colour, and the difficulty of the AIPlayer - specified by the
     * user through the GUI.
     *
     * @param checkers
     * @param colour
     * @param opponent
     * @param difficulty : "Easy", "Medium", or "Hard"
     */
    AIPlayer(ArrayList<Checker> checkers, String colour, Player opponent, String difficulty) {
        super(checkers, colour);
        this.opponent = opponent;
        moveChecker = null;
        moveTile = null;
        this.difficulty = difficulty;
    }

    /**
     * Returns the move that the AIPlayer will perform, based on difficulty
     * specified by the user. "Easy" looks at depth of 2, "Medium" at depth of
     * 4. and "Hard" at depth of 6 - meaning that a "Hard" AIPlayer will look 6
     * moves ahead when deciding its move.
     *
     * @return
     */
    public Move getMove() {
        switch (difficulty) {
            case "Easy":
                return getBestMove(2);
            case "Medium":
                return getBestMove(4);
            case "Hard":
                return getBestMove(6);
            default:
                return null;
        }
    }

    /**
     * Gets the move with the best score.
     *
     * @param depth
     * @return
     */
    public Move getBestMove(int depth) {
        evaluateMoves(depth);

        Move bestMove = allMoves.get(0);
        for (Move m : moveScores.keySet()) {
            if (moveScores.get(m) > moveScores.get(bestMove)) {
                bestMove = m;
            }
        }

        return bestMove;
    }

    /**
     * Evaluates future states of the game. The AIPlayer will look "depth" game
     * states ahead of the present state and evaluate each of next state's
     * possible moves using a negamax algorithm.
     *
     * @param depth
     */
    private void evaluateMoves(int depth) {
        moveScores.clear();
        for (Move m : allMoves) {
            m.addData(Board.tiles, checkers, opponent.checkers);
            moveScores.put(m, m.evaluateMove(depth));
        }
    }
}
