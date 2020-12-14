package CS2450_HW2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author SaraJoshi
 * CS 2450 Section 2
 * October 30, 2019
 * Professor Jonathan Johannsen and Tony Diaz
 * Assignment 2
 * 
 * An implementation of a dice poker game.
 * 
 * Rules:
 * The player has three rolls per turn.  
 * After the first roll, if the user clicks a dice, it will remain "held" and not roll the next time the user rolls
 * the dice. Its image should update accordingly. Once three rolls are up, determine what is the best "hand" the 
 * user has. Inform the user of what hand they have and how many points they received this round. The "Roll" button
 * should say "Play Again" once the player's turn is over. This should clear the Dice, allowing the user to start over.
 * Keep track of how many points overall the user has (i.e. keep a running total of how much they scored each turn)
 * 
 * Hands:
 * 5 of a kind - 10 points
 * Straight (5 numbers in a row i.e. 1, 2, 3, 4, 5 or 2, 3, 4, 5, 6) - 8 points
 * Four of a Kind - 7 points
 * Full House (3 of one kind, 2 of another) - 6 points
 * 3 of a kind - 5 points
 * 2 pair (2 of one kind, 2 of another, and 1 other value) - 4 points
 * 2 of a kind - 1 point 
 *
 */

public class Assignment2 extends Application {

    private final int TOTAL_DICE = 5;
    private BorderPane bp;
    private static boolean ready;
    private HBox layout; 
    private VBox top;
    private VBox bottom;
    private Label osLabel; 
    private String osText;
    private int score = 0;
    private Label rsLabel;
    private String rsText;
    private int rs = 0;
    private final int MAX_ROLL = 3;
    private Label rolls_remainingLabel;
    private String rollsRemainingText;
    private int rollsRemaining;
    private Image[] diceImages;
    private Image[] heldImages;
    private Die[] dice;
    private Button button;
    private String buttonText;
    private double imageHeight = 100;
    private double imageWidth = 100;
    private boolean preserveRatio = false;
    private boolean smooth = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ready = false;

        bp = new BorderPane();
        layout = new HBox();
        top = new VBox();
        bottom = new VBox();

        osText = "Overall Score: ";
        osLabel = new Label(osText);
        osLabel.setId("os");
        
        rsText = "Round Score: ";
        rsLabel = new Label("Round Score: ");

        rollsRemaining = 0;
        rollsRemainingText = "Rolls Remaining: ";
        rolls_remainingLabel = new Label(rollsRemainingText + rollsRemaining);

        diceImages = new Image[6];
        heldImages = new Image[6];
        dice = new Die[TOTAL_DICE];

        buttonText = "Roll Dice";
        button = new Button("Start Game");

        top.getChildren().add(osLabel);

        bottom.getChildren().add(button);
        bottom.getChildren().add(rsLabel);
        bottom.getChildren().add(rolls_remainingLabel);

        bp.setTop(top);
        bp.setCenter(layout);
        bp.setBottom(bottom);

        bp.setPrefSize(600,400);
        BorderPane.setMargin(bottom, new Insets(0,0,30,0));
        BorderPane.setMargin(top, new Insets(30,0,0,0));
        BorderPane.setMargin(layout, new Insets(15,15,15,15));

        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        top.setAlignment(Pos.CENTER);

        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(15);

        try {
            setImages();
        } catch (FileNotFoundException e) {
            System.out.println("Dice images not found " + e);
        }

        for (int i = 0; i < TOTAL_DICE; i++) {
            dice[i] = new Die(diceImages,heldImages);
            layout.getChildren().add(dice[i].getView());

            int captureI = i;
            dice[i].getView().setOnMouseClicked(mouseEvent -> {
                if (ready) {
                    dice[captureI].setHeld(!dice[captureI].isHeld());
                }
            });
        }

