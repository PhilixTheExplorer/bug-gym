package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.application.AppController;
import dev.philixtheexplorer.buggym.application.AppVersionResolver;
import dev.philixtheexplorer.buggym.application.BackgroundTaskRunner;
import dev.philixtheexplorer.buggym.application.CodeExecutionEngine;
import dev.philixtheexplorer.buggym.application.ExecutionUseCase;
import dev.philixtheexplorer.buggym.application.UpdateCheckUseCase;
import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.service.InProcessCodeExecutionEngine;
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
import dev.philixtheexplorer.buggym.ui.SubmissionFeedbackCoordinator;
import dev.philixtheexplorer.buggym.ui.UpdateFeedbackCoordinator;
import dev.philixtheexplorer.buggym.ui.WorkspaceUiCoordinator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Bug Gym - A mini coding practice platform for Java beginners.
 */
public class App extends Application {

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 600;

    private AppController appController;
    private BackgroundTaskRunner taskRunner;
    private CodeExecutionEngine codeExecutionEngine;
    private ExecutionUseCase executionUseCase;
    private UpdateService updateService;
    private UpdateCheckUseCase updateCheckUseCase;
    private WorkspaceUiCoordinator workspaceUiCoordinator;
    private SubmissionFeedbackCoordinator submissionFeedbackCoordinator;
    private UpdateFeedbackCoordinator updateFeedbackCoordinator;

    private QuestionTreeView questionTree;
    private WebView questionView;
    private CodeEditor codeEditor;
    private ResultsPanel resultsPanel;
    private Label progressLabel;

    private boolean darkMode = true;
    private boolean suppressPracticeAutoSwitch = false;

