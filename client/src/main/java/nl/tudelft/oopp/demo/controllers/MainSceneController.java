package nl.tudelft.oopp.demo.controllers;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import javafx.stage.Modality;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.data.cells.LectureQuestionCell;
import nl.tudelft.oopp.demo.data.cells.LecturerModeQuestionCell;
import nl.tudelft.oopp.demo.data.cells.StudentQuestionCell;


/**
 * The type Main scene controller.
 */
public class MainSceneController {

    User user = new User();
    Timer timer = new Timer();
    Timer questionsUpdater = new Timer();
    Timer timer2 = new Timer();
    char currentVote;
    int currentAnswer = 0;
    boolean isQuiz;
    //-----------------------------------------
    //-----------------------------------------
    //ANCHOR PANES (different screens)
    @FXML
    private AnchorPane welcomeScreen;
    @FXML
    private AnchorPane joinRoomStudent;
    @FXML
    private AnchorPane joinRoomModerator;
    @FXML
    private AnchorPane roomCreated;
    @FXML
    private AnchorPane questionScreenStudent;
    @FXML
    private AnchorPane questionScreenPrivileged;
    @FXML
    private AnchorPane questionScreenLecturerMode;
    @FXML
    private Button backButton;
    //-----------------------------------------
    //-----------------------------------------
    //WELCOME SCREEN VARIABLES:
    @FXML
    private AnchorPane closedPopup;
    @FXML
    private ToggleButton lecturerRole;
    @FXML
    private ToggleButton moderatorRole;
    @FXML
    private ToggleButton studentRole;
    @FXML
    private TextField nameField;
    @FXML
    private TextField invalidName;
    @FXML
    private AnchorPane helpPage;
    //-----------------------------------------
    //-----------------------------------------
    //HELP PAGE VARIABLES:
    @FXML
    private AnchorPane helpPage1;
    @FXML
    private AnchorPane helpPage2;
    @FXML
    private AnchorPane helpPage3;
    @FXML
    private AnchorPane helpPage4;
    @FXML
    private AnchorPane helpPage5;
    @FXML
    private AnchorPane helpPage6;
    @FXML
    private AnchorPane helpPage7;
    @FXML
    private AnchorPane helpPage8;
    @FXML
    private AnchorPane helpPage9;
    @FXML
    private Label helpPageLabel;
    @FXML
    private Label roleSelectionLabel;
    @FXML
    private Label studentJoinRoomLabel;
    @FXML
    private Label modJoinRoomLabel;
    @FXML
    private Label postQuestionLabel;
    @FXML
    private Label lectureSpeedStudentLabel;
    @FXML
    private Label upvoteLabelStudent;
    @FXML
    private Label deleteLabelStudent;
    @FXML
    private Label editLabelStudent;
    @FXML
    private Label endLectureLabel;
    @FXML
    private Label lecturerModeLabel;
    @FXML
    private Label lectureSpeedLecturerLabel;
    @FXML
    private Label editLabelLecturer;
    @FXML
    private Label markAsAnsweredLabelLecturer;
    @FXML
    private Label deleteLabelLecturer;
    @FXML
    private Label banLabelLecturer;
    @FXML
    private Label quizSliderLabel;
    @FXML
    private Label quizStudentLabel;
    @FXML
    private Label exportLabel;
    @FXML
    private Label dateTimeImageLabel;
    //-----------------------------------------
    //-----------------------------------------
    //STUDENT SCREEN VARIABLES:
    @FXML
    private TextField roomIdStudent;
    @FXML
    private TextField invalidRoomId;
    //-----------------------------------------
    //-----------------------------------------
    //JOIN_ROOM MODERATOR VARIABLES
    @FXML
    private TextField roomIdLecturer;
    @FXML
    private TextField accessTokenLecturer;
    @FXML
    private TextField dateField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField dateError;
    //-----------------------------------------
    //-----------------------------------------
    //ROOM CREATED VARIABLES:
    @FXML
    private TextField roomIdField;
    @FXML
    private TextField accessToken;
    @FXML
    private TextField durationCreated;
    @FXML
    private TextField dateCreated;
    @FXML
    private Button joinRoomDirectly;
    //-----------------------------------------
    //-----------------------------------------
    //QUESTIONS SCREEN VARIABLES:
    @FXML
    public ListView<QuestionCell> questionsList; //questionBox is a custom class, implemented at the bottom
    @FXML
    private ListView<QuestionCell> questionsListPrivileged;
    @FXML
    private ListView<QuestionCell> questionsListLecturerMode;
    @FXML
    private TextArea questionField;
    @FXML
    private Group lecturerAddons;
    @FXML
    private TextField accessTokenPrivileged;
    @FXML
    private TextField roomCodePrivileged;
    @FXML
    private TextField roomCode;
    @FXML
    private ToggleButton answeredToggle; //Toggle buttons for filtering the question feed
    @FXML
    private ToggleButton unansweredToggle;
    @FXML
    private ToggleButton bothToggle;
    @FXML
    private ToggleGroup filterPrivileged;
    @FXML
    private ToggleButton answeredToggleStudent; //Toggle buttons for filtering the question feed for students
    @FXML
    private ToggleButton unansweredToggleStudent;
    @FXML
    private ToggleButton bothToggleStudent;
    @FXML
    private ToggleGroup filterStudent;
    @FXML
    private javafx.scene.shape.Rectangle fastBar;
    @FXML
    private javafx.scene.shape.Rectangle alrightBar;
    @FXML
    private javafx.scene.shape.Rectangle slowBar;
    @FXML
    private Button endLectureButton;
    @FXML
    private ToggleButton fastLectureSpeed;
    @FXML
    private ToggleButton alrightLectureSpeed;
    @FXML
    private ToggleButton slowLectureSpeed;
    @FXML
    private ToggleButton answerAButton;
    @FXML
    private ToggleButton answerBButton;
    @FXML
    private ToggleButton answerCButton;
    @FXML
    private ToggleButton answerDButton;
    @FXML
    private ToggleButton answerEButton;
    @FXML
    private ToggleButton answerFButton;
    @FXML
    private ToggleButton answerGButton;
    @FXML
    private ToggleButton answerHButton;
    @FXML
    private ToggleButton answerIButton;
    @FXML
    private ToggleButton answerJButton;
    @FXML
    private Group quizGroup;
    @FXML
    private Button quizToggle;
    @FXML
    private QuizResultController quizController;
    @FXML
    private TextField answerDisplay;
    @FXML
    private TextField quizSize;
    //-----------------------------------------
    //-----------------------------------------
    //..


