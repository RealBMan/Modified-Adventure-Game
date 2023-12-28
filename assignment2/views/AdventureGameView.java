package views;

import AdventureModel.*;
import Trolls.SportTroll;
import Trolls.MathTroll;
import Trolls.DeathTroll;
import Trolls.iSpyTroll;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;

import javax.swing.Timer;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 */
public class AdventureGameView {

    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton, settings, Enlarge, darkMode, leaderBoardButton, ttsButton; //buttons
    Leaderboard leaderboard;// attribute that store the leaderboard instance
    Button leftButton, midButton, rightButton; // Button for player movement
    Boolean helpToggle = false; //is help on display?
    Boolean settingsToggle = false; //Keeps track of whether the settings page is still on or not
    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    Label objLabel; //Label that displays Object on the screen
    Label invLabel; //Label that displays Inventory on the screen
    Label commandLabel; //Label that displays/contains the commands
    VBox textEntry; //Vbox that is used for the user input
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input
    Boolean bigText = false; //Keeps track of if the text is enlarged
    HBox heartImages = new HBox(); //HBox for the hearts of the player
    VBox roomPane; //The vbox for that displays the content in the room
    private boolean dark_Mode; //Keeps of track of if dark mode is currently toggled or not
    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    private TextArea instructions; //TextArea that stores all the instructions
    private Timer timer; //to keep time
    private int seconds; //variable for timer

    private boolean tts_Mode; //check if text to speech is enabled

