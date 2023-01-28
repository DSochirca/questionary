package nl.tudelft.oopp.demo.controllers;

import java.util.TimerTask;

/**
 * class for showing the lecture speed coting answers in real time.
 */
public class ScheduledExecutorLecturer extends TimerTask {
    public MainSceneController mainSceneController;
    // without doing this it wouldn't be possible to do this without rewriting most of our code to take into account static

    public ScheduledExecutorLecturer(final MainSceneController mainSceneController) {
        this.mainSceneController = mainSceneController;
    }

    public void run() {
        mainSceneController.refreshVotes();
    }
}