    //leave anchor panes at the top

    /**
     * Page back.
     * if the page back button has been pressed:
     */
    public void pageBack() {
        if (joinRoomStudent.isVisible()) {
            joinRoomStudent.setVisible(false);
            welcomeScreen.setVisible(true);
            backButton.setVisible(false);
        }
        if (joinRoomModerator.isVisible()) {
            joinRoomModerator.setVisible(false);
            welcomeScreen.setVisible(true);
            backButton.setVisible(false);
        }
        if (roomCreated.isVisible()) {
            roomCreated.setVisible(false);
            joinRoomModerator.setVisible(true);
            dateError.setVisible(false);
        }

        //highlight current role + disable components (if in the welcomeScreen)
        if (welcomeScreen.isVisible()) {
            invalidName.setVisible(false);
            backButton.setVisible(false);
            highlightRole();
        }
    }

    /**
     * Highlight role.
     */
    // Used when the back button has been pressed, so it highlights the user's already chosen role
    public void highlightRole() {
        String prevRole = user.getUserType();
        if (prevRole == null || prevRole.equals("")) {
            return;
        }

        switch (prevRole) {
            case "lecturer":
                lecturerRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
                break;
            case "moderator":
                moderatorRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
                break;
            case "student":
                studentRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
                break;
            default:
                break;
        }
    }

    //--------------------------------------------
    //--------------------------------------------
    //      FOLLOWING ARE WELCOME SCREEN METHODS:
    //--------------------------------------------
    //--------------------------------------------

    /**
     * Sets role as lecturer.
     */
    public void setRoleAsLecturer() {
        //unhighlight the previous selected role
        String prevRole = user.getUserType();
        unHighlightRole(prevRole);

        //set current role and style
        user.setUserType("lecturer");
        lecturerRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
    }

    /**
     * Sets role as moderator.
     */
    public void setRoleAsModerator() {
        //unhighlight the previous selected role
        String prevRole = user.getUserType();
        unHighlightRole(prevRole);

        //set current role and style
        user.setUserType("moderator");
        moderatorRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
    }

    /**
     * Sets role as student.
     */
    public void setRoleAsStudent() {
        //unhighlight the previous selected role
        String prevRole = user.getUserType();
        unHighlightRole(prevRole);

        //set current role and style
        user.setUserType("student");
        studentRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #22C0FC;");
    }

    /**
     * Un highlight role.
     *
     * @param prevRole the prev role
     */
    //Unhighlight the previous selected role
    public void unHighlightRole(String prevRole) {
        if (prevRole == null || prevRole.equals("")) {
            return;
        }

        switch (prevRole) {
            case "lecturer":
                lecturerRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #F29913;");
                break;
            case "moderator":
                moderatorRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #F29913;");
                break;
            case "student":
                studentRole.setStyle("-fx-background-color: #212121; -fx-text-fill: #F29913;");
                break;
            default:
                break;
        }
    }

    /**
     * Submit pressed.
     */
    //Press submit
    public void submitPressed() {
        String username = nameField.getText();
        if (username == null || username.equals("") || user.getUserType() == null || user.getUserType().equals("")) {
            invalidName.setVisible(true);
            return;
        }
        //if name is valid:
        invalidName.setVisible(false);
        user.setUsername(username);

        welcomeScreen.setVisible(false);
        if (user.getUserType().equals("lecturer") || user.getUserType().equals("moderator")) {
            joinRoomModerator.setVisible(true);
            dateError.setVisible(false);
        }
        if (user.getUserType().equals("student")) {
            joinRoomStudent.setVisible(true);
        }
        backButton.setVisible(true);

        Long userId = ServerCommunication.submitUser(user);
        if (userId != null) {
            user.setId(userId);
        }
        //System.out.println("UserId = " + userId);
    }

    /**
     * Hide room closed popup.
     */
    public void hideClosedPopup() {
        closedPopup.setVisible(false);
    }

    //Variables and imports for the help page
    private boolean isHelpPageInitialized = false;

    ImageView roleSelection = new ImageView(new Image("/HelpPageImages/RoleSelection.PNG"));
    ImageView studentJoinRoom = new ImageView(new Image("/HelpPageImages/studentJoinRoom.PNG"));
    ImageView modJoinRoom = new ImageView(new Image("/HelpPageImages/modJoinRoom.PNG"));
    ImageView dateTimeImage = new ImageView(new Image("/HelpPageImages/dateTimeImage.PNG"));
    ImageView postQuestion = new ImageView(new Image("/HelpPageImages/postQuestion.PNG"));
    ImageView lectureSpeedStudent = new ImageView(new Image("/HelpPageImages/lectureSpeedStudent.PNG"));
    ImageView upvoteIcon = new ImageView(new Image("/Icons/UpvoteIcon.PNG"));
    ImageView deleteIcon = new ImageView(new Image("/Icons/DeleteIcon.PNG"));
    ImageView editIcon = new ImageView(new Image("/Icons/EditIcon.PNG"));
    ImageView endLecture = new ImageView(new Image("/HelpPageImages/endLecture.PNG"));
    ImageView lecturerMode = new ImageView(new Image("/HelpPageImages/lecturerMode.PNG"));
    ImageView lectureSpeedLecturer = new ImageView(new Image("/HelpPageImages/lectureSpeedLecturer.PNG"));
    ImageView deleteIconLecturer = new ImageView(new Image("/Icons/DeleteIcon.PNG"));
    ImageView editIconLecturer = new ImageView(new Image("/Icons/EditIcon.PNG"));
    ImageView markAsAnsweredIcon = new ImageView(new Image("/Icons/MarkAsAnsweredIcon.PNG"));
    ImageView banIcon = new ImageView(new Image("/Icons/BanIcon.PNG"));
    ImageView quizSlider = new ImageView(new Image("/HelpPageImages/quizSlider.PNG"));
    ImageView studentQuiz = new ImageView(new Image("/HelpPageImages/studentQuiz.PNG"));
    ImageView exporting = new ImageView(new Image("/HelpPageImages/exporting.PNG"));

