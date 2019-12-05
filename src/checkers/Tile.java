package checkers;

import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

/**
 * Tile objects hold information which, in combination with Checker objects,
 * represents the state of the game. Each tile represents each position on the
 * checkers board.
 */
public class Tile extends Rectangle {

    Checker checker;
    int x, y;
    Tile[][] tiles;
    Paint originalColour, colour;
    double width, height;

    /**
     * Create a tile of specified size and colour. The coordinates of the tile
     * on the board are also specified here.
     *
     * @param width : Width of the tile in the GUI.
     * @param height : Height of the tile in the GUI.
     * @param fill : Colour of the tile in the GUI.
     * @param x : x coordinate on the board.
     * @param y : y coordinate on the board.
     * @param tiles : An array of tiles into which this tile will be inserted.
     * This represents the checker board.
     */
    Tile(double width, double height, Paint fill, int x, int y, Tile[][] tiles) {
        super(width, height, fill);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        originalColour = (Color) fill;
        colour = (Color) fill;
        this.tiles = tiles;
    }

    /**
     * Creates an identical copy of this tile.
     *
     * @param tiles : Refer to "tiles" in constructor.
     * @return A tile.
     */
    public Tile deepCopy(Tile[][] tiles) {
        Tile copy = new Tile(width, height, colour, x, y, tiles);

        return copy;
    }

    /**
     * Place a checker on this tile.
     *
     * @param checker : Checker to place on this tile.
     */
    public void placeChecker(Checker checker) {
        this.checker = checker;
    }

    /**
     * Remove a checker from this tile.
     */
    public void removeChecker() {
        checker = null;
    }

    /**
     * Check if this tile has a checker.
     *
     * @return T/F
     */
    public boolean hasChecker() {
        return checker != null;
    }

    /**
     * Get all tiles surrounding this tile. Only tiles at diagonals (same colour
     * as this tile) are returned as opposite colour tiles aren't used in
     * checkers.
     *
     * @return An ArrayList of surrounding tiles.
     */
    public ArrayList<Tile> getSurroundingTiles() {
        ArrayList<Tile> surroundingTiles = new ArrayList<>();

        surroundingTiles.add(getTile(x + 1, y + 1));
        surroundingTiles.add(getTile(x + 1, y - 1));
        surroundingTiles.add(getTile(x - 1, y + 1));
        surroundingTiles.add(getTile(x - 1, y - 1));

        surroundingTiles.removeIf(Objects::isNull);
        return surroundingTiles;
    }

    /**
     * Get a tile adjacent to "tile" and this tile such that the three tiles
     * form a diagonal line, starting with this tile and ending with the tile
     * resulting from this method.
     *
     * @param tile : Tile which serves as the midpoint of the diagonal line.
     * @return Tile which serves as the end of the diagonal line.
     */
    public Tile getAdjacentTile(Tile tile) {
        int horDir = (x - tile.x) * -1;
        int vertDir = (y - tile.y) * -1;

        return getTile(tile.x + horDir, tile.y + vertDir);
    }

    /**
     * Get a tile at specified coordinates from my tiles array (the board).
     *
     * @param x : x coordinate in the array.
     * @param y : y coordinate in the array.
     * @return A tile at (x,y). null if coordinates are invalid.
     */
    public Tile getTile(int x, int y) {
        if (x < 8 && y < 8 && x > -1 && y > -1) {
            return tiles[x][y];
        }

        return null;
    }

    /**
     * Change this tile's colour.
     *
     * @param colour : New colour.
     */
    public void setColour(Color colour) {
        setFill(colour);
        this.colour = colour;
    }

    /**
     * Resets this tile's colour to its original colour.
     */
    public void resetColour() {
        if (colour == originalColour) {
            return;
        }

        setFill(originalColour);
    }
}
