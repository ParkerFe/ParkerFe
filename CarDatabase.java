package com.example.finalprojectbase;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class CarDatabase extends Application {
    //TableView is a JavaFX class that displays data in columns and rows
    //It is a "generic" class, so you must put your object type in the diamonds
    //This type is set up in a class of the same name in this project - Cat.java
    private final TableView<Car> table = new TableView<>();

    //ObservableList is a type of list that listens for changes and will update itself automatically
    //It gets created using the observableArrayList() static method from FXCollections
    //Stores Cat objects and is also a generic
    private final ObservableList<Car> carData = FXCollections.observableArrayList();

    //Using the init() method to create the database and table if not already created
    @Override
    public void init() throws Exception {
        try (
            Connection connect = DriverManager.getConnection("jdbc:derby:CarDatabase; create = true");
            Statement state = connect.createStatement();
        ){
            DatabaseMetaData dbm = connect.getMetaData();
            ResultSet result = dbm.getTables(null, null, "CAR_INFO", null);
         System.out.println("ResultSet created.");
            if (result.next()) {
                System.out.println("car_info exists");
            } else {
                state.execute("create table car_info(make varchar(10), model varchar(10), car_year int, color varchar(10), used boolean)");
                System.out.println("car_info created");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //GUI components and event handlers for communicating with database
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Header Label and settings
        Label title = new Label("Car Information Form");
        title.setPrefWidth(850);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(20, 0, 40, 0));

        //Form Labels, TextFields, RadioButtons
        Label makeLabel = new Label("Make");
        TextField tfMake = new TextField();
        Label modelLabel = new Label("Model");
        TextField tfModel = new TextField();
        Label yearLabel = new Label("Year");
        TextField tfYear = new TextField();
        Label colorLabel = new Label("Color");
        TextField tfColor = new TextField();
        Label usedLabel = new Label("Used?");
        RadioButton rbYes = new RadioButton("yes");
        RadioButton rbNo = new RadioButton("no");
        ToggleGroup tgUsed = new ToggleGroup();
        rbNo.setToggleGroup(tgUsed);
        rbYes.setToggleGroup(tgUsed);
        Button submit = new Button("Submit Record");
        Button view = new Button("View Table");
        Button makesButton = new Button("View Makes");

        //Node font settings
        title.setFont(Font.font("Verdana", 28));
        makeLabel.setFont(Font.font("Verdana", 28));
        tfMake.setFont(Font.font("Verdana", 28));
        modelLabel.setFont(Font.font("Verdana", 28));
        tfModel.setFont(Font.font("Verdana", 28));
        yearLabel.setFont(Font.font("Verdana", 28));
        tfYear.setFont(Font.font("Verdana", 28));
        colorLabel.setFont(Font.font("Verdana", 28));
        tfColor.setFont(Font.font("Verdana", 28));
        usedLabel.setFont(Font.font("Verdana", 28));
        rbNo.setFont(Font.font("Verdana", 28));
        rbYes.setFont(Font.font("Verdana", 28));
        submit.setFont(Font.font("Verdana", 28));
        view.setFont(Font.font("Verdana", 28));
        makesButton.setFont(Font.font("Verdana", 28));

        //Form layout
        GridPane gp = new GridPane();
        gp.add(makeLabel, 0, 0);
        gp.add(tfMake, 1, 0, 2, 1);
        gp.add(modelLabel, 0, 1);
        gp.add(tfModel, 1, 1, 2, 1);
        gp.add(yearLabel, 0, 2);
        gp.add(tfYear, 1, 2, 2, 1);
        gp.add(colorLabel, 0, 3);
        gp.add(tfColor, 1, 3, 2, 1);
        gp.add(usedLabel, 0, 4);
        gp.add(rbYes, 1, 4);
        gp.add(rbNo, 2, 4);
        gp.add(submit, 0, 5);
        gp.add(view, 1, 5);
        gp.add(makesButton, 2, 5);
        gp.setVgap(30);
        gp.setHgap(20);

        //Main layout for header label and gridpane
        VBox mainLayout = new VBox(title, gp);
        mainLayout.setPadding(new Insets(20));

        //Configure tableview that will be displayed when user hits View button
        //Create each column with header text in quotes
        TableColumn<Car, String> makeCol = new TableColumn<>("Make");
        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        TableColumn<Car, Integer> yearCol = new TableColumn<>("Year");
        TableColumn<Car, Integer> colorCol = new TableColumn<>("Color");
        TableColumn<Car, Boolean> usedCol = new TableColumn<>("Used?");

        //Add TableColumns to the javafx TableView
        table.getColumns().add(makeCol);
        table.getColumns().add(modelCol);
        table.getColumns().add(yearCol);
        table.getColumns().add(colorCol);
        table.getColumns().add(usedCol);

        //Set styling for javafx table view and table columns - so pretty
        table.setStyle("-fx-font-size: 30px; -fx-pref-width: 880px");
        makeCol.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        yearCol.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        modelCol.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colorCol.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        usedCol.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");

        //Add table to its own scene to be used later in View event handler and pop-up stage
        Scene viewScene = new Scene(table);

        //Event handler for Submit button
        submit.setOnAction(e -> {
            try (
                //Connect to database and create PreparedStatement for adding records
                Connection connect = DriverManager.getConnection("jdbc:derby:CarDatabase");
                PreparedStatement addRecord = connect.prepareStatement("INSERT INTO car_info VALUES(?, ?, ?, ?, ?)");

            ){
                //Grab data from textfields and radiobuttons, store in variables
                String make = tfMake.getText();
                int year = Integer.parseInt(tfYear.getText());
                String model = tfModel.getText();
                String color = tfColor.getText();
                boolean used = rbYes.isSelected(); //isSelected will return true or false

                System.out.println("Data retrieved from controls.");

                //Put data into prepared statement and execute update to add to database table
                addRecord.setString(1, make);
                addRecord.setString(2, model);
                addRecord.setInt(3, year );
                addRecord.setString(4, color);
                addRecord.setBoolean(5, used);
                addRecord.executeUpdate();
                System.out.println("Added record to table");

                //After submitting, clear text fields in preparation for next entry.
                tfMake.setText("");
                tfModel.setText("");
                tfYear.setText("");
                tfColor.setText("");
                rbYes.setSelected(true);

                //Create new stage to be displayed and pass in scene for confirmation
                //if a sql exception occurs during the above process, we will never reach
                //the confirmation below
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful Operation");
                alert.setHeaderText(null);
                alert.setContentText("Record Added.");
                alert.show();
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        });

        //Event handler for View button to see entire table
        view.setOnAction(e->{
            //Clear the javafx tableview each time so that new views don't keep
            //getting appended to old views
            table.getItems().clear();

            try (
                //Connect to database and create statement for executing queries
                Connection connect = DriverManager.getConnection("jdbc:derby:CarDatabase");
                Statement state = connect.createStatement();
                //Using * will select all fields from the table
                ResultSet result = state.executeQuery("SELECT * FROM car_info");

            ){
                //Loop through the results from the query and grab each value
                while (result.next()) {
                    String make = result.getString("make");
                    int year = result.getInt("car_year");
                    String model = result.getString("model");
                    String color = result.getString("color");
                    boolean used = result.getBoolean("used");

                    //Store retrieved values from the resultset in a Cat object --> See Cat.java
                    //This is necessary for using observable list, which is necessary
                    //for adding values to your javafx tableview
                    Car currentCar = new Car(make, model, year, color, used);

                    //Add Cat object to observable list
                    carData.add(currentCar);

                    //tell each column which Cat data member to connect to
                    //THIS PART IS WEIRD!!!!!!
                    //notice that name, year, weight and vaccinated are my private data member variables from Cat
                    //the words in quotes are variables from Cat, not the names of my columns in the table
                    makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
                    yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
                    modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
                    colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
                    usedCol.setCellValueFactory(new PropertyValueFactory<>("used"));

                    //Add catData to the table
                    table.setItems(carData);
                }

                System.out.println("Table populated.");
                //A new stage so a new window pops up
                Stage smallStage = new Stage();
                //Add earlier scene to it that holds the table
                smallStage.setScene(viewScene);
                System.out.println("Scene set to stage.");
                smallStage.setTitle("All Records");
                smallStage.show();
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        });

        makesButton.setOnAction(e->{
            table.getItems().clear();

            try (
                Connection connect = DriverManager.getConnection("jdbc:derby:CarDatabase");
                Statement state = connect.createStatement();
                ResultSet result = state.executeQuery("SELECT make FROM car_info");

            ){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);

                StringBuilder sb = new StringBuilder();
                while (result.next()) {
                    sb.append(result.getString("make"));
                }
                alert.setContentText(sb.toString());
                alert.show();



            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        });

        //Main scene and stage
        primaryStage.setTitle("Car Information Entry Form");
        Scene scene = new Scene(mainLayout, 800, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop(){
        try{
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