    private MainWorkspacePane workspacePane;
    private SplitPane mainContentSplit;
    private StackPane contentStack;
    private HomePageView homeContainer;

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 14);

        QuestionLoader questionLoader = new QuestionLoader();
        ProgressManager progressManager = new ProgressManager();
        appController = new AppController(questionLoader, progressManager);
        taskRunner = new BackgroundTaskRunner("buggym-app-bg", 2);

        codeExecutionEngine = new InProcessCodeExecutionEngine();
        executionUseCase = new ExecutionUseCase(codeExecutionEngine);
        updateService = new UpdateService();
        updateCheckUseCase = new UpdateCheckUseCase(updateService);
        workspaceUiCoordinator = new WorkspaceUiCoordinator();
        submissionFeedbackCoordinator = new SubmissionFeedbackCoordinator();
        updateFeedbackCoordinator = new UpdateFeedbackCoordinator();

        try {
            appController.loadQuestionsAndProgress();
        } catch (IOException e) {
            showError("Failed to load questions", e.getMessage());
        }

        BorderPane root = createMainLayout();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        stage.setTitle("Bug Gym - Java Practice Platform");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);

        stage.setOnCloseRequest(event -> {
            if (taskRunner != null) {
                taskRunner.shutdownNow();
            }
            if (codeExecutionEngine != null) {
                codeExecutionEngine.shutdown();
            }
        });

        stage.show();

        selectFirstQuestion();
        checkForUpdates(true);
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        MenuBar menuBar = MainMenuBarFactory.create(appController.getCategories(), darkMode,
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
                        this::showAbout));
        root.setTop(menuBar);

        workspacePane = new MainWorkspacePane(
                appController.getCategories(),
                this::onQuestionSelected,
                this::navigateQuestion,
                this::runTests,
                this::submitSolution,
                this::clearCode,
                this::resetToStarter,
                this::showHint);

        questionTree = workspacePane.getQuestionTree();
        codeEditor = workspacePane.getCodeEditor();
        questionView = workspacePane.getQuestionView();
        resultsPanel = workspacePane.getResultsPanel();
        progressLabel = workspacePane.getProgressLabel();

        mainContentSplit = workspacePane;
        updateProgress();

        homeContainer = new HomePageView(appController.getCategories(), this::openCategoryFromHome);

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
        if (progressLabel == null || appController == null) {
            return;
        }

        AppController.ProgressSnapshot snapshot = appController.getProgressSnapshot();
        workspaceUiCoordinator.updateProgressLabel(
            progressLabel,
            snapshot.solvedQuestions(),
            snapshot.totalQuestions());

        if (homeContainer != null) {
            workspaceUiCoordinator.refreshHomeCategories(homeContainer, appController.getCategories());
        }
    }

    private void openCategoryFromHome(Category category) {
        Question target = appController.getFirstUnsolvedOrFirst(category);
        showPracticePage();
        questionTree.selectQuestion(target);
    }

    private void showHomePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        workspaceUiCoordinator.showHomePage(homeContainer, mainContentSplit, appController.getCategories());
    }

    private void showPracticePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        workspaceUiCoordinator.showPracticePage(homeContainer, mainContentSplit);
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
        if (question == null) {
            return;
        }

        appController.persistCurrentCode(codeEditor.getCode());
        appController.setCurrentQuestion(question);

        if (!suppressPracticeAutoSwitch) {
            showPracticePage();
        }

        String html = appController.getQuestionHtml(question, darkMode);
        questionView.getEngine().loadContent(html);

        codeEditor.setCode(appController.getInitialCodeFor(question));
        resultsPanel.clear();
    }

    private void navigateQuestion(int direction) {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion == null) {
            selectFirstQuestion();
            return;
        }

        var questions = appController.getQuestions();
        int idx = questions.indexOf(currentQuestion);
        int newIdx = idx + direction;
        if (newIdx >= 0 && newIdx < questions.size()) {
            questionTree.selectQuestion(questions.get(newIdx));
        }
    }

    private void executeCodeRun(boolean isSubmission) {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion == null) {
            showError("No Question Selected", "Please select a question from the sidebar.");
            return;
        }

        String code = codeEditor.getCode();
        if (code.trim().isEmpty()) {
            showError("Empty Code", "Please write some code before running tests.");
            return;
        }

        Task<RunResult> task = executionUseCase.createExecutionTask(currentQuestion, code);

        resultsPanel.showLoading();
        codeEditor.setEditable(false);

        task.setOnSucceeded(e -> {
            RunResult result = task.getValue();
            resultsPanel.showResults(result);
            codeEditor.setEditable(true);

            if (isSubmission && result.allPassed()) {
                handleSuccessfulSubmission();
            }
        });

        task.setOnFailed(e -> {
            resultsPanel.showError("Execution failed: " + task.getException().getMessage());
            codeEditor.setEditable(true);
        });

        taskRunner.run(task);
    }

    private void handleSuccessfulSubmission() {
        appController.markCurrentSolved(codeEditor.getCode());

        Question currentQuestion = appController.getCurrentQuestion();
        questionTree.refreshQuestion(currentQuestion);
        updateProgress();

        Question nextQuestion = appController.getNextQuestion();
        submissionFeedbackCoordinator.showSuccessDialogAndHandleNext(
                getClass(),
                nextQuestion,
                questionTree::selectQuestion);
    }

    private void runTests() {
        executeCodeRun(false);
    }

    private void submitSolution() {
        executeCodeRun(true);
    }

    private void clearCode() {
        codeEditor.clear();
        resultsPanel.clear();
    }

    private void resetToStarter() {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            codeEditor.setCode(currentQuestion.getStarterCode());
            resultsPanel.clear();
        }
    }

    private void showHint() {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            resultsPanel.showHint(currentQuestion.getHint());
        }
    }

    private void toggleDarkMode(boolean dark) {
        this.darkMode = dark;

        Scene scene = codeEditor.getScene();
        if (scene != null) {
            workspaceUiCoordinator.applyTheme(scene, dark);
        }

        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            String html = appController.getQuestionHtml(currentQuestion, darkMode);
            questionView.getEngine().loadContent(html);
        }
    }

    private void selectFirstQuestion() {
        Question first = appController.getFirstQuestion();
        suppressPracticeAutoSwitch = true;
        if (first != null) {
            questionTree.selectQuestion(first);
        }
        suppressPracticeAutoSwitch = false;
    }

    private void showError(String title, String message) {
        AppDialogs.showError(getClass(), title, message);
    }

    private void showAbout() {
        String version = AppVersionResolver.resolve(App.class);
        AppDialogs.showAbout(getClass(), version, url -> getHostServices().showDocument(url));
    }

    private void checkForUpdates(boolean silent) {
        String currentVersion = AppVersionResolver.resolve(App.class);
        Task<UpdateCheckUseCase.UpdateCheckResult> task = updateCheckUseCase.createCheckTask(currentVersion);

        task.setOnSucceeded(e -> {
            UpdateCheckUseCase.UpdateCheckResult result = task.getValue();

            if (result.status() == UpdateCheckUseCase.Status.VERSION_UNAVAILABLE) {
                updateFeedbackCoordinator.showVersionUnavailable(getClass(), silent);
                return;
            }

            if (result.status() == UpdateCheckUseCase.Status.UPDATE_AVAILABLE) {
                updateFeedbackCoordinator.showUpdateAvailable(
                        getClass(),
                        result.currentVersion(),
                        result.latestVersion(),
                        url -> getHostServices().showDocument(url));
                return;
            }

            updateFeedbackCoordinator.showUpToDate(getClass(), silent, result.currentVersion());
        });

        task.setOnFailed(e -> updateFeedbackCoordinator.handleFailure(getClass(), silent));

        taskRunner.run(task);
    }

    private void showKeyboardShortcuts() {
        AppDialogs.showKeyboardShortcuts(getClass());
    }

    private void saveProgress() {
        appController.persistCurrentCode(codeEditor.getCode());
    }

    private void toggleSidebar() {
        if (workspacePane == null) {
            return;
        }
        workspacePane.toggleSidebar();
    }

    @Override
    public void stop() {
        if (codeEditor != null && appController != null) {
            appController.persistCurrentCode(codeEditor.getCode());
        }
        if (taskRunner != null) {
            taskRunner.shutdownNow();
        }
        if (codeExecutionEngine != null) {
            codeExecutionEngine.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
