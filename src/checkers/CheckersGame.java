/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Marko
 */
public class CheckersGame extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Game game = new Game();
        
        Scene scene = new Scene(game.board);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
