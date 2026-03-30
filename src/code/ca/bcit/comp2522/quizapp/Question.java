package ca.bcit.comp2522.quizapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Reads in trivia questions from quiz.txt and loads them into a map.
 *
 * @author Mischa Potter
 * @author Nathan Babec
 * @version 1.0
 */
public class Question
{
    private static final String QUESTION_DELIMETER = "\\|";
    private static final int    INDEX_NUM_ZERO     = 0;

    private static final Map<String, String> questionsAndAnswers;
    private static final List<String>        questions;

    static
    {
        final Path questionPath;

        questionsAndAnswers = new HashMap<>();
        questionPath        = Paths.get("src", "resources", "quiz.txt");

        try
        {
            final List<String> tempList;
            tempList = Files.readAllLines(questionPath);

            for (final String questionAndAnswerStr : tempList)
            {
                final Scanner questionScanner;
                final String question;
                final String answer;

                questionScanner = new Scanner(questionAndAnswerStr);
                questionScanner.useDelimiter(QUESTION_DELIMETER);

                question = questionScanner.next();
                answer   = questionScanner.next();

                questionsAndAnswers.put(question, answer);
            }

        }
        catch (final IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }

        questions = new ArrayList<>(questionsAndAnswers.keySet());
    }

    /**
     * Gets a random trivia question to ask the user.
     *
     * @return a trivia question
     */
    public static String getQuestion()
    {
        final Random randQuestionGen;
        final int randQuestionIndex;

        randQuestionGen   = new Random();
        randQuestionIndex = randQuestionGen.nextInt(INDEX_NUM_ZERO, questions.size());

        return questions.get(randQuestionIndex);
    }

    /**
     * Gets the answer to a trivia question.
     *
     * @param question the question that you want the answer for
     *
     * @return the answer
     */
    public static String getAnswer(final String question)
    {
        return questionsAndAnswers.get(question);
    }
}