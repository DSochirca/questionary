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
import nl.tudelft.oopp.demo.data.Question;


public class LectureQuestionCell extends ListCell<MainSceneController.QuestionCell> {
    // Icons to be used ion the list view cells
    ImageView banIcon = new ImageView(new Image("/Icons/BanIcon.PNG"));
    ImageView deleteIcon = new ImageView(new Image("/Icons/DeleteIcon.PNG"));
    ImageView editIcon = new ImageView(new Image("/Icons/EditIcon.PNG"));
    ImageView markAsAnsweredIcon = new ImageView(new Image("/Icons/MarkAsAnsweredIcon.PNG"));

    @FXML
    private TextField nameFieldLecture;
    @FXML
    private TextArea questionAreaLecture;
    @FXML
    private TextField upvotesFieldLecture;
    @FXML
    private Button deleteButtonLecture;
    @FXML
    private Button editButtonLecture;
    @FXML
    private Button markAsAnsweredButtonLecture;
    @FXML
    private Button banButtonLecture;
    @FXML
    private AnchorPane lecturePane;
    @FXML
    private FXMLLoader loader;

    public LectureQuestionCell() {
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
            deleteIcon.setFitHeight(25);
            deleteIcon.setPreserveRatio(true);
            deleteButtonLecture.setGraphic(deleteIcon);
            if (questionCell.getQuestion().isAnswered()) {
                deleteButtonLecture.setVisible(false);
            } else {
                deleteButtonLecture.setVisible(true);
                deleteButtonLecture.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ServerCommunication.deleteQuestionStudent(questionCell.getQuestion().getId(),
                                questionCell.getUser().getId());
                    }
                });
            }

            banIcon.setFitHeight(25);
            banIcon.setPreserveRatio(true);
            banButtonLecture.setGraphic(banIcon);
            banButtonLecture.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Question qstion = questionCell.getQuestion();
                    ServerCommunication.banIpAddress(qstion.getRoomID(), qstion.getCreatorIP());
                }
            });
            if (questionCell.getQuestion().isAnswered()) {
                banButtonLecture.setVisible(false);
            } else {
                banButtonLecture.setVisible(true);
            }

            markAsAnsweredIcon.setFitHeight(25);
            markAsAnsweredIcon.setPreserveRatio(true);
            markAsAnsweredButtonLecture.setGraphic(markAsAnsweredIcon);
            markAsAnsweredButtonLecture.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (questionCell.getQuestion().isAnswered()) {
                        return;
                    }
                    ServerCommunication.markAsAnswered(questionCell.getQuestion().getId());
                }
            });
            if (questionCell.getQuestion().isAnswered()) {
                markAsAnsweredButtonLecture.setVisible(false);
            } else {
                markAsAnsweredButtonLecture.setVisible(true);
            }

            editIcon.setFitHeight(25);
            editIcon.setPreserveRatio(true);
            editButtonLecture.setGraphic(editIcon);
            if (questionCell.getQuestion().isAnswered()) {
                editButtonLecture.setVisible(false);
            } else {
                editButtonLecture.setVisible(true);
                editButtonLecture.setOnAction(new EventHandler<ActionEvent>() {
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
            }


            // loading names, upvotes and questions
            nameFieldLecture.setText(questionCell.getQuestion().getUsername());
            upvotesFieldLecture.setText("" + questionCell.getQuestion().getUpvotes());
            questionAreaLecture.setText(questionCell.getQuestion().getQuestion());
            int rowPref = questionAreaLecture.getPrefRowCount();
            //System.out.println("rowPref = " + rowPref);
            int length = questionAreaLecture.getLength();
            if (length > 52) {
                //System.out.println("length = " + length);
                int increment = (length - 52) / 26 + 1;
                lecturePane.setPrefHeight(80 + increment * 20);
            }


            lecturePane.setPrefWidth(questionCell.getCellSize() - 35);
            setText(null);
            setGraphic(lecturePane);
        }

    }
}