    /**
     * Makes the help page visible/invisible and resets all variables.
     */
    public void makeHelpPageVisible() {
        if (!isHelpPageInitialized) {
            isHelpPageInitialized = true;
            initializeHelpPage();
        }

        helpPage.setVisible(!helpPage.isVisible());
        helpPageLabel.setText("1 / 9");

        helpPage1.setVisible(true);
        helpPage2.setVisible(false);
        helpPage3.setVisible(false);
        helpPage4.setVisible(false);
        helpPage5.setVisible(false);
        helpPage6.setVisible(false);
        helpPage7.setVisible(false);
        helpPage8.setVisible(false);
        helpPage9.setVisible(false);
    }

    /**
     * Assigns the labels in the help page with their respective images.
     */
    private void initializeHelpPage() {
        //Page 1
        roleSelection.setFitHeight(75);
        roleSelection.setPreserveRatio(true);
        roleSelectionLabel.setGraphic(roleSelection);

        //Page 2
        studentJoinRoom.setFitHeight(75);
        studentJoinRoom.setPreserveRatio(true);
        studentJoinRoomLabel.setGraphic(studentJoinRoom);
        modJoinRoom.setFitHeight(60);
        modJoinRoom.setPreserveRatio(true);
        modJoinRoomLabel.setGraphic(modJoinRoom);

        //Page 3
        dateTimeImage.setFitHeight(45);
        dateTimeImage.setPreserveRatio(true);
        dateTimeImageLabel.setGraphic(dateTimeImage);

        //Page 4
        postQuestion.setFitHeight(45);
        postQuestion.setPreserveRatio(true);
        postQuestionLabel.setGraphic(postQuestion);
        lectureSpeedStudent.setFitHeight(100);
        lectureSpeedStudent.setPreserveRatio(true);
        lectureSpeedStudentLabel.setGraphic(lectureSpeedStudent);

        //Page 5
        upvoteIcon.setFitHeight(40);
        upvoteIcon.setPreserveRatio(true);
        upvoteLabelStudent.setGraphic(upvoteIcon);
        deleteIcon.setFitHeight(40);
        deleteIcon.setPreserveRatio(true);
        deleteLabelStudent.setGraphic(deleteIcon);
        editIcon.setFitHeight(40);
        editIcon.setPreserveRatio(true);
        editLabelStudent.setGraphic(editIcon);

        //Page 6
        endLecture.setFitWidth(100);
        endLecture.setPreserveRatio(true);
        endLectureLabel.setGraphic(endLecture);
        lecturerMode.setFitWidth(100);
        lecturerMode.setPreserveRatio(true);
        lecturerModeLabel.setGraphic(lecturerMode);
        lectureSpeedLecturer.setFitHeight(100);
        lectureSpeedLecturer.setPreserveRatio(true);
        lectureSpeedLecturerLabel.setGraphic(lectureSpeedLecturer);

        //Page 7
        deleteIconLecturer.setFitHeight(40);
        deleteIconLecturer.setPreserveRatio(true);
        deleteLabelLecturer.setGraphic(deleteIconLecturer);
        editIconLecturer.setFitHeight(40);
        editIconLecturer.setPreserveRatio(true);
        editLabelLecturer.setGraphic(editIconLecturer);
        markAsAnsweredIcon.setFitHeight(40);
        markAsAnsweredIcon.setPreserveRatio(true);
        markAsAnsweredLabelLecturer.setGraphic(markAsAnsweredIcon);
        banIcon.setFitHeight(40);
        banIcon.setPreserveRatio(true);
        banLabelLecturer.setGraphic(banIcon);

        //Page 8
        quizSlider.setFitHeight(60);
        quizSlider.setPreserveRatio(true);
        quizSliderLabel.setGraphic(quizSlider);
        studentQuiz.setFitHeight(100);
        studentQuiz.setPreserveRatio(true);
        quizStudentLabel.setGraphic(studentQuiz);

        //Page 9
        exporting.setFitHeight(200);
        exporting.setPreserveRatio(true);
        exportLabel.setGraphic(exporting);
    }

    /**
     * Advances to the next help page in the help screen.
     */
    public void advanceForwards() {
        if (helpPage1.isVisible()) {
            helpPageLabel.setText("2 / 9");
            helpPage1.setVisible(false);
            helpPage2.setVisible(true);
        } else if (helpPage2.isVisible()) {
            helpPageLabel.setText("3 / 9");
            helpPage2.setVisible(false);
            helpPage3.setVisible(true);
        } else if (helpPage3.isVisible()) {
            helpPageLabel.setText("4 / 9");
            helpPage3.setVisible(false);
            helpPage4.setVisible(true);
        } else if (helpPage4.isVisible()) {
            helpPageLabel.setText("5 / 9");
            helpPage4.setVisible(false);
            helpPage5.setVisible(true);
        } else if (helpPage5.isVisible()) {
            helpPageLabel.setText("6 / 9");
            helpPage5.setVisible(false);
            helpPage6.setVisible(true);
        } else if (helpPage6.isVisible()) {
            helpPageLabel.setText("7 / 9");
            helpPage6.setVisible(false);
            helpPage7.setVisible(true);
        } else if (helpPage7.isVisible()) {
            helpPageLabel.setText("8 / 9");
            helpPage7.setVisible(false);
            helpPage8.setVisible(true);
        } else if (helpPage8.isVisible()) {
            helpPageLabel.setText("9 / 9");
            helpPage8.setVisible(false);
            helpPage9.setVisible(true);
        } else if (helpPage9.isVisible()) {
            return;
        }
    }

