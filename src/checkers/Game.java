package checkers;

import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The game's functionality and logic is implemented here. Manipulation of the
 * GUI board representation and checker moves is performed by this class.
 */
public class Game {

    Board board;
    Player player1, nextTurn;
    AIPlayer player2;
    HashMap<Tile, EventHandler> tileFilters = new HashMap<>();
    boolean helpOn = false;

    /**
     * Create a new game with a user-selected difficulty. This instantiates the
     * human player and the AI player - with a specified difficulty.
     *
     * @param difficulty : Difficulty of the AI player.
     */
    Game(String difficulty) {
        board = new Board();
        player1 = new Player(board.blueCheckers, "blue");
        player2 = new AIPlayer(board.whiteCheckers, "white", player1, difficulty);
        nextTurn = player1;

        calculateMoves();
        addHandlers();
    }

    /**
     * Calculate all the moves currently available to each player. If "Help" is
     * turned on, highlights each tile which contains a moveable checker.
     */
    private void calculateMoves() {
        player1.calculateAllMoves();
        player2.calculateAllMoves();

        if (helpOn) {
            board.resetAllTileColours();
            for (Move m : player1.allMoves) {
                m.checker.currentTile.setColour(Color.YELLOW);
            }
        } else {
            board.resetAllTileColours();
        }
    }

    /**
     * Add event handlers to each human-player's checker. This allows the game
     * to be controlled through the GUI.
     */
    private void addHandlers() {
        for (Checker checker : player1.checkers) {
            addCheckerHandler(checker);
        }
    }

    /**
     * Adds a handler to each checker.
     *
     * @param checker : Checker to add the handler to.
     */
    private void addCheckerHandler(Checker checker) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            /**
             * Adds a handler to checker which, on a mouse-click, shows the user
             * each available move if the "Help" option is enabled and activates
             * each moveable tile for that checker.
             *
             * @param event : A MouseEvent triggering this function.
             */
            @Override
            public void handle(MouseEvent event) {
                clearTileHandlers();
                board.resetMoveTileColours();
                if (helpOn) {
                    checker.showPossibleMoves();
                }

                checker.getPossibleMoveTiles().forEach((tile) -> {
                    addTileHandler(tile, checker);
                });
            }
        };

        checker.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    /**
     * This method is called from a checker's event handler. Adds an event
     * handler to a tile so that the checker which called this method moves to
     * the tile on subsequent click.
     *
     * @param tile : Tile to add the handler to.
     * @param checker : Checker which is adding the handler to the tile.
     */
    private void addTileHandler(Tile tile, Checker checker) {

        EventHandler<MouseEvent> moveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                performMove(checker, tile);
            }
        };

        tile.addEventFilter(MouseEvent.MOUSE_CLICKED, moveHandler);
        tileFilters.put(tile, moveHandler);
    }

    /**
     * Moves a checker to a tile. If a player loses as a consequence, the game
     * is terminated. Otherwise, the player whose turn it is is switched. If the
     * AI player is the next player to take a turn, a method to perform the AI
     * move is called.
     *
     * @param checker : Checker to be moved.
     * @param tile : Tile to move the checker to.
     */
    private void performMove(Checker checker, Tile tile) {
        moveChecker(checker, tile);

        board.resetMoveTileColours();
        clearTileHandlers();
        if (gameFinished()) {
            return;
        }

        switchTurn();

        calculateMoves();

        if (nextTurn == player2) {
            performAiMove();
        }
    }

    /**
     * Removes event handlers from each tile.
     */
    public void clearTileHandlers() {
        if (tileFilters.isEmpty()) {
            return;
        }

        tileFilters.keySet().forEach((tile) -> {
            tile.removeEventFilter(MouseEvent.MOUSE_CLICKED, tileFilters.get(tile));
        });
    }

    /**
     * Check if a player has lost and return the appropriate response.
     */
    private boolean gameFinished() {
        if (player1.hasLost()) {
            CheckersGame.gameOver("You lost!");
            return true;
        }

        if (player2.hasLost()) {
            CheckersGame.gameOver("You won!");
            return true;
        }

        return false;
    }

    /**
     * Switches the player who's taking the next turn.
     */
    private void switchTurn() {
        if (nextTurn == player1) {
            nextTurn = player2;
        } else {
            nextTurn = player1;
        }
    }

    /**
     * Gets the AI player's move and performs it.
     */
    private void performAiMove() {
        Move mv = player2.getMove();
        performMove(mv.checker, mv.tile);
    }

    /**
     * Moves checker to a tile.
     *
     * @param checker
     * @param tile
     */
    private void moveChecker(Checker checker, Tile tile) {
        move(checker, tile);

        if (checker.isCapturingMove(tile)) {
            capture(checker, tile);
        }
    }

    /**
     * Move a checker to a tile. This is also reflected in the GUI.
     *
     * @param checker
     * @param tile
     */
    private void move(Checker checker, Tile tile) {
        checker.move(tile);

        board.getChildren().remove(checker);
        board.add(checker, tile.x, tile.y);
    }

    /**
     * Perform a capturing move and updates the game state by removing captured
     * checkers. This is also reflected in the GUI.
     *
     * @param checker
     * @param tile
     */
    private void capture(Checker checker, Tile tile) {
        checker.performCapturingMove(tile);
        for (Checker c : checker.getTakenCheckers(tile)) {
            board.getChildren().remove(c);

            if (player1.ownsChecker(c)) {
                player1.removeChecker(c);
            } else {
                player2.removeChecker(c);
            }
        }
    }

    /**
     * Toggles the "Help" option which, when turned on, highlights all the tiles
     * with moveable checkers. Upon clicking on a checker, it also highlights
     * all the tiles which it can move to.
     */
    public void toggleHelp() {
        helpOn = !helpOn;
        calculateMoves();
    }
}
