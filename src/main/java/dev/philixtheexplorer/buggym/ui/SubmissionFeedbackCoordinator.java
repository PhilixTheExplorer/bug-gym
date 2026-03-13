package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.Question;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Handles user feedback UI after successful submissions.
 */
public class SubmissionFeedbackCoordinator {

    public void showSuccessDialogAndHandleNext(
            Class<?> resourceOwner,
            Question nextQuestion,
            Consumer<Question> onNextQuestion
    ) {
        Alert alert = AppDialogs.createStyledAlert(resourceOwner, Alert.AlertType.INFORMATION,
                "Congratulations!", "🎉 All tests passed!");
        alert.setContentText("Great job! You've successfully solved this question.");

        ButtonType nextQuestionBtn = new ButtonType("Next Question", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType stayBtn = new ButtonType("Stay Here", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (nextQuestion != null) {
            alert.getButtonTypes().setAll(nextQuestionBtn, stayBtn);
        } else {
            alert.getButtonTypes().setAll(ButtonType.OK);
        }

        Optional<ButtonType> resultBtn = alert.showAndWait();
        if (nextQuestion != null && resultBtn.isPresent() && resultBtn.get() == nextQuestionBtn) {
            onNextQuestion.accept(nextQuestion);
        }
    }
}
