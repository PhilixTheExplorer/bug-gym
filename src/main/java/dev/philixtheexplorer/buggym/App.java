package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.service.CodeRunner;
import dev.philixtheexplorer.buggym.service.ProgressManager;
import dev.philixtheexplorer.buggym.service.QuestionLoader;
import dev.philixtheexplorer.buggym.service.UpdateService;
import dev.philixtheexplorer.buggym.ui.AppDialogs;
import dev.philixtheexplorer.buggym.ui.CodeEditor;
import dev.philixtheexplorer.buggym.ui.HomePageView;
import dev.philixtheexplorer.buggym.ui.MainMenuBarFactory;
import dev.philixtheexplorer.buggym.ui.MainWorkspacePane;
import dev.philixtheexplorer.buggym.ui.QuestionTreeView;
import dev.philixtheexplorer.buggym.ui.ResultsPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.*;

/**
 * Bug Gym - A mini coding practice platform for Java beginners.
 */
public class App extends Application {

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 600;
    private QuestionLoader questionLoader;
    private CodeRunner codeRunner;
    private ProgressManager progressManager;
    private UpdateService updateService;

    private QuestionTreeView questionTree;
    private WebView questionView;
    private CodeEditor codeEditor;
    private ResultsPanel resultsPanel;
    private Label progressLabel;

    private Question currentQuestion;
    private boolean darkMode = true;

    private boolean suppressPracticeAutoSwitch = false;

    @Override
    public void start(Stage stage) {
        // Load custom fonts
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 14);

        // Initialize services
        questionLoader = new QuestionLoader();
        codeRunner = new CodeRunner();
        progressManager = new ProgressManager();
        updateService = new UpdateService();

        // Load questions
        try {
            questionLoader.loadQuestions();
            // Load progress for each question
            for (Question q : questionLoader.getQuestions()) {
                progressManager.loadProgress(q);
            }
        } catch (IOException e) {
            showError("Failed to load questions", e.getMessage());
        }

        // Create main layout
        BorderPane root = createMainLayout();

