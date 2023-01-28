package nl.tudelft.oopp.demo.data.cells;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.MainSceneController;

public class LecturerModeQuestionCell extends ListCell<MainSceneController.QuestionCell> {
    // Icons to be used ion the list view cells
    ImageView markAsAnsweredIcon = new ImageView(new Image("/Icons/MarkAsAnsweredIcon.PNG"));

    @FXML
    private TextField nameFieldLecturerMode;
    @FXML
    private TextArea questionAreaLecturerMode;
    @FXML
    private TextField upvotesFieldLecturerMode;
    @FXML
    private Button markAsAnsweredButtonLecturerMode;
    @FXML
    private AnchorPane lecturerModePane;
    @FXML
    private FXMLLoader loader;

    public LecturerModeQuestionCell() {
    }

    @Override
    protected void updateItem(MainSceneController.QuestionCell questionCell, boolean empty) {
        super.updateItem(questionCell, empty);

        if (empty || questionCell == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/ListCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // loading buttons
            markAsAnsweredIcon.setFitHeight(25);
            markAsAnsweredIcon.setPreserveRatio(true);
            markAsAnsweredButtonLecturerMode.setGraphic(markAsAnsweredIcon);
            markAsAnsweredButtonLecturerMode.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (questionCell.getQuestion().isAnswered()) {
                        return;
                    }
                    ServerCommunication.markAsAnswered(questionCell.getQuestion().getId());
                }
            });


            // loading names, upvotes and questions
            nameFieldLecturerMode.setText(questionCell.getQuestion().getUsername());
            upvotesFieldLecturerMode.setText("" + questionCell.getQuestion().getUpvotes());
            questionAreaLecturerMode.setText(questionCell.getQuestion().getQuestion());
            int length = questionAreaLecturerMode.getLength();
            if (length > 60) {
                //System.out.println("length = " + length);
                int increment = (length - 60) / 30 + 1;
                lecturerModePane.setPrefHeight(80 + increment * 20);
            }


            lecturerModePane.setPrefWidth(questionCell.getCellSize() - 35);
            setText(null);
            setGraphic(lecturerModePane);
        }

    }
}