    /**
     * Returns to the previous help page in the help screen.
     */
    public void advanceBackwards() {
        if (helpPage1.isVisible()) {
            return;
        } else if (helpPage2.isVisible()) {
            helpPageLabel.setText("1 / 9");
            helpPage1.setVisible(true);
            helpPage2.setVisible(false);
        } else if (helpPage3.isVisible()) {
            helpPageLabel.setText("2 / 9");
            helpPage2.setVisible(true);
            helpPage3.setVisible(false);
        } else if (helpPage4.isVisible()) {
            helpPageLabel.setText("3 / 9");
            helpPage3.setVisible(true);
            helpPage4.setVisible(false);
        } else if (helpPage5.isVisible()) {
            helpPageLabel.setText("4 / 9");
            helpPage4.setVisible(true);
            helpPage5.setVisible(false);
        } else if (helpPage6.isVisible()) {
            helpPageLabel.setText("5 / 9");
            helpPage5.setVisible(true);
            helpPage6.setVisible(false);
        } else if (helpPage7.isVisible()) {
            helpPageLabel.setText("6 / 9");
            helpPage6.setVisible(true);
            helpPage7.setVisible(false);
        } else if (helpPage8.isVisible()) {
            helpPageLabel.setText("7 / 9");
            helpPage7.setVisible(true);
            helpPage8.setVisible(false);
        } else if (helpPage9.isVisible()) {
            helpPageLabel.setText("8 / 9");
            helpPage8.setVisible(true);
            helpPage9.setVisible(false);
        }
    }

    //--------------------------------------------
    //--------------------------------------------
    //  FOLLOWING ARE STUDENT SCREEN METHODS:
    //--------------------------------------------
    //--------------------------------------------

    /**
     * Join room clicked.
     */
    public void joinRoomClicked() {
        String id = roomIdStudent.getText();
        if (id == null || id.equals("")) {
            invalidRoomId.setVisible(true);
            return;
        }

        //otherwise join room: (assuming room exists and it's joinable)
        String errorMsg = ServerCommunication.joinRoom(user.getId(), id);
        if (!errorMsg.equals("success")) {     //room id is invalid
            invalidRoomId.setVisible(true);
            invalidRoomId.setText(errorMsg);
            return;
        }

        user.setCurrentRoomID(id);

        roomCode.setText(id);
        invalidRoomId.setVisible(false);
        questionScreenStudent.setVisible(true);
        joinRoomStudent.setVisible(false);
        backButton.setVisible(false);
        listQuestionsClicked();
        timer = new Timer();
        questionsUpdater = new Timer();
        timer.schedule(new ScheduledExecutor(this), 0, 1000);
        questionsUpdater.schedule(new QuestionRefresh(this), 0, 1000);
        if (ServerCommunication.getQuizStatus(user.getCurrentRoomID())) {
            isQuiz = true;
            quizGroup.setVisible(true);
        }
    }


    //--------------------------------------------
    //--------------------------------------------
    //  FOLLOWING ARE MODERATOR/LECTURER ROOM CREATED METHODS:
    //--------------------------------------------
    //--------------------------------------------