        button.setOnAction(event -> {
            ready = true;
            if(rollsRemaining > 0) {
                rollsRemaining--;
                rolls_remainingLabel.setText(rollsRemainingText + rollsRemaining);

                for (int i = 0; i < TOTAL_DICE; i++) {
                    dice[i].roll();
                }

                if(rollsRemaining == 0) {
                    rs = calculate();
                    rsLabel.setText(rsText + rs);

                    button.setText("Play Again");

                }
            } else { 

                score += rs;
                osLabel.setText(osText + score);
                rsLabel.setText(rsText);
                ready = false;
                resetGame();
            }

        });

        Scene scene = new Scene(bp);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dice Poker");
        primaryStage.show();
    }

    private int calculate(){

        Set<Integer> value= new HashSet<>();
        int[] arr = new int[TOTAL_DICE];
        for(int i = 0; i < TOTAL_DICE; i++) {
            value.add(dice[i].getValue());
            arr[i] = dice[i].getValue();
        }
        Arrays.sort(arr);

        if(value.size() == 1) {
            rolls_remainingLabel.setText("Five of a Kind!");
            return 10;
        }

        if(arr[0] == (arr[1] - 1) && arr[1] == (arr[2] - 1) &&
           arr[2] == (arr[3] - 1) && arr[3] == (arr[4] - 1)) {
            rolls_remainingLabel.setText("Straight!");
            return 8;
        }

        if (value.size() == 2) {
            if(arr[3] == arr[0] || arr[1] == arr[4]) {
                rolls_remainingLabel.setText("Four of a Kind!");
                return 7;
            }
            else {
                rolls_remainingLabel.setText("Full House!");
                return 6;
            }
        }
        if(value.size() == 3) {
            if((arr[0] == arr[1] && arr[1] == arr[2]) ||
               (arr[1] == arr[2] && arr[2] == arr[3]) ||
               (arr[2] == arr[3] && arr[3] == arr[4])) {
                rolls_remainingLabel.setText("Three of a Kind!");
                return 5;
            } else {
                rolls_remainingLabel.setText("Two Pairs!");
                return 4;
            }
        }

        if(value.size() == 4) {
            rolls_remainingLabel.setText("Two of a Kind!");
            return 1;
        }

        rolls_remainingLabel.setText("Nothing!");
        return 0;

    }
    
    private void resetGame() {
        for(int i = 0; i < TOTAL_DICE; i++) {
            dice[i].reset();
        }
        rollsRemaining = MAX_ROLL;
        button.setText(buttonText);
        rolls_remainingLabel.setText(rollsRemainingText + rollsRemaining);
    }

    private void setImages() throws FileNotFoundException {
        diceImages[0] = new Image("file:./src/CS2450_HW2/DiceImages/Dice1.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        diceImages[1] = new Image("file:./src/CS2450_HW2/DiceImages/Dice2.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        diceImages[2] = new Image("file:./src/CS2450_HW2/DiceImages/Dice3.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        diceImages[3] = new Image("file:./src/CS2450_HW2/DiceImages/Dice4.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        diceImages[4] = new Image("file:./src/CS2450_HW2/DiceImages/Dice5.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        diceImages[5] = new Image("file:./src/CS2450_HW2/DiceImages/Dice6.png",
                imageHeight, imageWidth, preserveRatio, smooth);

        heldImages[0] = new Image("file:./src/CS2450_HW2/DiceImages/Dice1Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        heldImages[1] = new Image("file:./src/CS2450_HW2/DiceImages/Dice2Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        heldImages[2] = new Image("file:./src/CS2450_HW2/DiceImages/Dice3Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        heldImages[3] = new Image("file:./src/CS2450_HW2/DiceImages/Dice4Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        heldImages[4] = new Image("file:./src/CS2450_HW2/DiceImages/Dice5Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
        heldImages[5] = new Image("file:./src/CS2450_HW2/DiceImages/Dice6Held.png",
                imageHeight, imageWidth, preserveRatio, smooth);
    }
}