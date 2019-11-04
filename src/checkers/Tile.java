/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Marko
 */
public class Tile extends Rectangle {
    Checker checker;
    
    Tile(double width, double height, Paint fill) {
        super(width, height, fill);
    }
    
    public void placeChecker(Checker c) {
        if(!hasChecker())
            checker = c;
    }
    
    public void removeChecker() {
        checker = null;
    }
    
    public boolean hasChecker() {
        return checker != null;
    }
}