    /**
     * Copy room code to clipboard.
     */
    public void copyRoomCodeToClipboard() {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(roomIdField.getText()),
                        null);
    }

    /**
     * Copy access token to clipboard.
     */
    public void copyAccessTokenToClipboard() {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(accessToken.getText()),
                        null);
    }

    /**
     * Join room directly.
     */
    public void joinRoomDirectly() {
        String id = roomIdField.getText();
        String token = accessToken.getText();

        //if the button exists then the lecture exists and it is joinable now
        ServerCommunication.modJoinRoom(id, token, user);
        user.setCurrentRoomID(id);

        //if the user joining is a lecturer then the lecturer mode button and the speed poll should be visible
        if (user.getUserType().equals("lecturer")) {
            lecturerAddons.setVisible(true);
            timer = new Timer();
            questionsUpdater = new Timer();
            timer2 = new Timer();
            timer.schedule(new ScheduledExecutorLecturer(this), 0, 1000);
            questionsUpdater.schedule(new QuestionRefresh(this), 0, 1000);
            timer2.schedule(new ScheduledExecutor(this), 0, 1000);
            fastBar.setWidth(0);
            alrightBar.setWidth(0);
            slowBar.setWidth(0);
            if (ServerCommunication.getQuizStatus(user.getCurrentRoomID())) {
                quizToggle.setText("End Quiz");
            }

        } else {
            lecturerAddons.setVisible(false);
            questionsUpdater = new Timer();
            timer2 = new Timer();
            questionsUpdater.schedule(new QuestionRefresh(this), 0, 1000);
            timer2.schedule(new ScheduledExecutor(this), 0, 1000);
            if (ServerCommunication.getQuizStatus(user.getCurrentRoomID())) {
                quizToggle.setText("End Quiz");
            }
        }
        //adding the room code and access token to the screen
        roomCodePrivileged.setText(id);
        accessTokenPrivileged.setText(token);

        questionScreenPrivileged.setVisible(true);
        roomCreated.setVisible(false);
        backButton.setVisible(false);
    }


    //--------------------------------------------
    //--------------------------------------------
    //  FOLLOWING ARE JOIN_ROOM_MODERATOR METHODS:
    //--------------------------------------------
    //--------------------------------------------

    /**
     * Create room clicked.
     */
    public void createRoomClicked() {
        LocalDate date = null;
        LocalTime time = null;

        //if date field isn't null:
        try {
            if (!dateField.getText().equals("")) {
                date = LocalDate.parse(dateField.getText());
            } //else date = LocalDate.now();
        } catch (Exception e) {
            dateError.setVisible(true);
            dateError.setText("wrong date format should be yyyy-mm-dd");
            return;
        }

        try {
            //if duration isn't null:
            if (!durationField.getText().equals("")) {
                time = LocalTime.parse(durationField.getText());
            } //else time = LocalTime.now();
        } catch (Exception e) {
            dateError.setVisible(true);
            dateError.setText("wrong time format should be hh:mm:ss");
            return;
        }
        if (dateField.getText().equals("") || durationField.getText().equals("")) { //either is null
            date = null;
            time = null;
        }

        roomCreated.setVisible(true);
        joinRoomModerator.setVisible(false);

        //get the generated codes from server
        String idtoken = ServerCommunication.generateCodes();
        //parse the idtoken String into 2 different Strings
        Scanner sc = new Scanner(idtoken);
        sc.useDelimiter("-");
        String id = sc.next();
        String token = sc.next();
        //System.out.println(id);
        //System.out.println(token);

        //set the codes in the fields
        roomIdField.setText(id);
        accessToken.setText(token);


        LocalDateTime dateTime = null;

        //set date and time the room is scheduled at
        if (date == null || time == null) {
            dateCreated.setText("when first joined");
            durationCreated.setText("");
        } else {
            dateTime = LocalDateTime.of(date, time);
            dateCreated.setText(date.toString());
            durationCreated.setText(time.toString());
        }

        //set the join room directly button to not visible if the scheduled room is not for now
        joinRoomDirectly.setVisible(dateTime == null || dateTime.isBefore(LocalDateTime.now()));

        ServerCommunication.createRoom(idtoken, dateTime, user);
    }

    /**
     * Join room as mod.
     */
    public void joinRoomAsMod() {
        String id = roomIdLecturer.getText();
        String token = accessTokenLecturer.getText();

        if (id == null || id.equals("") || token == null || token.equals("")) {
            //to add a new text field to inform the user they need to enter correct codes
            return;
        }


        //otherwise join room: (assuming room exists and it's joinable)
        if (!ServerCommunication.modJoinRoom(id, token, user)) {
            //to add a new text field to inform the user they need to enter correct codes
            return;
        } //then it's not joinable
        user.setCurrentRoomID(id);

        //if the user joining is a lecturer then the lecturer mode button and the speed poll should be visible
        if (user.getUserType().equals("lecturer")) {
            lecturerAddons.setVisible(true);
            endLectureButton.setVisible(true);
            timer = new Timer();
            questionsUpdater = new Timer();
            timer2 = new Timer();
            timer.schedule(new ScheduledExecutorLecturer(this), 0, 1000);
            questionsUpdater.schedule(new QuestionRefresh(this), 0, 1000);
            timer2.schedule(new ScheduledExecutor(this), 0, 1000);
            fastBar.setWidth(0);
            alrightBar.setWidth(0);
            slowBar.setWidth(0);
            if (ServerCommunication.getQuizStatus(user.getCurrentRoomID())) {
                quizToggle.setText("End Quiz");
            }

        } else {
            lecturerAddons.setVisible(false);
            endLectureButton.setVisible(false);
            questionsUpdater = new Timer();
            timer2 = new Timer();
            questionsUpdater.schedule(new QuestionRefresh(this), 0, 1000);
            timer2.schedule(new ScheduledExecutor(this), 0, 1000);
            if (ServerCommunication.getQuizStatus(user.getCurrentRoomID())) {
                quizToggle.setText("End Quiz");
            }
        }

        //adding the room code and access token to the screen
        roomCodePrivileged.setText(id);
        accessTokenPrivileged.setText(token);

        questionScreenPrivileged.setVisible(true);
        joinRoomModerator.setVisible(false);
        backButton.setVisible(false);


    }

    //--------------------------------------------
    //--------------------------------------------
    //  FOLLOWING ARE QUESTIONS SCREEN METHODS:
    //--------------------------------------------
    //--------------------------------------------

    /**
     * Upvoted.
     *
     * @param q the q
     */
    public void upvoted(Question q) {
        ServerCommunication.addUpvote(q.getId(), user.getId());
        listQuestionsClicked();
    }

    /**
     * Add question clicked.
     */
    public void addQuestionClicked() {
        //if invalid  - THIS CAN MAKE VISIBLE A FIELD WHICH STATES THAT QUESTION FIELD IS EMPTY - should be implemented
        String question = questionField.getText();
        questionField.setText("");
        if (question.equals("")) {
            return;
        }

        String id = user.getCurrentRoomID();
        Question q = new Question(id, user.getUsername(), question);

        String s = ServerCommunication.addQuestion(q);
        if (s.equals("banned")) {
            questionField.setText("You are banned (your ip as well) and no longer can ask questions here. "
                    + "Also all of your questions were removed from this lecture room.");
            return;
        }
        if (s.equals("closed")) {
            exitRoom();
            closedPopup.setVisible(true);
        }

        listQuestionsClicked(); //list questions after adding a new one
    }


    /**
     * The type Question cell.
     * Used for passing information about questions to the question cells inhabiting the list views
     */
    public class QuestionCell {
        private final Question question;
        private final double cellSize;
        private final User user;

        /**
         * Constructor for object QuestionCell.
         *
         * @param question the question object
         * @param size     the size of the list view that should be populated
         */
        public QuestionCell(Question question, double size, User user) {
            this.question = question;
            this.cellSize = size;
            this.user = user; //for the methods which require a User (eg. adding upvotes)
        }

        public Question getQuestion() {
            return question;
        }

        public User getUser() {
            return user;
        }

        public double getCellSize() {
            return cellSize;
        }

    }

    /**
     * List questions clicked.
     */
    public void listQuestionsClicked() {
        String id = user.getCurrentRoomID();

        List<QuestionCell> finalResult = new ArrayList<>();
        List<Question> result = ServerCommunication.getQuestions(id);

        String questionFilter;
        if (questionScreenLecturerMode.isVisible()) {
            questionFilter = "Unanswered";
        } else if (questionScreenStudent.isVisible()) {
            ToggleButton toggled = (ToggleButton) filterStudent.getSelectedToggle();
            questionFilter = toggled.getText();
        } else if (questionScreenPrivileged.isVisible()) {
            ToggleButton toggled = (ToggleButton) filterPrivileged.getSelectedToggle();
            questionFilter = toggled.getText();
        } else { // just in case
            questionFilter = "Both";
        }

        if (questionFilter.equals("Answered")) {
            for (Question q : result) {
                if (q != null && q.isAnswered()) {
                    finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
                }
            }
        } else if (questionFilter.equals("Unanswered")) {
            for (Question q : result) {
                if (q != null && !q.isAnswered()) {
                    finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
                }
            }
        } else {
            for (Question q : result) {
                finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
            }
        }

        if (finalResult.size() < 1) {
            questionsList.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                    + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
            questionsListPrivileged.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                    + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
            questionsListLecturerMode.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                    + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
        } else {
            questionsList.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
            questionsListPrivileged.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
            questionsListLecturerMode.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
        }

        if (user.getUserType().equals("student")) {
            questionsList.getItems().clear();
            ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
            questionsList.setItems(questions);
            questionsList.setCellFactory(studentCell -> new StudentQuestionCell());
        } else if (questionScreenPrivileged.isVisible()) {
            questionsListPrivileged.getItems().clear();
            ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
            questionsListPrivileged.setItems(questions);
            questionsListPrivileged.setCellFactory(lectureCell -> new LectureQuestionCell());
        } else if (user.getUserType().equals("lecturer") && questionScreenLecturerMode.isVisible()) {
            questionsListLecturerMode.getItems().clear();
            ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
            questionsListLecturerMode.setItems(questions);
            questionsListLecturerMode.setCellFactory(lecturerModeCell -> new LecturerModeQuestionCell());
        }

    }

    /**
     * Exit room.
     */
    public void exitRoom() {
        ServerCommunication.leaveRoom(user.getId());
        user.setCurrentRoomID(null);

        questionScreenStudent.setVisible(false);
        questionScreenPrivileged.setVisible(false);
        if (user.getUserType().equals("lecturer")) {
            timer.cancel();
            timer.purge();
            timer2.cancel();
            timer2.purge();
            questionsUpdater.cancel();
            questionsUpdater.purge();
        } else if (user.getUserType().equals("student")) {
            timer.cancel();
            timer.purge();
            questionsUpdater.cancel();
            questionsUpdater.purge();
        } else {
            timer2.cancel();
            timer2.purge();
            questionsUpdater.cancel();
            questionsUpdater.purge();
        }
        if (user.getUserType().equals("lecturer") || user.getUserType().equals("moderator")) {
            ObservableList<QuestionCell> questions = questionsListPrivileged.getItems();
            questions.clear();
            questionsListPrivileged.setItems(questions);
        }
        joinRoomModerator.setVisible(false);
        joinRoomStudent.setVisible(false);
        welcomeScreen.setVisible(true);
    }


    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    //  FOLLOWING ARE QUESTIONS SCREEN METHODS FOR MODERATOR AND LECTURER ONLY:
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------

    private String lastfilter = ""; //variable to check what filter was last set

    /**
     * Sets filter on answered.
     */
    public void setFilterOnAnswered() {
        if (!lastfilter.equals("Answered")) {
            if (questionScreenStudent.isVisible()) {
                highlightToggle(answeredToggleStudent);
            } else if (questionScreenPrivileged.isVisible()) {
                highlightToggle(answeredToggle);
            }

            lastfilter = "Answered";
            listQuestionsClicked();
        }
    }

    /**
     * Sets filter on unanswered.
     */
    public void setFilterOnUnanswered() {
        if (!lastfilter.equals("Unanswered")) {
            if (questionScreenStudent.isVisible()) {
                highlightToggle(unansweredToggleStudent);
            } else if (questionScreenPrivileged.isVisible()) {
                highlightToggle(unansweredToggle);
            }

            lastfilter = "Unanswered";
            listQuestionsClicked();
        }
    }

    /**
     * Sets filter on both.
     */
    public void setFilterOnBoth() {
        if (!lastfilter.equals("Both")) {
            if (questionScreenStudent.isVisible()) {
                highlightToggle(bothToggleStudent);
            } else if (questionScreenPrivileged.isVisible()) {
                highlightToggle(bothToggle);
            }

            lastfilter = "Both";
            listQuestionsClicked();
        }
    }

    /**
     * Highlight toggle.
     *
     * @param button the button
     */
    public void highlightToggle(ToggleButton button) {
        if (questionScreenStudent.isVisible()) {
            answeredToggleStudent.setTextFill(Paint.valueOf("#ffffff"));
            unansweredToggleStudent.setTextFill(Paint.valueOf("#ffffff"));
            bothToggleStudent.setTextFill(Paint.valueOf("#ffffff"));
            button.setTextFill(Paint.valueOf("#22C0FC"));
        } else if (questionScreenPrivileged.isVisible()) {
            answeredToggle.setTextFill(Paint.valueOf("#ffffff"));
            unansweredToggle.setTextFill(Paint.valueOf("#ffffff"));
            bothToggle.setTextFill(Paint.valueOf("#ffffff"));
            button.setTextFill(Paint.valueOf("#22C0FC"));
        }
    }

    /**
     * Enter lecturer mode.
     */
    public void enterLecturerMode() {
        questionScreenPrivileged.setVisible(false);
        questionScreenLecturerMode.setVisible(true);

        listQuestionsClicked();
    }

    /**
     * Exit lecturer mode.
     */
    public void exitLecturerMode() {
        questionScreenPrivileged.setVisible(true);
        questionScreenLecturerMode.setVisible(false);

        listQuestionsClicked();
    }

    /**
     * Open close votes.
     */
    public void refreshVotes() {
        if ((welcomeScreen.isVisible() || joinRoomStudent.isVisible() || joinRoomModerator.isVisible()
                || roomCreated.isVisible())) {
            return;
        }
        List<Integer> votes = ServerCommunication.getSpeedLecture(user.getCurrentRoomID());
        if (votes == null) {
            return;
        }
        double fastVotes = votes.get(0);  //to change into input from database
        double alrightVotes = votes.get(1);
        double slowVotes = votes.get(2);
        double maxVotes = fastVotes + alrightVotes + slowVotes;

        fastBar.setWidth(132 * fastVotes / maxVotes);
        alrightBar.setWidth(132 * alrightVotes / maxVotes);
        slowBar.setWidth(132 * slowVotes / maxVotes);

    }

    /**
     * Fast clicked.
     */
    public void fastClicked() {
        if (!Character.isLetter(currentVote)) {
            currentVote = 'f';
        } else {
            ServerCommunication.removeSpeedVote(user.getCurrentRoomID(), currentVote);
        }
        ServerCommunication.addSpeedVote(user.getCurrentRoomID(), 'f');
        currentVote = 'f';
        slowLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff;");
        alrightLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        fastLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #22c0fc");
    }

    /**
     * Alright clicked.
     */
    public void alrightClicked() {
        if (!Character.isLetter(currentVote)) {
            currentVote = 'a';
        } else {
            ServerCommunication.removeSpeedVote(user.getCurrentRoomID(), currentVote);
        }
        ServerCommunication.addSpeedVote(user.getCurrentRoomID(), 'a');
        currentVote = 'a';
        slowLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff;");
        fastLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        alrightLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #22c0fc");
    }

    /**
     * Slow clicked.
     */
    public void slowClicked() {
        if (!Character.isLetter(currentVote)) {
            currentVote = 's';
        } else {
            ServerCommunication.removeSpeedVote(user.getCurrentRoomID(), currentVote);
        }
        ServerCommunication.addSpeedVote(user.getCurrentRoomID(), 's');
        currentVote = 's';
        fastLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        alrightLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        slowLectureSpeed.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc");
        //System.out.println(currentVote);
    }

    /**
     * answer A clicked.
     */
    public void answerAClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 1;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 1);
        currentAnswer = 1;
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");

    }

    /**
     * answer B clicked.
     */
    public void answerBClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 2;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 2);
        currentAnswer = 2;
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #22c0fc;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");

    }

    /**
     * answer C clicked.
     */
    public void answerCClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 3;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 3);
        currentAnswer = 3;
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #22c0fc;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");

    }

    /**
     * answer D clicked.
     */
    public void answerDClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 4;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 4);
        currentAnswer = 4;
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");

    }

    /**
     * answer E clicked.
     */
    public void answerEClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 5;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 5);
        currentAnswer = 5;
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * answer F clicked.
     */
    public void answerFClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 6;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 6);
        currentAnswer = 6;
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * answer G clicked.
     */
    public void answerGClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 7;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 7);
        currentAnswer = 7;
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * answer H clicked.
     */
    public void answerHClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 8;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 8);
        currentAnswer = 8;
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * answer I clicked.
     */
    public void answerIClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 9;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 9);
        currentAnswer = 9;
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * answer J clicked.
     */
    public void answerJClicked() {
        if (!isQuiz) {
            return;
        }
        if (currentAnswer == 0) {
            currentAnswer = 10;
        } else {
            ServerCommunication.removeQuizAnswer(user.getCurrentRoomID(), currentAnswer);
        }
        ServerCommunication.addQuizAnswer(user.getCurrentRoomID(), 10);
        currentAnswer = 10;
        answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #22c0fc; -fx-border-color: #0c7bc0;");
        answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
        answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
        answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
    }

    /**
     * check if a quiz is made by a lecturer.
     */
    public void checkQuiz() {
        if ((welcomeScreen.isVisible() || joinRoomStudent.isVisible() || joinRoomModerator.isVisible()
                || roomCreated.isVisible())) {
            return;
        }
        List<Object> response = ServerCommunication.checkQuiz(user.getCurrentRoomID());
        if (response.size() == 0) {
            return;
        }
        boolean quiz = (boolean) response.get(0);
        if (user.getUserType().equals("student")) {
            if (quiz) {
                if (!isQuiz) {
                    isQuiz = true;
                    int maxSize = (int) response.get(1);
                    //System.out.println(maxSize);
                    if (maxSize >= 1) {
                        answerAButton.setVisible(true);
                    }
                    if (maxSize >= 2) {
                        answerBButton.setVisible(true);
                    }
                    if (maxSize >= 3) {
                        answerCButton.setVisible(true);
                    }
                    if (maxSize >= 4) {
                        answerDButton.setVisible(true);
                    }
                    if (maxSize >= 5) {
                        answerEButton.setVisible(true);
                    }
                    if (maxSize >= 6) {
                        answerFButton.setVisible(true);
                    }
                    if (maxSize >= 7) {
                        answerGButton.setVisible(true);
                    }
                    if (maxSize >= 8) {
                        answerHButton.setVisible(true);
                    }
                    if (maxSize >= 9) {
                        answerIButton.setVisible(true);
                    }
                    if (maxSize == 10) {
                        answerJButton.setVisible(true);
                    }
                }
            } else {
                if (isQuiz) {

                    answerAButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
                    answerBButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
                    answerCButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
                    answerDButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
                    answerEButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
                    answerFButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
                    answerGButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
                    answerHButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");
                    answerIButton.setStyle("-fx-background-color: #2B2B2B; -fx-border-color: #0c7bc0; -fx-text-fill: #ffffff;");
                    answerJButton.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #ffffff; -fx-border-color: #0c7bc0;");

                    isQuiz = false;

                    answerAButton.setVisible(false);
                    answerBButton.setVisible(false);
                    answerCButton.setVisible(false);
                    answerDButton.setVisible(false);
                    answerEButton.setVisible(false);
                    answerFButton.setVisible(false);
                    answerGButton.setVisible(false);
                    answerHButton.setVisible(false);
                    answerIButton.setVisible(false);
                    answerJButton.setVisible(false);

                    int temp = currentAnswer;

                    currentAnswer = 0;

                    boolean ans = ServerCommunication.checkAnswerCorrect(user.getCurrentRoomID(), temp);
                    if (ans) {
                        answerDisplay.setVisible(true);
                        answerDisplay.setText("Your answer was correct!");
                    } else {
                        answerDisplay.setVisible(true);
                        answerDisplay.setText("Your answer was incorrect!");
                    }
                    ForkJoinPool.commonPool().submit(() -> {
                        try {
                            TimeUnit.SECONDS.sleep(30);
                            answerDisplay.setVisible(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else {
            if (quiz) {
                Platform.runLater(() -> {
                    quizToggle.setText("End Quiz");
                });
            } else {
                Platform.runLater(() -> {
                    quizToggle.setText("Start Quiz");
                });
            }
        }
    }

    /**
     * update questions list to show questions in real time.
     */
    public void asyncCheckQuestions() {
        if ((welcomeScreen.isVisible() || joinRoomStudent.isVisible() || joinRoomModerator.isVisible()
                || roomCreated.isVisible())) {
            return;
        }
        String id = user.getCurrentRoomID();

        List<QuestionCell> finalResult = new ArrayList<>();
        List<Question> result = ServerCommunication.updateQuestions(id);
        if (result == null || result.size() == 0) {
            return;
        }

        String questionFilter;
        if (questionScreenLecturerMode.isVisible()) {
            questionFilter = "Unanswered";
        } else if (questionScreenStudent.isVisible()) {
            ToggleButton toggled = (ToggleButton) filterStudent.getSelectedToggle();
            questionFilter = toggled.getText();
        } else if (questionScreenPrivileged.isVisible()) {
            ToggleButton toggled = (ToggleButton) filterPrivileged.getSelectedToggle();
            questionFilter = toggled.getText();
        } else { // just in case
            questionFilter = "Both";
        }

        if (questionFilter.equals("Answered")) {
            for (Question q : result) {
                if (q != null && q.isAnswered()) {
                    finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
                }
            }
        } else if (questionFilter.equals("Unanswered")) {
            for (Question q : result) {
                if (q != null && !q.isAnswered()) {
                    finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
                }
            }
        } else {
            for (Question q : result) {
                finalResult.add(new QuestionCell(q, questionsList.getWidth(), user));
            }
        }
        //Need to update the GUI within an FX thread
        Platform.runLater(() -> {
            if (finalResult.size() < 1) {
                questionsList.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                        + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
                questionsListPrivileged.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                        + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
                questionsListLecturerMode.setStyle("-fx-border-color: #0c7bc0; -fx-background-color: #2B2B2B;"
                        + "-fx-background-radius: 20px; -fx-border-radius: 20px;");
            } else {
                questionsList.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
                questionsListPrivileged.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
                questionsListLecturerMode.setStyle("-fx-border-color: #212121; -fx-background-color: #212121");
            }

            if (user.getUserType().equals("student")) {
                questionsList.getItems().clear();
                ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
                questionsList.setItems(questions);
                questionsList.setCellFactory(studentCell -> new StudentQuestionCell());
            } else if (questionScreenPrivileged.isVisible()) {
                questionsListPrivileged.getItems().clear();
                ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
                questionsListPrivileged.setItems(questions);
                questionsListPrivileged.setCellFactory(lectureCell -> new LectureQuestionCell());
            } else if (user.getUserType().equals("lecturer") && questionScreenLecturerMode.isVisible()) {
                questionsListLecturerMode.getItems().clear();
                ObservableList<QuestionCell> questions = FXCollections.observableList(finalResult);
                questionsListLecturerMode.setItems(questions);
                questionsListLecturerMode.setCellFactory(lecturerModeCell -> new LecturerModeQuestionCell());
            }
        });
    }

    /**
     * start or end quiz.
     */
    public void toggleQuiz() {
        boolean current = ServerCommunication.getQuizStatus(user.getCurrentRoomID());
        if (current) {
            List<Integer> answers = ServerCommunication.getQuizAnswers(user.getCurrentRoomID());
            ServerCommunication.toggleQuiz(user.getCurrentRoomID(), 0);
            quizToggle.setText("Start Quiz");
            //System.out.println(answers);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuizResult.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("Quiz Results");
                stage.setScene(new Scene(root1));
                stage.show();
                QuizResultController controller = fxmlLoader.getController();
                controller.setParentController(this);
                controller.setResults(answers);
                //quizController.setResults(answers);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // represent the current votes in a way
        } else {
            ServerCommunication.toggleQuiz(user.getCurrentRoomID(),
                    Integer.parseInt(quizSize.getText().substring(0, quizSize.getText().length() - 2)));
            ServerCommunication.setRightAnswer(user.getCurrentRoomID(), 'f');
            quizToggle.setText("End Quiz");
        }
    }


    /**
     * export and write to file.
     */
    public void exportEverything() {
        try {
            FileDialog fileDialog = new FileDialog(new Frame(), "Save", FileDialog.SAVE);
            fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
            String str = user.getCurrentRoomID() + ".txt";
            fileDialog.setFile(str);
            fileDialog.setVisible(true);
            //System.out.println("File: " + fileDialog.getFile());
            //System.out.println(fileDialog.getDirectory());
            if (!fileDialog.getFile().contains(".txt")) {
                fileDialog.setFile(fileDialog.getFile() + ".txt");
            }
            PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter(fileDialog.getDirectory()
                    + fileDialog.getFile())));

            String underline = "----------------------------------------------------"
                    + "---------------------------------------------------------------------------------------";

            List<Question> questionList = ServerCommunication.export(user.getCurrentRoomID());
            for (Question q : questionList) {
                String[] strings = q.getQuestion().split(" ");
                int p = 1;
                String question = strings[0];
                for (int i = 1; i < strings.length; i++) {
                    if (question.length() > (underline.length() * p) - (50)) {
                        question = question + "\n";
                        p++;
                    } else {
                        question = question + " " + strings[i];
                    }
                }
                wr.println("[" + q.getTimeAsked() + "]   " + question);
                wr.println(underline);
            }
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * End lecture.
     */
    public void endLecture() {
        if (!ServerCommunication.closeRoom(user.getCurrentRoomID())) {
            return;
        }

        if (user.getUserType().equals("student")) {
            timer.cancel();
            timer.purge();
            questionsUpdater.cancel();
            questionsUpdater.purge();
            questionScreenStudent.setVisible(false);
        } else if (user.getUserType().equals("lecturer") || user.getUserType().equals("moderator")) {
            if (user.getUserType().equals("lecturer")) {
                timer.cancel();
                timer.purge();
                timer2.cancel();
                timer2.purge();
                questionsUpdater.cancel();
                questionsUpdater.purge();
            } else {
                timer2.cancel();
                timer2.purge();
                questionsUpdater.cancel();
                questionsUpdater.purge();
            }
            questionScreenPrivileged.setVisible(false);
        }
        welcomeScreen.setVisible(true);
    }


}
