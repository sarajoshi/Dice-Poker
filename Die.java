package CS2450_HW2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

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

public class Die {
    private boolean held;
    private int value;
    private static Image[] diceImages;
    private static Image[] heldImages;
    private ImageView view;
    private Image image;

    public Die(){ }

    public Die(Image[] diceImages, Image[] heldImages) {
        Die.diceImages = diceImages;
        Die.heldImages = heldImages;

        held = false;
        value = 1;
        image = diceImages[0]; 
        view = new ImageView(image);
    }

    public void setHeld(boolean held) {
        this.held = held;
        if(!held) {
            switch (value) {
                case 1: setImage(diceImages[0]); break;
                case 2: setImage(diceImages[1]); break;
                case 3: setImage(diceImages[2]); break;
                case 4: setImage(diceImages[3]); break;
                case 5: setImage(diceImages[4]); break;
                case 6: setImage(diceImages[5]); break;
                default: System.out.println("Error at setting held"); break;
            }
        } else {
            switch (value) {
                case 1: setImage(heldImages[0]); break;
                case 2: setImage(heldImages[1]); break;
                case 3: setImage(heldImages[2]); break;
                case 4: setImage(heldImages[3]); break;
                case 5: setImage(heldImages[4]); break;
                case 6: setImage(heldImages[5]); break;
                default: System.out.println("Error at setting held"); break;
            }
        }
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void roll() {
        if(!held) {
            Random rand = new Random();
            value = rand.nextInt(5) + 1;
            rollImage();
        }
    }

    public ImageView getView(){
        return view;
    }

    public boolean isHeld(){
        return held;
    }

    private void setImage(Image image) {
        this.image = image;
        view.setImage(image);
    }

    private void rollImage(){
        switch (value) {
            case 1: setImage(diceImages[0]); break;
            case 2: setImage(diceImages[1]); break;
            case 3: setImage(diceImages[2]); break;
            case 4: setImage(diceImages[3]); break;
            case 5: setImage(diceImages[4]); break;
            case 6: setImage(diceImages[5]); break;
            default: System.out.println("Error at rolling image"); break;
        }
    }

    public int getValue() {
        return value;
    }

    public void reset() {
        if(isHeld()) {
            setHeld(false);
            setImage(diceImages[value - 1]);
        }
    }

}