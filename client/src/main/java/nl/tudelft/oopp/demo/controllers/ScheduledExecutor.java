package nl.tudelft.oopp.demo.controllers;

import java.util.TimerTask;
import javafx.application.Platform;


/**
 * class to show or update quizzes in real time.
 */
public class ScheduledExecutor extends TimerTask {

    public MainSceneController mainSceneController;
    // without doing this it wouldn't be possible to do this without rewriting most of our code to take into account static

    public ScheduledExecutor(final MainSceneController mainSceneController) {
        this.mainSceneController = mainSceneController;
    }

    /**
     * run the checkquiz.
     */
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
        mainSceneController.checkQuiz();
    }
}
