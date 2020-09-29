package MusicPlayerApplication;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox 
{
    static boolean answer;

    public static boolean display(String title, String message)
    {

/*
prefer instances over static methods, since the latter can lead to 
collisions in certain cases. Make the "answer" an instance variable, 
convert "display" to an instance method, and add a getAnswer() method. 
BTW, there's an Alert dialog object added since JavaFX 8u40 that does 
exactly that
*/

        Stage window = new Stage();

        //Blocks interaction with other windows keeps it at front.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> 
        {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> 
        {
            answer = false;
            window.close();
        });




        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); 


        return answer;
    }
}
