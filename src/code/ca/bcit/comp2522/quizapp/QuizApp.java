package ca.bcit.comp2522.quizapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates the use of JavaFX via a simple quiz game.
 *
 * @author Mischa Potter
 * @version 1.0
 */
public class QuizApp extends Application
{
    private static final int VBOX_WIDTH_PIXELS   = 700;
    private static final int VBOX_HEIGHT_PIXELS  = 500;
    private static final int MAX_NUM_QUESTIONS   = 10;
    private static final int VBOX_SPACING_PIXELS = 20;

    private static final String QUIZ_APP_TITLE           = "Quiz Game";
    private static final String STYLESHEET_PATH          = "/styles.css";
    private static final String START_BTN_MESSAGE        = "Start Quiz";
    private static final String SUBMIT_BTN_MESSAGE       = "Submit";
    private static final String DEFAULT_SCORE_MESSAGE    = "Score: 0";
    private static final String DEFAULT_QUESTION_MESSAGE = "Press 'Start Quiz' to begin!";


    private static final List<String> incorrectQuestions;
    private static       int          scoreNum;
    private static       int          numQuestionsAsked;

    static
    {
        incorrectQuestions = new ArrayList<>();
    }

    /**
     * Creates the JavaFX Quiz App.
     *
     * @param primaryStage the primary stage of the app
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final Scene mainGame;
        final VBox layout;

        layout = createLayout();
        layout.getStyleClass().add("vbox");

        mainGame = new Scene(layout, VBOX_WIDTH_PIXELS, VBOX_HEIGHT_PIXELS);
        mainGame.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        primaryStage.setTitle(QUIZ_APP_TITLE);
        primaryStage.setScene(mainGame);

        primaryStage.show();
    }

    /**
     * Creates the layout for this QuizApp.
     *
     * @return a VBox containing the layout
     */
    private VBox createLayout()
    {
        final Button submitBtn;
        final Button startQuizBtn;
        final Label question;
        final Label score;
        final TextField userInput;
        final VBox layout;
        final TextArea missedQuestions;

        submitBtn       = new Button(SUBMIT_BTN_MESSAGE);
        startQuizBtn    = new Button(START_BTN_MESSAGE);
        question        = new Label(DEFAULT_QUESTION_MESSAGE);
        userInput       = new TextField();
        score           = new Label(DEFAULT_SCORE_MESSAGE);
        layout          = new VBox(VBOX_SPACING_PIXELS);
        missedQuestions = new TextArea();

        missedQuestions.setVisible(false);
        submitBtn.setDisable(true);
        startQuizBtn.setOnAction(event -> startQuiz(question, submitBtn, userInput, score, missedQuestions,
                                                    startQuizBtn));
        layout.getChildren().addAll(question, submitBtn, startQuizBtn, userInput, score, missedQuestions);

        return layout;
    }

    /*
     * Starts an instance of the QuizApp. Contains the logic for most of the QuizApp,
     * including resetting variables after restarting the game and handling the control
     * flow.
     *
     * @param question        a Label containing the question asked
     * @param submitBtn       a Button that represents the "submit answer" button
     * @param userInput       a TextField where the user inputs their answer
     * @param scoreLabel      a Label displaying the user's current score
     * @param missedQuestions a TextArea that displays the user's missed questions from this
     *                        loop of a QuizApp
     * @param startQuizBtn    a Button that lets the user start the game
     */
    private static void startQuiz(final Label question,
                                  final Button submitBtn,
                                  final TextField userInput,
                                  final Label scoreLabel,
                                  final TextArea missedQuestions,
                                  final Button startQuizBtn)
    {
        // Resetting parts of the game if the user plays again
        scoreNum          = 0;
        numQuestionsAsked = 0;
        scoreLabel.setText("Score: " + scoreNum);
        incorrectQuestions.clear();
        startQuizBtn.setDisable(true);
        submitBtn.setDisable(false);
        missedQuestions.setVisible(false);

        question.setText(Question.getQuestion());

        submitBtn.setOnAction(event ->
                              {
                                  boolean userCorrect;

                                  userCorrect = checkAnswer(userInput.getText(), question.getText());

                                  if (userCorrect)
                                  {
                                      scoreNum++;
                                      scoreLabel.setText("Score: " + scoreNum);
                                  }
                                  else
                                  {
                                      incorrectQuestions.add(question.getText());
                                  }

                                  userInput.clear();

                                  if (numQuestionsAsked < MAX_NUM_QUESTIONS)
                                  {
                                      question.setText(Question.getQuestion());
                                  }
                                  else
                                  {
                                      startQuizBtn.setDisable(false);
                                      endQuiz(submitBtn, missedQuestions);
                                  }

                                  numQuestionsAsked++;
                              });
    }

    /*
     * Ends the quiz by disabling buttons and displaying missed questions.
     *
     * @param submitBtn a Button that represents the "submit answer" button
     * @param missedQuestions a TextArea that displays the user's missed questions
     */
    private static void endQuiz(final Button submitBtn,
                                final TextArea missedQuestions)
    {
        submitBtn.setDisable(true);
        final StringBuilder builder;
        builder = new StringBuilder();

        builder.append("Missed Questions:\n");
        for (final String question : incorrectQuestions)
        {
            builder.append("\nQ: ");
            builder.append(question);
            builder.append("\nA: ");
            builder.append(Question.getAnswer(question));
            builder.append("\n");
        }

        // make missed questions visible and add the text
        missedQuestions.setVisible(true);
        missedQuestions.setText(builder.toString());
    }

    /**
     * Checks if the user's guess is the correct answer.
     *
     * @param userInput the user's guess
     * @param question  the question the user was answering for
     *
     * @return true if the user's guess is correct
     */
    private static boolean checkAnswer(final String userInput,
                                       final String question)
    {
        return Question.getAnswer(question).equalsIgnoreCase(userInput);
    }

    /**
     * Runs the JavaFX app.
     *
     * @param args used??
     */
    public static void main(final String[] args)
    {
        launch(args);
    }
}
