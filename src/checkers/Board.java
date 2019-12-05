package checkers;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * The Board class holds the state representation. Two sets of Checkers are
 * instantiated. A set of Tiles is also created to represent each individual
 * tile on the board. This board also doubles up as a GUI board due to being a
 * sub-class of GridPane.
 */
public class Board extends GridPane {

    ArrayList<Checker> blueCheckers = new ArrayList<>();
    ArrayList<Checker> whiteCheckers = new ArrayList<>();

    static public Tile[][] tiles = new Tile[8][8];

    Board() {
        draw();
        populate();
        showCheckers();
    }

    /**
     * Draws the GUI by placing each individual tile into the board.
     */
    private void draw() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        addTile("red", i, j);
                    } else {
                        addTile("black", i, j);
                    }
                } else {
                    if (j % 2 == 0) {
                        addTile("black", i, j);
                    } else {
                        addTile("red", i, j);
                    }
                }
            }
        }
    }

    /**
     * Creates a Tile object and adds it to the board.
     *
     * @param colour : The colour of the particular tile.
     * @param x : The x coordinate of the tile on the board.
     * @param y : The y coordinate of the tile on the board.
     */
    private void addTile(String colour, int x, int y) {
        Color tileColour = Color.BLACK;
        if (colour.equals("red")) {
            tileColour = Color.RED;
        }

        Tile tile = new Tile(100, 100, tileColour, x, y, tiles);
        add(tile, x, y);
        tiles[x][y] = tile;
    }

    /**
     * Populates the board with checkers.
     */
    private void populate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        addChecker("white", j, i);
                    }
                } else {
                    if (j % 2 == 0) {
                        addChecker("white", j, i);
                    }
                }
            }
        }

        for (int i = 7; i > 4; i--) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        addChecker("blue", j, i);
                    }
                } else {
                    if (j % 2 == 0) {
                        addChecker("blue", j, i);
                    }
                }
            }
        }
    }

    /**
     * Adds a Checker object and adds it to the state representation by
     * associating it with its tile.
     *
     * @param colour : The colour of the checker.
     * @param x : The x coordinate of the checker on the board.
     * @param y : The y coordinate of the checker on the board.
     */
    private void addChecker(String colour, int x, int y) {
        Color checkerColour = Color.BLUE;
        if (colour.equals("white")) {
            checkerColour = Color.WHITE;
        }

        Tile tile = tiles[x][y];
        Checker checker = new Checker(100 / 2, checkerColour, tile);
        tile.placeChecker(checker);

        if (colour.equals("blue")) {
            blueCheckers.add(checker);
        } else {
            whiteCheckers.add(checker);
        }
    }

    /**
     * Displays each checker on the board by adding it to the GridPane.
     */
    private void showCheckers() {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (tile.hasChecker()) {
                    add(tile.checker, tile.x, tile.y);
                }
            }
        }
    }

    /**
     * Resets each tile colour to its original colour.
     */
    public void resetAllTileColours() {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.resetColour();
            }
        }
    }

    /**
     * Resets each tile which doesn't contain a checker to its original colour.
     */
    public void resetMoveTileColours() {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (!tile.hasChecker()) {
                    tile.resetColour();
                }
            }
        }
    }
}
