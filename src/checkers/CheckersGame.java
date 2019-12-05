package checkers;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * CheckersGame is the main class of this application. It presents a GUI using
 * JavaFX.
 */
public class CheckersGame extends Application {

    static Stage primaryStage;

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Launches the GUI.
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        CheckersGame.primaryStage = primaryStage;
        primaryStage.setTitle("Checkers");

        launchScreen(primaryStage);
        primaryStage.show();
    }

    /**
     * Presents the game's starting screen.
     *
     * @param primaryStage
     */
    private void launchScreen(Stage primaryStage) {
        BorderPane layout = new BorderPane();

        setTitle(layout, "Checkers");

        Button playButton = new Button("Play");
        playButton.setMinWidth(200);
        playButton.setMinHeight(50);
        EventHandler<MouseEvent> playHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                difficultyScreen();
            }

        };
        playButton.addEventFilter(MouseEvent.MOUSE_CLICKED, playHandler);

        VBox vbox = new VBox();
        vbox.getChildren().add(playButton);

        vbox.setAlignment(Pos.CENTER);
        layout.setCenter(vbox);

        Scene scene = new Scene(layout, 1100, 850);
        primaryStage.setScene(scene);
    }

    /**
     * Sets a title on a BorderPane layout.
     *
     * @param layout
     * @param title
     */
    static private void setTitle(BorderPane layout, String title) {
        Text sceneTitle = new Text(title);
        sceneTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        BorderPane.setAlignment(sceneTitle, Pos.TOP_CENTER);
        layout.setTop(sceneTitle);
    }

    /**
     * Displays the difficulty selection screen.
     */
    static private void difficultyScreen() {
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
                launchGame(difficulty);
            }
        };

        for (Button btn : options) {
            btn.setMinWidth(200);
            btn.setMinHeight(50);
            btn.addEventFilter(MouseEvent.MOUSE_CLICKED, difficultyHandler);
        }

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        for (int i = 0; i < options.length; i++) {
            vbox.getChildren().add(options[i]);
        }

        vbox.setAlignment(Pos.CENTER);
        layout.setCenter(vbox);

        Scene scene = new Scene(layout, 1100, 850);
        primaryStage.setScene(scene);
    }

    /**
     * Displays the game board.
     *
     * @param difficulty
     */
    static private void launchGame(String difficulty) {
        BorderPane layout = new BorderPane();

        Game game = new Game(difficulty);
        GridPane board = game.board;
        board.setAlignment(Pos.TOP_LEFT);
        layout.setLeft(board);

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER_RIGHT);

        Button help = new Button("Help");
        help.setMinWidth(200);
        help.setMinHeight(50);
        EventHandler<MouseEvent> helpHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.toggleHelp();
            }
        };
        help.addEventFilter(MouseEvent.MOUSE_CLICKED, helpHandler);

        Button rules = new Button("Rules");
        rules.setMinWidth(200);
        rules.setMinHeight(50);
        EventHandler<MouseEvent> rulesHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                openRulesWindow();
            }
        };
        rules.addEventFilter(MouseEvent.MOUSE_CLICKED, rulesHandler);

        vbox.getChildren().add(help);
        vbox.getChildren().add(rules);
        layout.setCenter(vbox);

        Scene scene = new Scene(layout, 1100, 850);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * Displays the rules window.
     */
    static private void openRulesWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(primaryStage);
        window.setTitle("Rules");

        String rules = ("- Moves are only allowed on black squares (diagonally)\n"
                + "- Regular (non-king) checkers may only move forward\n"
                + "- A checker making a non-capturing move may only move one tile\n"
                + "- A checker making a capturing move may jump over the opposing checker landing on its other side\n"
                + "- Multiple capturing moves may be made in a single turn\n"
                + "- When a checker reaches the edge furthest from its starting point, it becomes a king\n"
                + "- Kings may move forward and backward (including capturing forwards and backwards)\n"
                + "- If a piece captures a king, it becomes king itself\n"
                + "- A player wins once all of opponent's pieces have been captured, or once the opponent can no longer move");

        Label rulesLabel = new Label(rules);

        Scene scene = new Scene(rulesLabel, 600, 300);
        window.setScene(scene);
        window.show();
    }

    /**
     * Displays the game over window.
     *
     * @param gameOver
     */
    static public void gameOver(String gameOver) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Play again?");

        BorderPane layout = new BorderPane();
        setTitle(layout, gameOver);

        Button yes = new Button("Yes");
        Button no = new Button("No");

        EventHandler<MouseEvent> playHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                difficultyScreen();
            }
        };
        yes.addEventFilter(MouseEvent.MOUSE_CLICKED, playHandler);

        EventHandler<MouseEvent> quitHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        };
        no.addEventFilter(MouseEvent.MOUSE_CLICKED, quitHandler);

        HBox hbox = new HBox();
        hbox.setSpacing(30);
        hbox.getChildren().add(yes);
        hbox.getChildren().add(no);

        hbox.setAlignment(Pos.CENTER);
        layout.setCenter(hbox);

        Scene scene = new Scene(layout, 500, 300);
        dialog.setScene(scene);
        dialog.show();
    }
}
