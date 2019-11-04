/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Marko
 */
public class Checker extends Circle {
    Tile currentTile;
    
    
    Checker(double radius, Paint fill, Tile tile) {
        super(radius, fill);
        
        place(tile);
    }
    
    public final void place(Tile tile) {
        currentTile.removeChecker();
        currentTile = tile;
        // can be expanded to place 'this' particular checker onto tile if need be
        currentTile.placeChecker(this);
    }
}