        // Create scene with styling
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        // Set up stage
        stage.setTitle("Bug Gym - Java Practice Platform");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);

        // Handle close
        stage.setOnCloseRequest(event -> {
            codeRunner.shutdown();
        });

        stage.show();

        // Select first question if available
        selectFirstQuestion();

        // Check for updates silently
        checkForUpdates(true);
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Top area with menu bar
        MenuBar menuBar = MainMenuBarFactory.create(questionLoader.getCategories(), darkMode,
            new MainMenuBarFactory.Actions(
                this::saveProgress,
                Platform::exit,
                this::clearCode,
                this::resetToStarter,
                this::toggleDarkMode,
                this::showHomePage,
                this::showPracticePage,
                this::toggleSidebar,
                this::increaseZoom,
                this::decreaseZoom,
                this::resetZoom,
                this::runTests,
                this::submitSolution,
                this::openCategoryFromMenu,
                this::showHint,
                this::showKeyboardShortcuts,
                () -> checkForUpdates(false),
                this::showAbout
            ));
        root.setTop(menuBar);

        workspacePane = new MainWorkspacePane(
            questionLoader.getCategories(),
            this::onQuestionSelected,
            this::navigateQuestion,
            this::runTests,
            this::submitSolution,
            this::clearCode,
            this::resetToStarter,
            this::showHint
        );

        questionTree = workspacePane.getQuestionTree();
        codeEditor = workspacePane.getCodeEditor();
        questionView = workspacePane.getQuestionView();
        resultsPanel = workspacePane.getResultsPanel();
        progressLabel = workspacePane.getProgressLabel();

        mainContentSplit = workspacePane;
        updateProgress();

        homeContainer = new HomePageView(questionLoader.getCategories(), this::openCategoryFromHome);

        contentStack = new StackPane();
        contentStack.getChildren().addAll(mainContentSplit, homeContainer);
        root.setCenter(contentStack);

        showHomePage();

        return root;
    }

    private void openCategoryFromMenu(Category category) {
        if (!category.getQuestions().isEmpty()) {
            questionTree.selectQuestion(category.getQuestions().get(0));
        }
    }

    private void updateProgress() {
        if (progressLabel == null || questionLoader == null)
            return;
        long totalQuestions = questionLoader.getQuestions().size();
        long solvedQuestions = questionLoader.getQuestions().stream()
                .filter(Question::isSolved).count();
        progressLabel.setText("Progress: %d/%d solved".formatted(solvedQuestions, totalQuestions));

        if (homeContainer != null) {
            homeContainer.refreshCategories(questionLoader.getCategories());
        }
    }

    private void openCategoryFromHome(Category category) {
        Question target = category.getQuestions().stream()
                .filter(q -> !q.isSolved())
                .findFirst()
                .orElse(category.getQuestions().get(0));

        showPracticePage();
        questionTree.selectQuestion(target);
    }

    private void showHomePage() {
        if (homeContainer == null || mainContentSplit == null)
            return;
        homeContainer.refreshCategories(questionLoader.getCategories());
        homeContainer.setVisible(true);
        homeContainer.setManaged(true);
        mainContentSplit.setVisible(false);
        mainContentSplit.setManaged(false);
    }

    private void showPracticePage() {
        if (homeContainer == null || mainContentSplit == null)
            return;
        homeContainer.setVisible(false);
        homeContainer.setManaged(false);
        mainContentSplit.setVisible(true);
        mainContentSplit.setManaged(true);
    }

    private void increaseZoom() {
        if (codeEditor != null) {
            codeEditor.increaseFontSize();
        }
    }

    private void decreaseZoom() {
        if (codeEditor != null) {
            codeEditor.decreaseFontSize();
        }
    }

    private void resetZoom() {
        if (codeEditor != null) {
            codeEditor.resetFontSize();
        }
    }

    private void onQuestionSelected(Question question) {
        if (question == null)
            return;

        // Save current code before switching
        if (currentQuestion != null) {
            currentQuestion.setUserCode(codeEditor.getCode());
            progressManager.saveProgress(currentQuestion);
        }

        currentQuestion = question;

        if (!suppressPracticeAutoSwitch) {
            showPracticePage();
        }

        // Load question content into WebView
        String html = questionLoader.getQuestionHtml(question, darkMode);
        questionView.getEngine().loadContent(html);

        // Load code (user code if exists, otherwise starter)
        if (question.getUserCode() != null && !question.getUserCode().isEmpty()) {
            codeEditor.setCode(question.getUserCode());
        } else {
            codeEditor.setCode(question.getStarterCode());
        }

        // Clear results
        resultsPanel.clear();
    }

    private void navigateQuestion(int direction) {
        if (currentQuestion == null) {
            selectFirstQuestion();
            return;
        }
        var questions = questionLoader.getQuestions();
        int idx = questions.indexOf(currentQuestion);
        int newIdx = idx + direction;
        if (newIdx >= 0 && newIdx < questions.size()) {
            questionTree.selectQuestion(questions.get(newIdx));
        }
    }

    private void executeCodeRun(boolean isSubmission) {
        if (currentQuestion == null) {
            showError("No Question Selected", "Please select a question from the sidebar.");
            return;
        }

        String code = codeEditor.getCode();
        if (code.trim().isEmpty()) {
            showError("Empty Code", "Please write some code before running tests.");
            return;
        }

        if (currentQuestion.getTestCases().isEmpty()) {
            // Run without test cases (just run main)
            Task<RunResult> task = new Task<>() {
                @Override
                protected RunResult call() {
                    return codeRunner.runMain(code);
                }
            };

            resultsPanel.showLoading();
            codeEditor.setEditable(false);

            task.setOnSucceeded(e -> {
                RunResult result = task.getValue();
                resultsPanel.showResults(result);
                codeEditor.setEditable(true);
            });

            task.setOnFailed(e -> {
                resultsPanel.showError("Execution failed: " + task.getException().getMessage());
                codeEditor.setEditable(true);
            });

            new Thread(task).start();
            return;
        }

        resultsPanel.showLoading();
        codeEditor.setEditable(false);

        Task<RunResult> task = new Task<>() {
            @Override
            protected RunResult call() {
                return codeRunner.runTests(code, currentQuestion.getTestCases());
            }
        };

        task.setOnSucceeded(e -> {
            RunResult result = task.getValue();
            resultsPanel.showResults(result);
            codeEditor.setEditable(true);

            if (isSubmission && result.allPassed()) {
                handleSuccessfulSubmission(result);
            }
        });

        task.setOnFailed(e -> {
            resultsPanel.showError("Execution failed: " + task.getException().getMessage());
            codeEditor.setEditable(true);
        });

        new Thread(task).start();
    }

    private void handleSuccessfulSubmission(RunResult result) {
        currentQuestion.setSolved(true);
        currentQuestion.setUserCode(codeEditor.getCode());
        progressManager.saveProgress(currentQuestion);
        questionTree.refreshQuestion(currentQuestion);
        updateProgress();

        Alert alert = AppDialogs.createStyledAlert(getClass(), Alert.AlertType.INFORMATION, "Congratulations!",
            "🎉 All tests passed!");
        alert.setContentText("Great job! You've successfully solved this question.");

        ButtonType nextQuestionBtn = new ButtonType("Next Question", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType stayBtn = new ButtonType("Stay Here", ButtonBar.ButtonData.CANCEL_CLOSE);

        Question nextQuestion = getNextQuestion(currentQuestion);
        if (nextQuestion != null) {
            alert.getButtonTypes().setAll(nextQuestionBtn, stayBtn);
        } else {
            alert.getButtonTypes().setAll(ButtonType.OK);
        }

        Optional<ButtonType> resultBtn = alert.showAndWait();
        if (resultBtn.isPresent() && resultBtn.get() == nextQuestionBtn) {
            questionTree.selectQuestion(nextQuestion);
        }
    }

    private void runTests() {
        executeCodeRun(false);
    }

    private void submitSolution() {
        executeCodeRun(true);
    }

    private Question getNextQuestion(Question current) {
        List<Question> allQuestions = questionLoader.getQuestions();
        int index = allQuestions.indexOf(current);
        if (index >= 0 && index < allQuestions.size() - 1) {
            return allQuestions.get(index + 1);
        }
        return null;
    }

    private void clearCode() {
        codeEditor.clear();
        resultsPanel.clear();
    }

    private void resetToStarter() {
        if (currentQuestion != null) {
            codeEditor.setCode(currentQuestion.getStarterCode());
            resultsPanel.clear();
        }
    }

    private void showHint() {
        if (currentQuestion != null) {
            resultsPanel.showHint(currentQuestion.getHint());
        }
    }

    private void toggleDarkMode(boolean dark) {
        this.darkMode = dark;
        // The CSS handles dark/light mode through the root class
        Scene scene = codeEditor.getScene();
        if (scene != null) {
            if (dark) {
                scene.getRoot().getStyleClass().remove("light-mode");
            } else {
                scene.getRoot().getStyleClass().add("light-mode");
            }
        }
        // Refresh question view with new theme
        if (currentQuestion != null) {
            String html = questionLoader.getQuestionHtml(currentQuestion, darkMode);
            questionView.getEngine().loadContent(html);
        }
    }

    private void selectFirstQuestion() {
        var categories = questionLoader.getCategories();
        suppressPracticeAutoSwitch = true;
        for (var category : categories) {
            if (!category.getQuestions().isEmpty()) {
                Question first = category.getQuestions().get(0);
                questionTree.selectQuestion(first);
                break;
            }
        }
        suppressPracticeAutoSwitch = false;
    }

    private void showError(String title, String message) {
        AppDialogs.showError(getClass(), title, message);
    }

    private void showAbout() {
        String version = getAppVersion();
        AppDialogs.showAbout(getClass(), version, url -> getHostServices().showDocument(url));
    }

    private String getAppVersion() {
        String fromManifest = App.class.getPackage() != null
                ? App.class.getPackage().getImplementationVersion()
                : null;
        if (fromManifest != null && !fromManifest.isBlank()) {
            return fromManifest;
        }

        String fromProperty = System.getProperty("app.version");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }

        return "dev";
    }

    private void checkForUpdates(boolean silent) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return updateService.fetchLatestVersion();
            }
        };
        task.setOnSucceeded(e -> {
            String latest = task.getValue();
            String current = getAppVersion();
            if (latest == null) {
                if (!silent)
                    showInfo("Update Check", "Could not retrieve version info.");
            } else if ("dev".equals(current) || updateService.compareVersions(current, latest) < 0) {
                AppDialogs.showUpdateAvailable(getClass(), current, latest, url -> getHostServices().showDocument(url));
            } else {
                if (!silent)
                    showInfo("Up to Date", "You are running the latest version (v" + current + ").");
            }
        });
        task.setOnFailed(e -> {
            if (!silent)
                showInfo("Update Check", "Could not check for updates.");
        });
        new Thread(task).start();
    }

    private void showInfo(String title, String message) {
        AppDialogs.showInfo(getClass(), title, message);
    }

    private void showKeyboardShortcuts() {
        AppDialogs.showKeyboardShortcuts(getClass());
    }

    private void saveProgress() {
        if (currentQuestion != null) {
            currentQuestion.setUserCode(codeEditor.getCode());
            progressManager.saveProgress(currentQuestion);
        }
    }

    private MainWorkspacePane workspacePane;
    private SplitPane mainContentSplit;
    private StackPane contentStack;
    private HomePageView homeContainer;

    private void toggleSidebar() {
        if (workspacePane == null) {
            return;
        }
        workspacePane.toggleSidebar();
    }

    @Override
    public void stop() {
        if (currentQuestion != null && codeEditor != null) {
            currentQuestion.setUserCode(codeEditor.getCode());
            progressManager.saveProgress(currentQuestion);
        }
        codeRunner.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}
