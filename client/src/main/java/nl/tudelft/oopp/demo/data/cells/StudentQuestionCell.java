package nl.tudelft.oopp.demo.data.cells;

import java.awt.Dimension;
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

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.MainSceneController;


public class StudentQuestionCell extends ListCell<MainSceneController.QuestionCell> {
    // Icons to be used ion the list view cells
    ImageView deleteIcon = new ImageView(new Image("/Icons/DeleteIcon.PNG"));
    ImageView upvoteIcon = new ImageView(new Image("/Icons/UpvoteIcon.PNG"));
    ImageView editIcon = new ImageView(new Image("/Icons/EditIcon.PNG"));


    @FXML
    private TextField nameFieldStudent;
    @FXML
    private TextArea questionAreaStudent;
    @FXML
    private TextField upvotesFieldStudent;
    @FXML
    private Button upvoteButton;
    @FXML
    private Button deleteButtonStudent;
    @FXML
    private AnchorPane studentPane;
    @FXML
    private FXMLLoader loader;
    @FXML
    private Button editButton;

    public StudentQuestionCell() {
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
            upvoteIcon.setFitHeight(25);
            upvoteIcon.setPreserveRatio(true);
            upvoteButton.setGraphic(upvoteIcon);
            upvoteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ServerCommunication.addUpvote(questionCell.getQuestion().getId(), questionCell.getUser().getId());
                    upvotesFieldStudent.setText("" + questionCell.getQuestion().getUpvotes());
                }
            });
            if (questionCell.getQuestion().isAnswered()) {
                upvoteButton.setVisible(false);
            } else {
                upvoteButton.setVisible(true);
            }

            //I would rather if we could get the ID of a student from the question
            // but making a new endpoint isn't much of a hassle
            editIcon.setFitHeight(25);
            editIcon.setPreserveRatio(true);
            editButton.setGraphic(editIcon);
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    JTextArea textArea = new JTextArea();
                    textArea.setEditable(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.requestFocus();
                    textArea.requestFocusInWindow();
                    scrollPane.setPreferredSize(new Dimension(200, 400));
                    JOptionPane.showMessageDialog(
                            null, scrollPane,
                            "Insert Your Edits", JOptionPane.PLAIN_MESSAGE);
                    String newQuestion = textArea.getText();
                    if (newQuestion.equals("")) {
                        return;
                    }
                    ServerCommunication.editQuestion(questionCell.getQuestion(), newQuestion);
                }
            });
            if (questionCell.getQuestion().isAnswered()) {
                editButton.setVisible(false);
            } else {
                editButton.setVisible(true);
            }

            deleteIcon.setFitHeight(25);
            deleteIcon.setPreserveRatio(true);
            deleteButtonStudent.setGraphic(deleteIcon);
            deleteButtonStudent.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ServerCommunication.deleteQuestionStudent(questionCell.getQuestion().getId(), questionCell.getUser().getId());
                }
            });

            //System.out.println("questionCell.getUser().getId() = " + questionCell.getUser().getId());

            if (questionCell.getUser().getId().equals(ServerCommunication.getIdOfUser(
                    questionCell.getQuestion().getUsername(), questionCell.getQuestion().getCreatorIP()))
                    && !questionCell.getQuestion().isAnswered()) {
                deleteButtonStudent.setVisible(true);
                editButton.setVisible(true);
            } else {
                deleteButtonStudent.setVisible(false);
                editButton.setVisible(false);
            }


            // loading names, upvotes and questions
            nameFieldStudent.setText(questionCell.getQuestion().getUsername());
            upvotesFieldStudent.setText("" + questionCell.getQuestion().getUpvotes());

            questionAreaStudent.setText(questionCell.getQuestion().getQuestion());
            int length = questionAreaStudent.getLength();
            if (length > 60) {
                //System.out.println("length = " + length);
                int increment = (length - 60) / 30 + 1;
                studentPane.setPrefHeight(80 + increment * 20);
            }


            studentPane.setPrefWidth(questionCell.getCellSize() - 35);
            setText(null);
            setGraphic(studentPane);
        }

    }
}
