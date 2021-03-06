package controllers;

import game.GameBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Group canvas;

    @FXML
    private Button play;

    @FXML
    private Button resettle;

    private Properties gameProperties;
    private GameBoard board;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameProperties = new Properties();
        try (FileInputStream stream = new FileInputStream(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("game.properties")).getPath())) {
            gameProperties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        board = new GameBoard(canvas, gameProperties);
        board.settle();

        play.setOnAction(e -> board.play());
        resettle.setOnAction(e -> board.resettle());
    }
}
