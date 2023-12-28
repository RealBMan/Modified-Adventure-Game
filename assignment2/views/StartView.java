package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartView {

    private Label difficultyPromptLabel = new Label("Select difficulty");
    private Label usernamePromptLabel = new Label(String.format("Enter username"));
    private TextField saveNameTextField = new TextField("");
    private Button setDifficulty = new Button("Easy");
    private Button startGameButton = new Button("Start Game");
    public Boolean difficultyHard = false;
    final Stage dialog = new Stage();

    private AdventureGameView adventureGameView;

    /**
     * Constructor
     */
    public StartView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        saveNameTextField.setId("SaveFileNameTextField");
        usernamePromptLabel.setStyle("-fx-text-fill: #e8e6e3;");
        usernamePromptLabel.setFont(new Font(16));
        difficultyPromptLabel.setStyle("-fx-text-fill: #e8e6e3;");
        difficultyPromptLabel.setFont(new Font(16));
        saveNameTextField.setStyle("-fx-text-fill: #000000;");
        saveNameTextField.setFont(new Font(16));

        setDifficulty.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        setDifficulty.setPrefSize(200, 50);
        setDifficulty.setFont(new Font(16));
        setDifficulty.setOnAction(e -> setDifficulty());

        startGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        startGameButton.setPrefSize(200, 50);
        startGameButton.setFont(new Font(16));
        startGameButton.setOnAction(e -> closeScreen());

        VBox saveNameBox = new VBox(10, usernamePromptLabel, saveNameTextField, difficultyPromptLabel, setDifficulty, startGameButton);
        saveNameBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(saveNameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * This method allows user to select either easy or hard difficulty.
     * Changes button to display the current choice.
     */
    private void setDifficulty(){
        if(!difficultyHard){
            difficultyHard = true;
            setDifficulty.setText("Hard");
            setDifficulty.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        }
        else{
            difficultyHard = false;
            setDifficulty.setText("Easy");
            setDifficulty.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        }
    }

    /**
     * This method saves the username and difficulty to attributes in the player class.
     * Closes window and begins the game.
     */
    private void closeScreen(){
        String fileName = saveNameTextField.getText().trim();
        if (fileName.isEmpty()){
            usernamePromptLabel.setText("Username required!!");
        }
        else{
            this.adventureGameView.model.getPlayer().username = fileName;
            this.adventureGameView.model.getPlayer().difficultyHard = difficultyHard;
            dialog.close();
        }

    }
}
