package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * TreeView component for displaying question categories and questions.
 */
public class QuestionTreeView extends TreeView<Object> {

    private Consumer<Question> onQuestionSelected;

    public QuestionTreeView() {
        TreeItem<Object> root = new TreeItem<>("Questions");
        root.setExpanded(true);
        setRoot(root);
        setShowRoot(false);

        // Custom cell factory for rendering
        setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("category-cell", "question-cell", "solved-cell");

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Category category) {
                    setText(category.getDisplayName());
                    setGraphic(null);
                    getStyleClass().add("category-cell");
                } else if (item instanceof Question question) {
                    String prefix = question.isSolved() ? "✓ " : "";
                    setText(prefix + "Question " + question.getOrder());
                    setGraphic(null);
                    getStyleClass().add("question-cell");
                    if (question.isSolved()) {
                        getStyleClass().add("solved-cell");
                    }
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
            }
        });

        // Selection listener
        getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getValue() instanceof Question question) {
                if (onQuestionSelected != null) {
                    onQuestionSelected.accept(question);
                }
            }
        });

        getStyleClass().add("question-tree");
    }

    /**
     * Populates the tree with categories and questions.
     */
    public void setCategories(Collection<Category> categories) {
        getRoot().getChildren().clear();

        for (Category category : categories) {
            TreeItem<Object> categoryItem = new TreeItem<>(category);
            categoryItem.setExpanded(false);

            for (Question question : category.getQuestions()) {
                TreeItem<Object> questionItem = new TreeItem<>(question);
                categoryItem.getChildren().add(questionItem);
            }

            // Only add categories that have questions
            if (!category.getQuestions().isEmpty()) {
                getRoot().getChildren().add(categoryItem);
            }
        }
    }

    /**
     * Sets the callback for when a question is selected.
     */
    public void setOnQuestionSelected(Consumer<Question> callback) {
        this.onQuestionSelected = callback;
    }

    /**
     * Refreshes the display of a specific question (e.g., after marking as solved).
     */
    public void refreshQuestion(Question question) {
        // Find and refresh the tree item
        for (TreeItem<Object> categoryItem : getRoot().getChildren()) {
            for (TreeItem<Object> questionItem : categoryItem.getChildren()) {
                if (questionItem.getValue() == question) {
                    // Force refresh by setting value again
                    questionItem.setValue(null);
                    questionItem.setValue(question);
                    break;
                }
            }
        }
    }

    /**
     * Selects a specific question in the tree.
     */
    public void selectQuestion(Question question) {
        for (TreeItem<Object> categoryItem : getRoot().getChildren()) {
            for (TreeItem<Object> questionItem : categoryItem.getChildren()) {
                if (questionItem.getValue() == question) {
                    getSelectionModel().select(questionItem);
                    scrollTo(getSelectionModel().getSelectedIndex());
                    return;
                }
            }
        }
    }
}