    Label timerLabel; //label to store the timer



    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("adetunj9's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        instructions = new TextArea(this.model.getInstructions());
        instructions.setWrapText(true);
        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        tts_Mode = false; // disable tts by default

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));
        //heart code
        ColumnConstraints column4 = new ColumnConstraints(150);
        RowConstraints row4 = new RowConstraints();
        row4.setVgrow( Priority.SOMETIMES );
        leftButton = new Button("Go Left");
        leftButton.setId("Left");
        customizeButton(leftButton, 150, 50);
        //addLeftEvent();
        midButton = new Button("TODO");
        midButton.setId("Mid");
        customizeButton(midButton, 150, 50);
        //addMidEvent();
        rightButton = new Button("Go Right");
        rightButton.setId("Right");
        customizeButton(rightButton, 150, 50);
        //addRightEvent();
        HBox botButtons = new HBox();
        //botButtons.getChildren().addAll(leftButton,midButton,rightButton);
        botButtons.setSpacing(10);
        botButtons.setAlignment(Pos.CENTER);

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(15);
        column2.setPercentWidth(65);
        column3.setPercentWidth(15);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row2.setPercentHeight(69);


        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        ImageView pic = new ImageView(this.model.getDirectoryName()+"/setting image/"+"settings_button.png");
        pic.setFitHeight(25);
        pic.setFitWidth(25);
        pic.setPreserveRatio(true);

        settings = new Button("",pic);
        settings.setId("Settings");
        settings.setAlignment(Pos.CENTER_LEFT);
        customizeButton(settings, 25, 25);
        makeButtonAccessible(settings,"Settings button", "This button displays the settings","This button displays the settings of the game. Click to modify the game accordingly");
        addSettingEvent();

        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        //Leaderboard
        leaderboard = Leaderboard.getInstance();
        leaderBoardButton = new Button("Leaderboard");
        leaderBoardButton.setId("Leaderboard");
        customizeButton(leaderBoardButton, 300, 50);
        makeButtonAccessible(leaderBoardButton,"Leaderboard button","This button displays the leaderboard from a file.", "This button displays the leaderboard from a file. CLick it in order to display the leaderboard.");
        addLeaderboardEvent();

        Enlarge= new Button("Enlarge");
        loadButton.setId("Enlarge");
        customizeButton(Enlarge, 150, 50);
        makeButtonAccessible(Enlarge, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addEnlarge();

        dark_Mode = true;
        darkMode = new Button("LightMode");
        darkMode.setId("Dark Mode");
        customizeButton(darkMode, 150, 50);
        makeButtonAccessible(darkMode, "Dark Mode Button", "This button switches between Dark and Light Mode", "This button allosws users to switch between dark and light mode based on their preference to what makes the game easier to play.");
        setDarkMode();


        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();


        ttsButton = new Button("Text to Speech");
        ttsButton.setId("Text_to_Speech");
        customizeButton(ttsButton, 200, 50);
        makeButtonAccessible(ttsButton, "Text to Speech Button", "This button turns text into speech", "This button reads words to the user by converting alt text to a voice when hovering a button.");
        addTTSEvent();

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        VBox topButtons = new VBox();
        topButtons.getChildren().addAll(settings);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        //labels for inventory and room items
        objLabel =  new Label("Objects in room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));
        objLabel.setWrapText(true);
        objLabel.setOnMouseEntered(f -> lbl_tts(objLabel.getText()));

        invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER_RIGHT);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));
        invLabel.setOnMouseEntered(f -> lbl_tts(invLabel.getText()));
        topButtons.getChildren().addAll(invLabel);

        //readd invLabel here - took out because overlap with settings btn
        //topButtons.getChildren().add(invLabel);


        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        //gridPane.add( settings, 2, 0, 1, 1 );  // Add buttons

        gridPane.add(topButtons, 2, 0, 1, 1 );  // Add buttons
        //gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label
        gridPane.add(botButtons,0,3,3,1);

        commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        gridPane.setMinSize(800, 1000); // Set a minimum size (optional)
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillHeight(gridPane, true);
        GridPane.setFillWidth(gridPane, true);

        // Render everything
        var scene = new Scene( gridPane,1000,800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(true);
        this.stage.show();
        if (bigText){
            changeTextSize();
        }
        //Create the timer label
        timerLabel = new Label("0:00:00");
        timerLabel.setFont(new Font("Arial",  26));
        timerLabel.setStyle("-fx-text-fill: white;");
        if (timer == null) {
            timer = new Timer(1000, e -> {
                seconds++;
                updateTimer();
            });
            timer.start();
        }
        HBox heartsAndTimer = new HBox();
        heartsAndTimer.getChildren().addAll(heartImages, timerLabel);
        heartsAndTimer.setSpacing(10);
        heartsAndTimer.setAlignment(Pos.CENTER);
        gridPane.add(heartsAndTimer, 1, 0, 1, 1 );  // Add health
    }



    public Node getHeartImg(boolean half){
        String heartImage;
        if (half){
            heartImage = this.model.getDirectoryName() + "/objectImages/half_heart.png";
        }else{
            heartImage = this.model.getDirectoryName() + "/objectImages/heart.png";

        }

        Image heartImageFile = new Image(heartImage);
        ImageView heartImageView = new ImageView(heartImageFile);
        heartImageView.setPreserveRatio(true);
        heartImageView.setFitWidth(25);
        heartImageView.setFitHeight(25);

        //set accessible text
        heartImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        return heartImageView;
    }
    public void updateHearts(){
        heartImages.getChildren().clear();
        for (int i =0; i< this.model.player.getHitpoints(); i +=10){
            if (this.model.player.getHitpoints() - i == 5){
                Button temp = new Button("");
                temp.setPrefSize(10,10);
                temp.setGraphic(getHeartImg(true));
                heartImages.getChildren().add(temp);
            }
            else{
                Button temp = new Button("");
                temp.setPrefSize(10,10);
                temp.setGraphic(getHeartImg(false));
                heartImages.getChildren().add(temp);
            }
        }
    }

    /**
     * updateTimer
     * __________________________
     * Updates the timer label and player's time taken regularly.
     */
    private void updateTimer() {
        if(Objects.equals(this.model.getPlayer().username, "")){
            seconds = 0;
        }
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;
        Platform.runLater(() -> timerLabel.setText(String.format("%d:%02d:%02d", hours, minutes, remainingSeconds)));
        this.model.getPlayer().timeTaken = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
    }



    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        //add your code here!
        EventHandler<KeyEvent> event = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
               if(keyEvent.getCode().getCode() == KeyCode.ENTER.getCode()){
                   if (helpToggle && !(inputTextField.getText().equals("h"))){
                       helpToggle = false;
                   }
                   String action = inputTextField.getText();
                   if (!action.isEmpty()){
                       action = action.strip();
                       inputTextField.clear();
                       submitEvent(action);
                   }
               } else if (keyEvent.getCode().getCode() == KeyCode.TAB.getCode()){
                   inputTextField.requestFocus();
                }
            }
        };
       inputTextField.addEventFilter(KeyEvent.KEY_PRESSED, event);
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.contains("Troll") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
            updateHearts();
        } else if (output.equals("SportTroll")) {
            int i =0;
            while(i<this.model.player.getCurrentRoom().getMotionTable().getDirection().size()){
                if (this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName()!= null && this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")){
                    break;
                }
                ++i;
            }
            Room r = this.model.getRooms().get(this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDestinationRoom());
            int j = 0;
            while (j< r.objectsInRoom.size()){
                if(r.objectsInRoom.get(j) instanceof Weapon){
                    break;
                }
                ++j;
            }
            SportTroll troll = new SportTroll(this.model.player.difficultyHard,(Weapon)r.objectsInRoom.get(j));
            playgame(troll);
        } else if (output.equals("MathTroll")) {
            int i = 0;
            while (i < this.model.player.getCurrentRoom().getMotionTable().getDirection().size()) {
                if (this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName() != null && this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")) {
                    break;
                }
                ++i;
            }
            Room r = this.model.getRooms().get(this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDestinationRoom());
            int j = 0;
            while (j < r.objectsInRoom.size()) {
                if (r.objectsInRoom.get(j) instanceof Weapon) {
                    break;
                }
                ++j;
            }
            MathTroll troll = new MathTroll(this.model.player.difficultyHard, (Weapon) r.objectsInRoom.get(j));
            Mathplaygame(troll);
        } else if (output.equals("DeathTroll")) {
            int i = 0;
            while (i < this.model.player.getCurrentRoom().getMotionTable().getDirection().size()) {
                if (this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName() != null && this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")) {
                    break;
                }
                ++i;
            }
            Room r = this.model.getRooms().get(this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDestinationRoom());
            int j = 0;
            while (j <= r.objectsInRoom.size() - 1) {
                if (r.objectsInRoom.get(j) instanceof Weapon) {
                    break;
                }
                j++;
            }
            DeathTroll troll = new DeathTroll(this.model.player.difficultyHard, (Weapon) r.objectsInRoom.get(j));
            Deathplaygame(troll);
        }else if (output.equals("iSpyTroll")) {
            int i = 0;
            while (i < this.model.player.getCurrentRoom().getMotionTable().getDirection().size()) {
                if (this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName() != null && this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")) {
                    break;
                }
                ++i;
            }
            Room r = this.model.getRooms().get(this.model.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDestinationRoom());
            int j = 0;
            while (j <= r.objectsInRoom.size() - 1) {
                if (r.objectsInRoom.get(j) instanceof Weapon) {
                    break;
                }
                j++;
            }
            System.out.println(r.objectsInRoom.get(j).getName());
            iSpyTroll troll = new iSpyTroll(this.model.player.difficultyHard, (Weapon) r.objectsInRoom.get(j));
            iSpyPlayGame(troll);
        } else if (output.equals("GAME OVER")) {

            //tries to add player to the leaderboard
            List<Object> player1= new ArrayList<>();
            timer.stop();
            player1.add(this.model.player.username);
            player1.add(timeseconds(this.model.player.timeTaken));
            leaderboard.addPlayer(player1);

            updateScene("");
            updateItems();



            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            inputTextField.setEditable(false);
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            updateScene("");
            updateItems();
            pause.setOnFinished(event -> {
                String[] move = this.model.player.getCurrentRoom().getCommands().split(",");
                ArrayList <String> move_2 = new ArrayList<>();
                move_2.addAll(Arrays.asList(move));
                if (move_2.contains("FORCED")){
                    inputTextField.setEditable(true);
                    submitEvent("FORCED");
                }
            });
            pause.play();
        }
    }

    private int timeseconds(String timeTaken) {
        String[] list_time = timeTaken.split(":");
        int hours = Integer.parseInt(list_time[0]);
        int minutes = Integer.parseInt(list_time[1]);
        int seconds = Integer.parseInt(list_time[2]);

        return hours * 3600 + minutes * 60 + seconds;
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        String commands = this.model.player.getCurrentRoom().getCommands();
        String description = "You can move in the following directions: \n" + "\n" + commands;
        roomDescLabel.setText(description);
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {
        heartImages.getChildren().clear();
        updateHearts();
        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        changeTextSize();
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);

        inputTextField.setDisable(false);
        settings.setDisable(false);
        if(dark_Mode){
            roomDescLabel.setStyle("-fx-text-fill: white;");
            roomPane.setStyle("-fx-background-color: #000000;");
        } else{
            roomDescLabel.setStyle("-fx-text-fill: black;");
            roomPane.setStyle("-fx-background-color: white;");
        }

        gridPane.add(roomPane, 1, 1);
        //stage.sizeToScene();

        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     * 
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView 
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     * 
     * Images of each object are in the assets 
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        ArrayList<AdventureObject> objects = this.model.player.getCurrentRoom().objectsInRoom;
        ArrayList<String> object_player = this.model.player.getInventory();
        ArrayList<Node> object_drawn = new ArrayList<Node>();
        ArrayList<Node> player_object_drawn = new ArrayList<Node>();

        if(!objectsInRoom.getChildren().isEmpty()){
            for (int i = 0; i < objectsInRoom.getChildren().size();i++){
                object_drawn.add(objectsInRoom.getChildren().get(i));
            }
            for (int j = 0; j < object_drawn.size();j++){
                objectsInRoom.getChildren().remove(object_drawn.get(j));
            }
        }

        if(!objectsInInventory.getChildren().isEmpty()){
            for (int i = 0; i < objectsInInventory.getChildren().size();i++){
                player_object_drawn.add(objectsInInventory.getChildren().get(i));
            }
            for (int j = 0; j < player_object_drawn.size();j++){
                objectsInInventory.getChildren().remove(player_object_drawn.get(j));
            }
        }

        for (int i = 0; i < objects.size(); i++){
            ImageView picture = new ImageView(this.model.getDirectoryName() + "/objectImages/" + objects.get(i).getName() + ".jpg");
            String object = objects.get(i).getName();
            String name = object + "Button";
            String shortString = "This button represents the item in the current room";
            String longString = "This button represents the objects in the current room. Click it to take the object from the room and add the object to your/Player's inventory";
            picture.setFitWidth(100);
            picture.setPreserveRatio(true);
            Button image = new Button(objects.get(i).getName(),picture);
            image.setOnMouseEntered(f -> lbl_tts(image.getAccessibleText()));
            image.setPrefHeight(105.00);
            image.setContentDisplay(ContentDisplay.TOP);
            image.setOnAction(e -> {
                submitEvent("take"+" "+object);
            });
            makeButtonAccessible(image,name,shortString,longString);
            objectsInRoom.getChildren().add(image);
        }

        for (int j = 0; j < object_player.size(); j++){
            ImageView picture = new ImageView(this.model.getDirectoryName() + "/objectImages/" + object_player.get(j) + ".jpg");
            String object = object_player.get(j);
            String name = object + "Button";
            String shortString = "This button represents the item in the your/Player's inventory";
            String longString = "This button represents the objects in the your/Player's inventory. Click it to drop the object from your inventory and place it in the room you are in.";
            picture.setFitWidth(100);
            picture.setPreserveRatio(true);
            Button image = new Button(object_player.get(j),picture);
            image.setOnMouseEntered(f -> lbl_tts(image.getAccessibleText()));
            image.setPrefHeight(105.00);
            image.setContentDisplay(ContentDisplay.TOP);
            image.setOnAction(e -> {
                submitEvent("drop"+" "+object);
            });
            makeButtonAccessible(image,name,shortString,longString);
            objectsInInventory.getChildren().add(image);
        }

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        if (dark_Mode){
            scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        } else{
            scO.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
        }
        scO.setFitToWidth(true);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        if (dark_Mode){
            scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        } else {
            scI.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
        }
        gridPane.add(scO,0,1);
        gridPane.add(scI,2,1);

    }

    /**
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- set settingsToggle to false
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        ObservableList<Node> child = gridPane.getChildren();
        instructions.setWrapText(true);
        //instructions.setBackground(new Background(new BackgroundFill(
                //Color.BLACK, new CornerRadii(0), new Insets(0))));
        instructions.setEditable(false);
        if (dark_Mode){
            instructions.setStyle("-fx-control-inner-background: black;");
        } else{
            instructions.setStyle("-fx-control-inner-background: white;");
        }
        Node content = null;
        for (int i = 0; i < child.size(); i++){
            if(GridPane.getColumnIndex(child.get(i)) == 1 && GridPane.getRowIndex(child.get(i)) == 1){
                content = child.get(i);
                break;
            }
        }
        if (!helpToggle){
            gridPane.getChildren().remove(content);
            gridPane.add(instructions, 1,1);
            helpToggle = true;
            settingsToggle= false;
        } else {
            gridPane.getChildren().remove(instructions);
            updateScene("");
            helpToggle = false;
        }
        if (bigText){
            changeTextSize();
        } else{
            changeTextSize();
        }
    }

    /**
     * showSettings
     * __________________________
     *
     * This method will remove elements int he 1,1 cell of the gridpane
     * to display the settings
     */
    public void showSettings() {
        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
        ScrollPane element = createSidebar();
        if(dark_Mode){
            element.setStyle("-fx-background: #000000;");
        } else{
            element.setStyle("-fx-background: #FFFFFF;");
        }
        if(!settingsToggle){
            gridPane.add(element, 1, 1);
            settingsToggle = true;
            helpToggle = false;
        }else {
            updateScene("");
            updateItems();
            settingsToggle = false;
        }
    }

    /**
     * createSideBar
     * __________________________
     *
     * This private method constructs a scroll pane where all the button
     * for setting purpose will lie
     * @return
     */
    private ScrollPane createSidebar(){
        Label instru = new Label("Settings");
        instru.setAlignment(Pos.CENTER);
        instru.setStyle("-fx-text-fill: black;");
        instru.setFont(new Font("Arial", 20));
        instru.setPrefWidth(640);
        instru.setStyle("-fx-padding: 30 30 0 30");
        instru.setWrapText(true);

        VBox sidebar = new VBox(instru, helpButton,loadButton,saveButton,Enlarge,darkMode,leaderBoardButton,ttsButton);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setSpacing(30);
        ScrollPane scp = new ScrollPane(sidebar);
        scp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scp.setFitToHeight(true);
        scp.setFitToWidth(true);
        return scp;
    }

    /**
     * playgame
     * __________________________
     *
     * This method displays all the elements on the screen and calls the createTrollscreen_Death(t) method to get the
     * Vbox to be displayed in the middle gridpane, which then allows the user to play against the troll.
     *
     * @param t This is the SportTroll object that the player is going to fight/encounter
     */
    public void playgame(SportTroll t){
            gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
            gridPane.add(createTrollscreen(t),1,1);
            inputTextField.setDisable(true);
        objectsInRoom.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        objectsInInventory.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        settings.setDisable(true);
    }

    /**
     * Mathplaygame
     * __________________________
     *
     * This method displays all the elements on the screen and calls the createTrollscreen_Death(t) method to get the
     * Vbox to be displayed in the middle gridpane, which then allows the user to play against the troll.
     *
     * @param t This is the MathTroll object that the player is going to fight/encounter
     */
    public void Mathplaygame(MathTroll t){
        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
        gridPane.add(createTrollscreen_Math(t),1,1);
        inputTextField.setDisable(true);
        objectsInRoom.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        objectsInInventory.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        settings.setDisable(true);
    }

    /**
     * Deathplaygame
     * __________________________
     *
     * This method displays all the elements on the screen and calls the createTrollscreen_Death(t) method to get the
     * Vbox to be displayed in the middle gridpane, which then allows the user to play against the troll.
     *
     * @param t This is the DeathTroll object that the player is going to fight/encounter
     */
    public void Deathplaygame(DeathTroll t){
        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
        gridPane.add(createTrollscreen_Death(t),1,1);
        inputTextField.setDisable(true);
        objectsInRoom.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        objectsInInventory.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        settings.setDisable(true);
    }

    /**
     * iSpyPlayGame
     * __________________________
     *
     * This method displays all the elements on the screen and calls the createTrollscreen_iSpy(t) method to get the
     * Vbox to be displayed in the middle gridpane, which then allows the user to play against the troll.
     *
     * @param t This is the iSpyTroll object that the player is going to fight/encounter
     */
    public void iSpyPlayGame(iSpyTroll t){
        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
        gridPane.add(createTrollscreen_iSpy(t),1,1);
        inputTextField.setDisable(true);
        objectsInRoom.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        objectsInInventory.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        });
        settings.setDisable(true);
    }

    /**
     * createTrollscreen_iSpy
     * -----------------------
     *
     * This method creates the troll ui to be fought against by the player.
     * This vbox contains 4 labels, an answer field, a picture and addiSpyTrolltexthandling method
     * to help check whether an answer is correct or wrong
     *
     * @param t
     * @return VBox
     */
    private VBox createTrollscreen_iSpy(iSpyTroll t) {
        Label title = new Label("iSpy Troll");
        title.setOnMouseEntered(f -> lbl_tts(title.getText()));
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-text-fill: white;");
        title.setFont(new Font("Arial", 26));

        Label instructions = new Label(t.giveInstructions());
        instructions.setOnMouseEntered(f -> lbl_tts(instructions.getText()));
        instructions.setStyle("-fx-text-fill: white;");
        instructions.setFont(new Font("Arial", 16));
        instructions.setWrapText(true);
        VBox title_box = new VBox(title, instructions);
        title_box.setSpacing(10);
        title_box.setAlignment(Pos.TOP_CENTER);

        Label temp = new Label();
        temp.setText("Click the troll to start the game!");
        temp.setAlignment(Pos.CENTER);
        temp.setStyle("-fx-text-fill: white;");
        temp.setFont(new Font("Arial", 16));
        temp.setOnMouseEntered(f -> lbl_tts(temp.getText()));


        ImageView picture = new ImageView("Trolls/TrollImages/iSpyTroll.png");
        picture.setFitWidth(400);
        picture.setFitHeight(400);
        picture.setPreserveRatio(true);

        Label question = new Label();
        question.setVisible(false);
        String quest = t.askQuestion();
        question.setText(quest);
        question.setAlignment(Pos.CENTER);
        question.setStyle("-fx-text-fill: white;");
        question.setOnMouseEntered(f -> lbl_tts(question.getText()));
        question.setFont(new Font("Arial", 16));
        picture.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
              picture.setImage(new Image("Trolls/TrollImages/ispy.png"));
              temp.setVisible(false);
              question.setVisible(true);

          }
      }
        );



        TextField answer_field = new TextField();
        answer_field.setFont(new Font("Arial", 16));
        answer_field.setFocusTraversable(true);


        answer_field.setAccessibleRole(AccessibleRole.TEXT_AREA);
        answer_field.setAccessibleRoleDescription("Text Entry Box");
        answer_field.setOnMouseEntered(f -> lbl_tts(answer_field.getAccessibleText()));
        answer_field.setAccessibleText("Enter answer in this box.");
        answer_field.setAccessibleHelp("This is the area in which you can enter answer to the troll's questions.  Enter an answer and hit return to continue.");



        VBox troll_screen = new VBox(title_box, picture, temp,question, answer_field);
        troll_screen.setAlignment(Pos.CENTER);
        troll_screen.setSpacing(10);
        troll_screen.setStyle("-fx-text-fill: white;");
        addiSpyTrolltexthandling(t,answer_field,question);
        return troll_screen;

    }

    /**
     * createTrollscreen_Death
     * __________________________
     *
     * This method creates a Vbox that displays the trolls in the middle gridpane on the screen. This VBox contains
     * the image of the troll, the description of the troll and the button where the user clicks to attack the troll.
     *
     * @param t This is the DeathTroll object that the player is going to fight/encounter
     * @return Vbox which would be displayed in the middle gridpane on the screen.
     */
    private VBox createTrollscreen_Death(DeathTroll t){
        Label title = new Label("DEATH TROLL");
        title.setOnMouseEntered(f -> lbl_tts(title.getText()));
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-text-fill: white;");
        title.setFont(new Font("Arial", 26));

        Label instructions = new Label(t.giveInstructions());
        instructions.setOnMouseEntered(f -> lbl_tts(instructions.getText()));
        instructions.setStyle("-fx-text-fill: white;");
        instructions.setFont(new Font("Arial", 16));
        instructions.setWrapText(true);
        VBox title_box = new VBox(title, instructions);
        title_box.setSpacing(10);
        title_box.setAlignment(Pos.TOP_CENTER);

        ImageView picture = new ImageView("Trolls/TrollImages/trolls2.jpeg");
        picture.setFitWidth(400);
        picture.setFitHeight(400);
        picture.setPreserveRatio(true);

        Label question = new Label();
        question.setText("ATTACK");
        question.setAlignment(Pos.CENTER);
        question.setStyle("-fx-text-fill: white;");
        question.setOnMouseEntered(f -> lbl_tts(question.getText()));
        question.setFont(new Font("Arial", 16));

        Button attack = new Button("Attack");
        attack.setId("Attack");
        customizeButton(attack, 100, 50);
        attack.setOnMouseEntered(f -> lbl_tts(attack.getText()));
        makeButtonAccessible(attack, "Attack Button", "This attacks the troll.", "This attack the troll. Click it in order to inflict damage on the troll.");

        attack.setOnAction(e -> {
            Fight(t, question);

        });

        VBox troll_screen = new VBox(title_box, picture, question, attack);
        troll_screen.setAlignment(Pos.CENTER);
        troll_screen.setSpacing(10);
        troll_screen.setStyle("-fx-text-fill: white;");
        return troll_screen;
    }

    /**
     * createTrollscreen_Math
     * __________________________
     *
     * This method creates a Vbox that displays the trolls in the middle gridpane on the screen. This VBox contains
     * the image of the troll, the description of the troll and the input text field where the player submits their
     * answer to.
     *
     * @param t This is the MathTroll object that the player is going to fight/encounter
     * @return Vbox which would be displayed in the middle gridpane on the screen.
     */
    private VBox createTrollscreen_Math(MathTroll t){
        Label title = new Label("MATH TROLL");
        title.setOnMouseEntered(f -> lbl_tts(title.getText()));
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-text-fill: white;");
        title.setFont(new Font("Arial", 26));

        Label instructions = new Label(t.giveInstructions());
        instructions.setOnMouseEntered(f -> lbl_tts(instructions.getText()));
        instructions.setStyle("-fx-text-fill: white;");
        instructions.setFont(new Font("Arial", 16));
        instructions.setWrapText(true);
        VBox title_box = new VBox(title, instructions);
        title_box.setSpacing(10);
        title_box.setAlignment(Pos.TOP_CENTER);

        ImageView picture = new ImageView("Trolls/TrollImages/trolls3.png");
        picture.setFitWidth(400);
        picture.setFitHeight(400);
        picture.setPreserveRatio(true);

        Label question = new Label();
        String quest = t.askQuestion();
        question.setText(quest);
        question.setOnMouseEntered(f -> lbl_tts(question.getText()));
        question.setAlignment(Pos.CENTER);
        question.setStyle("-fx-text-fill: white;");
        question.setFont(new Font("Arial", 16));

        TextField answer_field = new TextField();
        answer_field.setFont(new Font("Arial", 16));
        answer_field.setFocusTraversable(true);

        answer_field.setAccessibleRole(AccessibleRole.TEXT_AREA);
        answer_field.setAccessibleRoleDescription("Text Entry Box");
        answer_field.setAccessibleText("Enter answer in this box.");
        answer_field.setOnMouseEntered(f -> lbl_tts(answer_field.getAccessibleText()));
        answer_field.setAccessibleHelp("This is the area in which you can enter answer to the troll's questions.  Enter an answer and hit return to continue.");

        VBox troll_screen = new VBox(title_box, picture, question, answer_field);
        troll_screen.setAlignment(Pos.CENTER);
        troll_screen.setSpacing(10);
        troll_screen.setStyle("-fx-text-fill: white;");
        addMathTrolltexthandling(t, answer_field, question);
        return troll_screen;
    }

    /**
     * createTrollscreen
     * __________________________
     *
     * This method creates a Vbox that displays the trolls in the middle gridpane on the screen. This VBox contains
     * the image of the troll, the description of the troll and the input text field where the player submits their
     * answer to.
     *
     * @param t This is the SportTroll object that the player is going to fight/encounter
     * @return Vbox which would be displayed in the middle gridpane on the screen.
     */
    private VBox createTrollscreen(SportTroll t){
        Label title = new Label("SPORT_TROLL");
        title.setOnMouseEntered(f -> lbl_tts(title.getText()));
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-text-fill: white;");
        title.setFont(new Font("Arial", 26));

        Label instructions = new Label(t.giveInstructions());
        instructions.setOnMouseEntered(f -> lbl_tts(instructions.getText()));
        instructions.setStyle("-fx-text-fill: white;");
        instructions.setFont(new Font("Arial", 16));
        VBox title_box = new VBox(title, instructions);
        title_box.setSpacing(10);
        title_box.setAlignment(Pos.TOP_CENTER);

        ImageView picture = new ImageView("Trolls/TrollImages/troll.jpg");
        picture.setFitWidth(400);
        picture.setFitHeight(400);
        picture.setPreserveRatio(true);

        Label question = new Label();
        Random rand = new Random();
        int r_int = rand.nextInt(0,20);
        ArrayList<String> quest = new ArrayList<String>(t.q_and_a.keySet());
        question.setText(quest.get(r_int));
        question.setOnMouseEntered(f -> lbl_tts(question.getText()));
        question.setAlignment(Pos.CENTER);
        question.setStyle("-fx-text-fill: white;");
        question.setFont(new Font("Arial", 16));

        TextField answer_field = new TextField();
        answer_field.setFont(new Font("Arial", 16));
        answer_field.setFocusTraversable(true);

        answer_field.setAccessibleRole(AccessibleRole.TEXT_AREA);
        answer_field.setAccessibleRoleDescription("Text Entry Box");
        answer_field.setAccessibleText("Enter answer in this box.");
        answer_field.setOnMouseEntered(f -> lbl_tts(answer_field.getAccessibleText()));
        answer_field.setAccessibleHelp("This is the area in which you can enter answer to the troll's questions.  Enter an answer and hit return to continue.");

        VBox troll_screen = new VBox(title_box, picture, question, answer_field);
        troll_screen.setAlignment(Pos.CENTER);
        troll_screen.setSpacing(10);
        troll_screen.setStyle("-fx-text-fill: white;");
        addSportTrolltexthandling(t, answer_field, question);
        return troll_screen;
    }

    /**
     * addSportTrolltexthandling
     * __________________________
     *
     * This method handles the event when the player submits an answer to the SportTroll's question. The method
     * checks if the answer is correct or not. If the answer is wrong then the troll does damage to the player,
     * if the answer is correct then the player does damage to the troll. The label question is updated on the
     * screen to display the next question that is randomly chosen from the bank of questions.
     *
     * @param t This is the SportTroll object that the player is going to fight/encounter
     * @param question This label that displays the current question on the screen that the player
     *                 has to answer.
     */
    private void addSportTrolltexthandling(SportTroll t, TextField a_field, Label question){
        Player p = this.model.player;
        AdventureGame mod = this.model;
        a_field.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent event){
                if (event.getCode().equals(KeyCode.ENTER)){
                    String answer = a_field.getText().trim();
                    a_field.clear();
                    t.playGame(question.getText(), answer, p);
                    updateHearts();
                    if(t.troll_hp<=0){
                        int i =0;
                        while(i<mod.player.getCurrentRoom().getMotionTable().getDirection().size()){
                            if (mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName()!= null && mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")){
                                break;
                            }
                            ++i;
                        }
                        mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).keyName = "";
                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });
                        submitEvent(mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDirection());
                    } else if (p.hitpoints<=0) {
                        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
                        inputTextField.setDisable(true);

                        settings.setDisable(true);


                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });
                        ImageView lost = new ImageView("Games/TinyGame/room-images/lost.jpg");
                        lost.setPreserveRatio(true);
                        lost.setFitHeight(400);
                        lost.setFitWidth(400);
                        VBox lost1 = new VBox(lost);
                        lost1.setAlignment(Pos.CENTER);
                        gridPane.add(lost1,1,1);

                        PauseTransition pause = new PauseTransition(Duration.seconds(10));
                        pause.setOnFinished(event2 -> {
                            Platform.exit();
                        });
                        pause.play();
                    } else{
                        playgame(t);
                    }
                }
            }
        });
    }

    /**
     * addMathTrolltexthandling
     * __________________________
     *
     * This method handles the event when the player submits an answer to the MathTroll's question. The method
     * checks if the answer is correct or not. If the answer is wrong then the troll does damage to the player,
     * if the answer is correct then the player does damage to the troll.
     *
     * @param t This is the MathTroll object that the player is going to fight/encounter
     * @param question This label that displays the current question on the screen that the player
     *                 has to answer.
     */
    private void addMathTrolltexthandling(MathTroll t, TextField a_field, Label question){
        Player p = this.model.player;
        AdventureGame mod = this.model;
        a_field.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent event){
                if (event.getCode().equals(KeyCode.ENTER)){
                    String answer = a_field.getText().trim();
                    a_field.clear();
                    boolean val = t.guessAnswer(answer, p);
                    t.playGame(val,p);
                    updateHearts();
                    if(t.troll_hp<=0){
                        int i =0;
                        while(i<mod.player.getCurrentRoom().getMotionTable().getDirection().size()){
                            if (mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName()!= null && mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")){
                                break;
                            }
                            ++i;
                        }
                        mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).keyName = "";
                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });
                        submitEvent(mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDirection());
                    } else if (p.hitpoints<=0) {
                        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
                        inputTextField.setDisable(true);

                        settings.setDisable(true);


                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });
                        ImageView lost = new ImageView("Games/TinyGame/room-images/lost.jpg");
                        lost.setPreserveRatio(true);
                        lost.setFitHeight(400);
                        lost.setFitWidth(400);
                        VBox lost1 = new VBox(lost);
                        lost1.setAlignment(Pos.CENTER);
                        gridPane.add(lost1,1,1);

                        PauseTransition pause = new PauseTransition(Duration.seconds(10));
                        pause.setOnFinished(event2 -> {
                            Platform.exit();
                        });
                        pause.play();
                    } else{
                        Mathplaygame(t);
                    }
                }
            }
        });

    }

    /**
     * addiSpyTrolltexthandling
     * -------------------------
     *
     * This method handles the event when the player submits an answer to the iSpyTroll's question. The method
     * checks if the answer is correct or not. If the answer is wrong then the troll does damage to the player,
     * if the answer is correct then the player does damage to the troll.
     *
     * @param t This is a iSpyTroll object
     * @param a_field This is the answer field which will have the user's inputed answer
     * @param question This is the current question being asked by the Troll
     */
    private void addiSpyTrolltexthandling(iSpyTroll t, TextField a_field, Label question){
        Player p = this.model.player;
        AdventureGame mod = this.model;
        a_field.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent event){
                if (event.getCode().equals(KeyCode.ENTER)){
                    String answer = a_field.getText().trim();
                    a_field.clear();
                    boolean val = t.guessAnswer(answer, p);
                    t.playGame(val,p);
                    updateHearts();
                    if(t.troll_hp<=0){
                        int i =0;
                        while(i<mod.player.getCurrentRoom().getMotionTable().getDirection().size()){
                            if (mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName()!= null && mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")){
                                break;
                            }
                            ++i;
                        }
                        mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).keyName = "";
                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(false);
                            }
                        });
                        submitEvent(mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDirection());
                    } else if (p.hitpoints<=0) {
                        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
                        inputTextField.setDisable(true);

                        settings.setDisable(true);


                        objectsInRoom.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });

                        objectsInInventory.getChildren().forEach(node -> {
                            if (node instanceof Button) {
                                Button button = (Button) node;
                                button.setDisable(true);
                            }
                        });
                        ImageView lost = new ImageView("Games/TinyGame/room-images/lost.jpg");
                        lost.setPreserveRatio(true);
                        lost.setFitHeight(400);
                        lost.setFitWidth(400);
                        VBox lost1 = new VBox(lost);
                        lost1.setAlignment(Pos.CENTER);
                        gridPane.add(lost1,1,1);

                        PauseTransition pause = new PauseTransition(Duration.seconds(10));
                        pause.setOnFinished(event2 -> {
                            Platform.exit();
                        });
                        pause.play();
                    } else{
                        iSpyPlayGame(t);
                    }
                }
            }
        });

    }

    /**
     * DarkMode
     * __________________________
     *
     * This method changes the visual settings of the game and changes it from light mode to dark mode and
     * dark mode to light mode.
     *
     * If dark_Mode is FALSE:
     * -- It changes all GUI elements on the screen i.e. all the elements within the gridPane from black to white.
     * -- It also changes all text within the screen from white to black. (if the text previously is blakc then
     *    it changes the text to black).
     * -- sets dark_Mode to true.
     *
     * If dark_Mode is TRUE:
     * -- It changes all GUI elements on the screen i.e. all the elements within the gridPane from white to black.
     * -- It also changes all text within the screen from black to white. (if the text previously is white then
     *    it changes the text to black).
     *-- sets dark_Mode to false.
     */
    public void DarkMode(){
        ObservableList<Node> child = gridPane.getChildren();
        if (dark_Mode){
            dark_Mode = false;
            darkMode.setText("DarkMode");
            timerLabel.setStyle("-fx-text-fill: black;");
            gridPane.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#FFFFFF"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            roomDescLabel.setStyle("-fx-text-fill: black;");
            roomPane.setStyle("-fx-text-fill: black;");
            gridPane.getChildren().get(0).setStyle("-fx-text-fill: black;");
            gridPane.getChildren().get(2).setStyle("-fx-text-fill: black;");
            gridPane.getChildren().get(4).setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            gridPane.getChildren().get(5).setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            objectsInInventory.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#FFFFFF"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            objectsInRoom.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#FFFFFF"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            inputTextField.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#000000"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            for (int i = 0; i < child.size(); i++){
                if(GridPane.getColumnIndex(child.get(i)) == 1 && GridPane.getRowIndex(child.get(i)) == 1){
                    child.get(i).setStyle("-fx-text-fill: black;");
                    child.get(i).setStyle("-fx-background: #FFFFFF;");
                } else if(GridPane.getColumnIndex(child.get(i)) == 0 && GridPane.getRowIndex(child.get(i)) == 1){
                    child.get(i).setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
                } else if(GridPane.getColumnIndex(child.get(i)) == 2 && GridPane.getRowIndex(child.get(i)) == 1){
                    child.get(i).setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
                } else if (GridPane.getColumnIndex(child.get(i)) == 0 && GridPane.getRowIndex(child.get(i)) == 2) {
                    child.get(i).setStyle("-fx-text-fill: black;");
                    child.get(i).setStyle("-fx-background: #FFFFFF;");
                }
            }
            if (helpToggle){
                instructions.setStyle("-fx-control-inner-background: white;");
            }
            commandLabel.setStyle("-fx-text-fill: black;");
            inputTextField.setStyle("-fx-background-color: black; -fx-text-inner-color: white;");
        } else{
            dark_Mode = true;
            darkMode.setText("LightMode");
            timerLabel.setStyle("-fx-text-fill: white;");
            gridPane.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#000000"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            objectsInInventory.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#000000"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            objectsInRoom.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#000000"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            roomDescLabel.setStyle("-fx-text-fill: white;");
            roomPane.setStyle("-fx-text-fill: white;");
            gridPane.getChildren().get(0).setStyle("-fx-text-fill: white;");
            gridPane.getChildren().get(2).setStyle("-fx-text-fill: white;");
            gridPane.getChildren().get(4).setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            gridPane.getChildren().get(5).setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            inputTextField.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#FFFFFF"),
                    new CornerRadii(0),
                    new Insets(0)
            )));
            for (int i = 0; i < child.size(); i++) {
                if (GridPane.getColumnIndex(child.get(i)) == 1 && GridPane.getRowIndex(child.get(i)) == 1) {
                    child.get(i).setStyle("-fx-text-fill: black;");
                    child.get(i).setStyle("-fx-background: #000000;");
                    child.get(i).getAccessibleText();
                } else if (GridPane.getColumnIndex(child.get(i)) == 0 && GridPane.getRowIndex(child.get(i)) == 1) {
                    child.get(i).setStyle("-fx-background: #000000; -fx-background-color:transparent;");
                } else if (GridPane.getColumnIndex(child.get(i)) == 2 && GridPane.getRowIndex(child.get(i)) == 1) {
                    child.get(i).setStyle("-fx-background: #000000; -fx-background-color:transparent;");
                } else if (GridPane.getColumnIndex(child.get(i)) == 0 && GridPane.getRowIndex(child.get(i)) == 2) {
                    child.get(i).setStyle("-fx-text-fill: white;");
                    child.get(i).setStyle("-fx-background: #000000;");
                }
            }
            if (helpToggle){
                instructions.setStyle("-fx-control-inner-background: black;");
            }
            commandLabel.setStyle("-fx-text-fill: white;");
            inputTextField.setStyle("-fx-text-fill: black;");
            inputTextField.setStyle("-fx-background-color: #FFFFFF;");
        }
    }

    /**
     * This methods handles the event related to the settings button.
     */
    public void addSettingEvent(){
        settings.setOnAction(e -> {
            stopArticulation();
            showSettings();
        });
    }
    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    public void addLeaderboardEvent(){
        leaderBoardButton.setOnAction(e->{
            helpToggle = false;
            settingsToggle = false;
            showLeaderBoard();
        });
    }

    /**
     * This method displays the Leaderboard in the middle cell of the gridpane
     */
    private void showLeaderBoard(){
        gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
        gridPane.add(createboard(),1,1);
    }

    /**
     * This method creates a VBox containing the title LEADERBOARD, a section for the place, name and time for each player in the top 10
     * @return VBox
     */
    private VBox createboard(){
        Label title = new Label("LEADERBOARD");
        title.setAlignment(Pos.TOP_CENTER);
        if(dark_Mode){
            title.setStyle("-fx-text-fill: white;");
        } else{
            title.setStyle("-fx-text-fill: black;");
        }
        if(bigText == true){
            title.setFont(new Font("Arial", 30));
        } else {
            title.setFont(new Font("Arial", 26));
        }

        //Place
        Label pos = new Label("Place");
        pos.setAlignment(Pos.TOP_CENTER);
        if(dark_Mode){
            pos.setStyle("-fx-text-fill: white;");
        } else {
            pos.setStyle("-fx-text-fill: black;");
        }
        if(bigText == true){
            pos.setFont(new Font("Arial", 22));
        } else {
            pos.setFont(new Font("Arial", 15));
        }
        VBox places = new VBox(pos);
        places.setSpacing(10);
        places.setAlignment(Pos.CENTER);
        for(int i = 0; i<leaderboard.getTop_players().size(); i++){
            int j = i+1;
            Label poss = new Label(Integer.toString(j));
            poss.setAlignment(Pos.TOP_CENTER);
            if(dark_Mode){
                poss.setStyle("-fx-text-fill: white;");
            } else {
                poss.setStyle("-fx-text-fill: black;");
            }
            if(bigText == true){
                poss.setFont(new Font("Arial", 21));
            } else {
                poss.setFont(new Font("Arial", 14));
            }
            places.getChildren().add(poss);
        }

        //Username
        Label user = new Label("Username");
        user.setAlignment(Pos.TOP_CENTER);
        if (dark_Mode){
            user.setStyle("-fx-text-fill: white;");
        } else {
            user.setStyle("-fx-text-fill: black;");
        }
        if(bigText == true){
            user.setFont(new Font("Arial", 22));
        } else {
            user.setFont(new Font("Arial", 15));
        }
        VBox usernames = new VBox(user);
        usernames.setSpacing(10);
        usernames.setAlignment(Pos.CENTER);
        for(List<Object> obj : leaderboard.getTop_players()){
            Label name = new Label(obj.get(0).toString());
            name.setAlignment(Pos.TOP_CENTER);
            if(dark_Mode){
                name.setStyle("-fx-text-fill: white;");
            } else {
                name.setStyle("-fx-text-fill: black;");
            }
            if(bigText == true){
                name.setFont(new Font("Arial", 21));
            } else {
                name.setFont(new Font("Arial", 14));
            }
            usernames.getChildren().add(name);
        }

        //Time
        Label time = new Label("Time");
        time.setAlignment(Pos.TOP_CENTER);
        if(dark_Mode){
            time.setStyle("-fx-text-fill: white;");
        } else {
            time.setStyle("-fx-text-fill: black;");
        }
        if(bigText == true){
            time.setFont(new Font("Arial", 22));
        } else {
            time.setFont(new Font("Arial", 15));
        }
        VBox times = new VBox(time);
        times.setSpacing(10);
        times.setAlignment(Pos.CENTER);
        for(List<Object> obj : leaderboard.getTop_players()){
            Label tim = new Label(obj.get(1).toString()+" "+"sec");
            tim.setAlignment(Pos.TOP_CENTER);
            if(dark_Mode){
                tim.setStyle("-fx-text-fill: white;");
            } else {
                tim.setStyle("-fx-text-fill: black;");
            }
            if(bigText == true){
                tim.setFont(new Font("Arial", 21));
            } else {
                tim.setFont(new Font("Arial", 14));
            }
            times.getChildren().add(tim);
        }

        HBox hb = new HBox(places, usernames, times);
        hb.setAlignment(Pos.TOP_CENTER);
        hb.setSpacing(100);

        VBox finall = new VBox(title,hb);
        finall.setAlignment(Pos.TOP_CENTER);
        finall.setSpacing(20);
        return finall;
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }

    /**
     * This method handles the event related to the
     * enlarge text button.
     */
    public void addEnlarge() {
        Enlarge.setOnAction(e -> {
            gridPane.requestFocus();
            if (bigText == false){
                bigText = true;
                changeTextSize();
            }
            else{
                bigText = false;
                changeTextSize();
            }
            //updateScene("");
        });
    }

    /**
     * This method handles the event related to the
     * Dark Mode text button.
     */
    public void setDarkMode(){
        darkMode.setOnAction(e -> {
            DarkMode();
            //updateScene("");
        });
    }

    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
    /**
     * changeTextSize
     * __________________________
     *
     * This method changes the size of the text on the screen either decreases or increases it for accessibility
     * purposes.
     *
     * If bigText is FALSE:
     * -- It calls the method makeButtonSmaller on all the buttons to make the text smaller.
     * -- It reduces the font size of the text on the screen back to the original size.
     *
     * If bigText is TRUE:
     * -- It calls the method makeButtonBigger on all the buttons to make the text bigger.
     * -- It increases the font size of the text on the screen by 6.
     */
    public void changeTextSize(){
        if(bigText == true){
            //bigText = true;
            makeButtonBigger(loadButton);
            makeButtonBigger(helpButton);
            makeButtonBigger(saveButton);
            makeButtonBigger(Enlarge);
            makeButtonBigger(darkMode);
            makeButtonBigger(leftButton);
            makeButtonBigger(midButton);
            makeButtonBigger(rightButton);
            makeButtonBigger(leaderBoardButton);
            inputTextField.setFont(new Font("Arial", 22));
            roomDescLabel.setFont(new Font("Arial", 22));
            instructions.setFont(new Font("Arial", 22));
            objLabel.setFont(new Font("Arial", 22));
            objLabel.setWrapText(true);

            invLabel.setFont(new Font("Arial", 22));

            commandLabel.setFont(new Font("Arial", 22));
        }
        if(bigText == false){
            //bigText = false;
            makeButtonSmaller(loadButton);
            makeButtonSmaller(helpButton);
            makeButtonSmaller(saveButton);
            makeButtonSmaller(Enlarge);
            makeButtonSmaller(darkMode);
            makeButtonSmaller(leftButton);
            makeButtonSmaller(midButton);
            makeButtonSmaller(rightButton);
            makeButtonSmaller(leaderBoardButton);
            instructions.setFont(new Font("Arial", 16));
            inputTextField.setFont(new Font("Arial", 16));
            roomDescLabel.setFont(new Font("Arial", 16));
            objLabel.setFont(new Font("Arial", 16));
            objLabel.setWrapText(true);
            invLabel.setFont(new Font("Arial", 16));

            commandLabel.setFont(new Font("Arial", 16));
        }
    }
    /**
     * makeButtonBigger
     * __________________________
     *
     * Increase the text size within the button for accessibility purposes
     * to make it easier to read/see.
     *
     * @param inputButton the button that the text size is going to be increased.
     */
    private void makeButtonBigger(Button inputButton){
        inputButton.setFont(new Font("Arial", 22));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * makeButtonSmaller
     * __________________________
     *
     * Reduces the text size within the button back to the original size.
     *
     * @param inputButton the button that the text size is going to be reduced.
     */
    private void makeButtonSmaller(Button inputButton){
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * Fight
     * __________________________
     *
     * This method allows the player to fight th DeathTroll when the button on the screen has been pressed. When the
     * attack button is pressed the troll takes damage and depending on the difficulty of the game,the troll can do
     * damage to the player. If the game is easy then a number is randomly generated from 0 to 4 (inclusive) and
     * if the number is even the troll does damage if not the troll does not do any damage. If the game is easy
     * the troll does damage to the player and has the ability to block the attack.
     *
     * @param t This is the DeathTroll object that is going to fight the player
     * @param question This label that displays "Attack" on the screen when the DeathTroll is encountered
     */
    public void Fight(DeathTroll t, Label question){
        Player p = this.model.player;
        AdventureGame mod = this.model;
        boolean val = false;
        p.attack(t);
        //t.defendAttack(p);

        Label title = new Label("DEATH TROLL");
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-text-fill: white;");
        title.setFont(new Font("Arial", 26));

        Label instructions = new Label();
        instructions.setStyle("-fx-text-fill: white;");
        instructions.setFont(new Font("Arial", 16));
        instructions.setWrapText(true);
        VBox title_box = new VBox(title, instructions);
        title_box.setSpacing(10);
        title_box.setAlignment(Pos.TOP_CENTER);

        if (p.difficultyHard){
            val = true;
            t.defendAttack(p);
            t.attack(p);
            updateHearts();
        } else {
            Random rand = new Random();
            int r = rand.nextInt(0,4);
            if(r % 2 == 0){
                val = true;
                t.attack(p);
                updateHearts();
            }
        }
        if (val){
            instructions.setText("You hit the troll and the troll hit you");
        } else{
            instructions.setText("You hit the troll");
        }
        if(t.troll_hp<=0){
            int i =0;
            while(i<mod.player.getCurrentRoom().getMotionTable().getDirection().size()){
                if (mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName()!= null && mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getKeyName().contains("Troll")){
                    break;
                }
                ++i;
            }
            mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).keyName = "";
            objectsInRoom.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setDisable(false);
                }
            });

            objectsInInventory.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setDisable(false);
                }
            });
            submitEvent(mod.player.getCurrentRoom().getMotionTable().getDirection().get(i).getDirection());
        } else if (p.hitpoints<=0) {
            gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
            inputTextField.setDisable(true);

            settings.setDisable(true);


            objectsInRoom.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setDisable(true);
                }
            });

            objectsInInventory.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setDisable(true);
                }
            });
            ImageView lost = new ImageView("Games/TinyGame/room-images/lost.jpg");
            lost.setPreserveRatio(true);
            lost.setFitHeight(400);
            lost.setFitWidth(400);
            VBox lost1 = new VBox(lost);
            lost1.setAlignment(Pos.CENTER);
            gridPane.add(lost1,1,1);

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event2 -> {
                Platform.exit();
            });
            pause.play();
        } else{
            gridPane.getChildren().removeIf(nd -> GridPane.getRowIndex(nd) == 1 && GridPane.getColumnIndex(nd) == 1 && GridPane.getRowIndex(nd) != null && GridPane.getColumnIndex(nd) != null);
            VBox att = new VBox(title_box);
            att.setAlignment(Pos.CENTER);
            gridPane.add(att, 1,1);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event2 -> {
                Deathplaygame(t);
            });
            pause.play();
        }
    }

    public void lbl_tts(String text){
        if (tts_Mode){
            speakText(text);
        }
    }

    /**
     *
     * This method adds text to speech on hover functionality to all buttons displayed on the ui
     * if tts_Mode is false, text to speech is enabled on hover
     * if tts_Mode is true, text to speech is disabled on hover
     *
     */
    public void addTTSEvent() {
        ttsButton.setOnAction(e -> {
            if (!tts_Mode){
                //turned tts on
                helpButton.setOnMouseEntered(f -> speakText(helpButton.getAccessibleText()));
                saveButton.setOnMouseEntered(f -> speakText(saveButton.getAccessibleText()));
                loadButton.setOnMouseEntered(f -> speakText(loadButton.getAccessibleText()));
                Enlarge.setOnMouseEntered(f -> speakText(Enlarge.getAccessibleText()));
                darkMode.setOnMouseEntered(f -> speakText(darkMode.getAccessibleText()));
                ttsButton.setOnMouseEntered(f -> speakText(ttsButton.getAccessibleText()));
                settings.setOnMouseEntered(f -> speakText(settings.getAccessibleText()));
                leaderBoardButton.setOnMouseEntered(f -> speakText(leaderBoardButton.getAccessibleText()));
                for (int i = 0; i < heartImages.getChildren().size(); i++){

                    String temp_text = heartImages.getChildren().get(i).getAccessibleText();
                    heartImages.getChildren().get(i).setOnMouseEntered(f -> speakText(temp_text));

                }
                ttsButton.setText("Disable Text to Speech");
                tts_Mode = true;


            }else{
                //turn tts off
                helpButton.setOnMouseEntered(null);
                saveButton.setOnMouseEntered(null);
                loadButton.setOnMouseEntered(null);
                Enlarge.setOnMouseEntered(null);
                darkMode.setOnMouseEntered(null);
                ttsButton.setOnMouseEntered(null);
                settings.setOnMouseEntered(null);
                leaderBoardButton.setOnMouseEntered(null);
                for (int i = 0; i < heartImages.getChildren().size(); i++){
                    heartImages.getChildren().get(i).setOnMouseEntered(null);
                }
                ttsButton.setText("Text to Speech");
                tts_Mode = false;
            }

        });
    }

    /**
     * speakText
     * ------------
     *
     * this function takes a string and uses a third party library to convert the text into verbal audio
     *
     * @param texttosay this is a string that will be converted into audio
     */
    public void speakText(String texttosay){
        tts text = new tts(texttosay);
    }

}
