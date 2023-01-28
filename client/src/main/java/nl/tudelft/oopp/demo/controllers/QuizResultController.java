package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import javafx.fxml.FXML;

import nl.tudelft.oopp.demo.communication.ServerCommunication;



/**
 * class for the quizzes.
 */
public class QuizResultController {
    MainSceneController parentController;
    @FXML
    private javafx.scene.control.TextField resA;
    @FXML
    private javafx.scene.control.TextField resB;
    @FXML
    private javafx.scene.control.TextField resC;
    @FXML
    private javafx.scene.control.TextField resD;
    @FXML
    private javafx.scene.control.TextField resE;
    @FXML
    private javafx.scene.control.TextField resF;
    @FXML
    private javafx.scene.control.TextField resG;
    @FXML
    private javafx.scene.control.TextField resH;
    @FXML
    private javafx.scene.control.TextField resI;
    @FXML
    private javafx.scene.control.TextField resJ;
    @FXML
    private javafx.scene.shape.Rectangle barA;
    @FXML
    private javafx.scene.shape.Rectangle barB;
    @FXML
    private javafx.scene.shape.Rectangle barC;
    @FXML
    private javafx.scene.shape.Rectangle barD;
    @FXML
    private javafx.scene.shape.Rectangle barE;
    @FXML
    private javafx.scene.shape.Rectangle barF;
    @FXML
    private javafx.scene.shape.Rectangle barG;
    @FXML
    private javafx.scene.shape.Rectangle barH;
    @FXML
    private javafx.scene.shape.Rectangle barI;
    @FXML
    private javafx.scene.shape.Rectangle barJ;
    @FXML
    private javafx.scene.control.Button answerA;
    @FXML
    private javafx.scene.control.Button answerB;
    @FXML
    private javafx.scene.control.Button answerC;
    @FXML
    private javafx.scene.control.Button answerD;
    @FXML
    private javafx.scene.control.Button answerE;
    @FXML
    private javafx.scene.control.Button answerF;
    @FXML
    private javafx.scene.control.Button answerG;
    @FXML
    private javafx.scene.control.Button answerH;
    @FXML
    private javafx.scene.control.Button answerI;
    @FXML
    private javafx.scene.control.Button answerJ;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarA;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarB;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarC;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarD;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarE;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarF;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarG;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarH;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarI;
    @FXML
    private javafx.scene.shape.Rectangle secondaryBarJ;

    /**
     * set the results of the quiz.
     *
     * @param results   the results of the quiz
     */
    public void setResults(List<Integer> results) {
        int size = results.size();
        int maxVotes = results.stream().mapToInt(Integer::intValue).sum();
        if (size >= 1) {
            barA.setVisible(true);
            answerA.setVisible(true);
            resA.setVisible(true);
            secondaryBarA.setVisible(true);
            barA.setWidth(132 * results.get(0) / maxVotes);
            resA.setText("A                            " + 100 * results.get(0) / maxVotes + "%");
        }
        if (size >= 2) {
            barB.setVisible(true);
            answerB.setVisible(true);
            resB.setVisible(true);
            secondaryBarB.setVisible(true);
            barB.setWidth(132 * results.get(1) / maxVotes);
            resB.setText("B                            " + 100 * results.get(1) / maxVotes + "%");
        }
        if (size >= 3) {
            barC.setVisible(true);
            answerC.setVisible(true);
            resC.setVisible(true);
            secondaryBarC.setVisible(true);
            barC.setWidth(132 * results.get(2) / maxVotes);
            resC.setText("C                            " + 100 * results.get(2) / maxVotes + "%");
        }
        if (size >= 4) {
            barD.setVisible(true);
            answerD.setVisible(true);
            resD.setVisible(true);
            secondaryBarD.setVisible(true);
            barD.setWidth(132 * results.get(3) / maxVotes);
            resD.setText("D                            " + 100 * results.get(3) / maxVotes + "%");
        }
        if (size >= 5) {
            barE.setVisible(true);
            answerE.setVisible(true);
            resE.setVisible(true);
            secondaryBarE.setVisible(true);
            barE.setWidth(132 * results.get(4) / maxVotes);
            resE.setText("E                            " + 100 * results.get(4) / maxVotes + "%");
        }
        if (size >= 6) {
            barF.setVisible(true);
            answerF.setVisible(true);
            resF.setVisible(true);
            secondaryBarF.setVisible(true);
            barF.setWidth(132 * results.get(5) / maxVotes);
            resF.setText("F                            " + 100 * results.get(5) / maxVotes + "%");
        }
        if (size >= 7) {
            barG.setVisible(true);
            answerG.setVisible(true);
            resG.setVisible(true);
            secondaryBarG.setVisible(true);
            barG.setWidth(132 * results.get(6) / maxVotes);
            resG.setText("G                            " + 100 * results.get(6) / maxVotes + "%");
        }
        if (size >= 8) {
            barH.setVisible(true);
            answerH.setVisible(true);
            resH.setVisible(true);
            secondaryBarH.setVisible(true);
            barH.setWidth(132 * results.get(7) / maxVotes);
            resH.setText("H                            " + 100 * results.get(7) / maxVotes + "%");
        }
        if (size >= 9) {
            barI.setVisible(true);
            answerI.setVisible(true);
            resI.setVisible(true);
            secondaryBarI.setVisible(true);
            barI.setWidth(132 * results.get(8) / maxVotes);
            resI.setText("I                            " + 100 * results.get(8) / maxVotes + "%");
        }
        if (size == 10) {
            barJ.setVisible(true);
            answerJ.setVisible(true);
            resJ.setVisible(true);
            secondaryBarJ.setVisible(true);
            barJ.setWidth(132 * results.get(9) / maxVotes);
            resJ.setText("J                            " + 100 * results.get(9) / maxVotes + "%");
        }

    }

    /**
     * set A as right answer.
     */
    public void answerA() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 1);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set B as right answer.
     */
    public void answerB() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 2);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set C as right answer.
     */
    public void answerC() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 3);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set D as right answer.
     */
    public void answerD() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 4);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set E as right answer.
     */
    public void answerE() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 5);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set F as right answer.
     */
    public void answerF() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 6);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set G as right answer.
     */
    public void answerG() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 7);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set H as right answer.
     */
    public void answerH() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 8);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set I as right answer.
     */
    public void answerI() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 9);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    /**
     * set J as right answer.
     */
    public void answerJ() {
        ServerCommunication.setRightAnswer(parentController.user.getCurrentRoomID(), 10);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);
        answerE.setVisible(false);
        answerF.setVisible(false);
        answerG.setVisible(false);
        answerH.setVisible(false);
        answerI.setVisible(false);
        answerJ.setVisible(false);
    }

    public void setParentController(MainSceneController parentController) {
        this.parentController = parentController;
    }

}
