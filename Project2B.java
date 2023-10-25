package com.example.lab6;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

// Parker Fellows
// This program uses JavaFX to create a trivia game with graphical user input. The questions/answers are stored using a
// hashmap and are displayed when the user submits their answers with buttons.

public class Project2B extends Application {

    HashMap<String, String> statesMap = new HashMap<>();
    List<String> states;


    private int index = 0;
    private int numCorrect = 0;
    private int totalNum = 0;


    @Override
    public void init() throws Exception {
        //Location URL may need tweaked if on Mac
        File text = new File("src/main/resources/statecapitals.txt");
        System.out.println("File Found.");
        Scanner read = new Scanner(text);
        for (int i = 0; i < 50; i++) {
            String state = read.nextLine();     //reads in state from lines 1, 3, 5, etc.
            String capital = read.nextLine();   //reads in capitals from lines 2, 4, 6, etc.
            statesMap.put(state, capital);      //puts key/value pairs in hashmap
        }
        System.out.println(statesMap);          //double-checks contents by printing to console
        states = new ArrayList<>(statesMap.keySet());
        Collections.shuffle(states);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

            Label stateLabel = new Label(states.get(index));
            stateLabel.setFont(new Font("Verdana", 35));

            Label capitalLabel = new Label();
            capitalLabel.setFont(new Font("Verdana", 30));

            Label answer = new Label();
            answer.setFont(new Font("Verdana", 30));

            TextField letterField = new TextField();

            Button checkButton = new Button("Check");
            checkButton.setFont(new Font("Comic Sans MS", 20));

            Button nextButton = new Button("Next");
            nextButton.setFont(new Font("Comic Sans MS", 20));

            Label correct = new Label("Correct: ");
            correct.setFont(new Font("Comic Sans MS", 20));
            Label correctNums = new Label(String.valueOf(numCorrect));
            correctNums.setFont(new Font("Comic Sans MS", 20));

            Label total = new Label("Total: ");
            total.setFont(new Font("Comic Sans MS", 20));
            Label totalNums = new Label(String.valueOf(totalNum));
            totalNums.setFont(new Font("Comic Sans MS", 20));


            HBox tallyBox = new HBox(correct, correctNums, total, totalNums);
            tallyBox.setSpacing(11.0);


            VBox root = new VBox(stateLabel,capitalLabel,answer,letterField,checkButton,nextButton,tallyBox);
            root.setPadding(new Insets(20));
            root.setSpacing(20);

        checkButton.setOnAction(e-> {

            if (letterField.getText().isEmpty()) {
                answer.setText("Please enter an answer");
            }


            String currentState = stateLabel.getText();
            if (letterField.getText().equalsIgnoreCase(statesMap.get(currentState))) {

                answer.setText("Correct!");
                capitalLabel.setText(statesMap.get(currentState));
                correctNums.setText(String.valueOf(numCorrect));
                totalNums.setText(String.valueOf(totalNum));

                numCorrect++;
                totalNum++;
                states.remove(currentState);

                } else {
                    answer.setText("Incorrect");
                    totalNum++;
                }


        });

        nextButton.setOnAction(e->{

            if(letterField.getText().isEmpty()){
                answer.setText("Please enter an answer");
            }
            else if (index < 50){
                index++;
                String listKey = states.get(index);
                stateLabel.setText(listKey);
                letterField.clear();
            } else {
                double doubleNum = (double) numCorrect / totalNum;
                String finalNum = String.valueOf(doubleNum);
                answer.setText("Game is over. You scored" + finalNum + "%");
            }
        });



            Scene scene = new Scene(root, 500, 500);
            primaryStage.setTitle("States and Capitals Trivia Cards");
            primaryStage.setScene(scene);
            primaryStage.show();

        }



    public static void main(String[] args) {
        launch(args);
    }
}


