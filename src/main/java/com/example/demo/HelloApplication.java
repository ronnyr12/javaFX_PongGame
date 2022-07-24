package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//step1 - add before coding
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    //step2 - variables
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;

    private int ballYSpeed = 1;
    private int ballXSpeed = 1;

    private double ballXPos = WIDTH / 2;
    private double ballYPos = HEIGHT / 2;

    private int scoreP1 = 0;
    private int scoreP2 = 0;

    private double player1YPos = HEIGHT / 2;
    private double player2YPos = HEIGHT / 2;

    private double player1XPos = 0;
    private double player2XPos = WIDTH - PLAYER_WIDTH;

    private boolean gameStarted;


    @Override
    public void start(Stage stage) throws IOException {
        //step3 - window definitions

        //stage represents the window, inside the stage is the scene which represents the content
        stage.setTitle("P O N G");

        //Canvas is an image that can be drawn on using a set of graphics commands provided
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        //the GraphicsContext is the library that provides the ability of drawing in the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //timeline extends Object, it Defines target values at a specified point in time for a set of variables that are interpolated along a Timeline
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc))); //this is a lambda method
        tl.setCycleCount(Timeline.INDEFINITE);  //the number of cycles in our animation

        //step4 - setting the game control (using the mouse control)
        canvas.setOnMouseMoved(e -> player1YPos = e.getY()); //moving up and down
        canvas.setOnMouseClicked(e -> gameStarted = true);    //pressing for starting the game

        //step4 - creating the visual
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }

    private void run(GraphicsContext gc) {
        //step5 - set background color, gc is the graphic object
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        //step6 - set the text color
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));

        //set7 - game logic
        if (gameStarted) {
            //set ball movement on the x-axis and y-axis
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            //the computer opponent
            if (ballXPos < WIDTH - WIDTH / 4) {
                player2YPos = ballYPos - PLAYER_HEIGHT / 2;
            } else {
                player2YPos = (ballYPos > player2YPos + PLAYER_HEIGHT / 2) ? (player2YPos += 1) : (player2YPos - 1);
            }

            //draw ball
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

        } else {
            //game isn't started so set the starting text
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("ON Click", WIDTH / 2, HEIGHT / 2);

            //reset the ball start position
            ballXPos = WIDTH / 2;
            ballYPos = HEIGHT / 2;

            //the ball gettes faster during the game, so when start new to reset
            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
        }

        //keeping sure the ball stays in the canvas
        if (ballYPos > HEIGHT || ballYPos < 0)
            ballYSpeed *= -1;

        //step8 - pointing the game (if player misses the ball the opponent gets a point)

        //computer gets a point
        if (ballXPos < player1XPos - PLAYER_WIDTH) {
            scoreP2++;
            gameStarted = false;
        }
        //player1 gets a point
        if (ballXPos > player2XPos + PLAYER_WIDTH) {
            scoreP1++;
            gameStarted = false;
        }

        //step9 - increasing the speed of the ball after hitting a player
        if (((ballXPos + BALL_R > player2XPos) &&
                ballYPos >= player2YPos &&
                ballYPos <= player2YPos + PLAYER_HEIGHT) ||
                ((ballXPos < player1XPos + PLAYER_WIDTH) &&
                        ballYPos >= player1YPos &&
                        ballYPos <= player1YPos + PLAYER_HEIGHT)) {

            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        //step10 - draw score and players
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t"+ scoreP2, WIDTH/2, 100);   //draw score

        gc.fillRect(player2XPos, player2YPos, PLAYER_WIDTH, PLAYER_HEIGHT); //draw player2
        gc.fillRect(player1XPos, player1YPos, PLAYER_WIDTH, PLAYER_HEIGHT); //draw player1

    }

    public static void main(String[] args) {
        launch();
    }
}