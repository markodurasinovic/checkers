/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Marko
 */
public class CheckersGame extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");
        
        launchScreen(primaryStage);
        primaryStage.show();
    }
    
    private void launchScreen(Stage primaryStage) {
        BorderPane layout = new BorderPane();
                
        setTitle(layout, "Checkers");
        
        Button playButton = new Button("Play");
        playButton.setMinWidth(200);
        playButton.setMinHeight(50);
        EventHandler<MouseEvent> playHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                difficultyScreen(primaryStage);
            }
        
        };
        playButton.addEventFilter(MouseEvent.MOUSE_CLICKED, playHandler);
        
        VBox vbox = new VBox();
        vbox.getChildren().add(playButton);
        
        vbox.setAlignment(Pos.CENTER);
        layout.setCenter(vbox);        
        
        Scene scene = new Scene(layout, 850, 850);
        primaryStage.setScene(scene);
    }
    
    private void setTitle(BorderPane layout, String title) {
        Text sceneTitle = new Text(title);
        sceneTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        BorderPane.setAlignment(sceneTitle, Pos.TOP_CENTER);
        layout.setTop(sceneTitle);
    } 
    
    private void difficultyScreen(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        
        setTitle(layout, "Choose a difficulty:");
        
        Button[] options = new Button[]{
            new Button("Easy"),
            new Button("Medium"),
            new Button("Hard")
        };
        
        EventHandler<MouseEvent> difficultyHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Button b = (Button) event.getSource();
                String difficulty = b.getText();
                launchGame(primaryStage, difficulty);
            }
        };
        
        for(Button btn : options) {
            btn.setMinWidth(200);
            btn.setMinHeight(50);
            btn.addEventFilter(MouseEvent.MOUSE_CLICKED, difficultyHandler);
        }
                
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        for(int i = 0; i < options.length; i++) {
            vbox.getChildren().add(options[i]);
        }
        
        vbox.setAlignment(Pos.CENTER);
        layout.setCenter(vbox);
        
        Scene scene = new Scene(layout, 850, 850);
        primaryStage.setScene(scene);
    }

    private void launchGame(Stage primaryStage, String difficulty) {
        Game game = new Game(difficulty);
        Scene scene = new Scene(game.board);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
    
}
