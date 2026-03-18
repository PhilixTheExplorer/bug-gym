package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.application.AppController;
import dev.philixtheexplorer.buggym.application.AppVersionResolver;
import dev.philixtheexplorer.buggym.application.UpdateCheckUseCase;
import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.ui.AppDialogs;
import dev.philixtheexplorer.buggym.ui.CodeEditor;
import dev.philixtheexplorer.buggym.ui.HomePageView;
import dev.philixtheexplorer.buggym.ui.MainWorkspacePane;
import dev.philixtheexplorer.buggym.ui.QuestionTreeView;
import dev.philixtheexplorer.buggym.ui.ResultsPanel;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
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

    private AppBootstrap.BootstrapContext runtime;

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

        try {
            runtime = new AppBootstrap().initialize();
        } catch (IOException e) {
            showError("Failed to load questions", e.getMessage());
        }

        BorderPane root = createMainLayout();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        runtime.stageConfigurator().configureMainStage(stage, scene, this::shutdownRuntime);

        stage.show();

        selectFirstQuestion();
        checkForUpdates(true);
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        AppInteractionFactory.Callbacks callbacks = createInteractionCallbacks();

        MenuBar menuBar = AppInteractionFactory.createMenuBar(runtime.appController().getCategories(), darkMode,
                callbacks);
        root.setTop(menuBar);

        workspacePane = AppInteractionFactory.createWorkspacePane(runtime.appController().getCategories(), callbacks);

        questionTree = workspacePane.getQuestionTree();
        codeEditor = workspacePane.getCodeEditor();
        questionView = workspacePane.getQuestionView();
        resultsPanel = workspacePane.getResultsPanel();
        progressLabel = workspacePane.getProgressLabel();

        mainContentSplit = workspacePane;
        updateProgress();

        homeContainer = new HomePageView(runtime.appController().getCategories(), this::openCategoryFromHome);

        contentStack = new StackPane();
        contentStack.getChildren().addAll(mainContentSplit, homeContainer);
        root.setCenter(contentStack);

        showHomePage();
        return root;
    }

    private AppInteractionFactory.Callbacks createInteractionCallbacks() {
        return new AppInteractionFactory.Callbacks(
                this::saveProgress,
                this::clearCode,
                this::resetToStarter,
                this::toggleDarkMode,
                this::toggleAutoIndent,
                this::toggleAutoBracketPairing,
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
                this::showAbout,
                this::onQuestionSelected,
                this::navigateQuestion);
    }

    private void openCategoryFromMenu(Category category) {
        if (!category.getQuestions().isEmpty()) {
            questionTree.selectQuestion(category.getQuestions().get(0));
        }
    }

    private void updateProgress() {
        if (progressLabel == null || runtime == null) {
            return;
        }

        AppController.ProgressSnapshot snapshot = runtime.appController().getProgressSnapshot();
        runtime.workspaceUiCoordinator().updateProgressLabel(
                progressLabel,
                snapshot.solvedQuestions(),
                snapshot.totalQuestions());

        if (homeContainer != null) {
            runtime.workspaceUiCoordinator().refreshHomeCategories(homeContainer,
                    runtime.appController().getCategories());
        }
    }

    private void openCategoryFromHome(Category category) {
        Question target = runtime.sessionFlowCoordinator().resolveHomeTarget(category);
        showPracticePage();
        questionTree.selectQuestion(target);
    }

    private void showHomePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        runtime.workspaceUiCoordinator().showHomePage(homeContainer, mainContentSplit,
                runtime.appController().getCategories());
    }

    private void showPracticePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        runtime.workspaceUiCoordinator().showPracticePage(homeContainer, mainContentSplit);
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
        var update = runtime.sessionFlowCoordinator().handleQuestionSelected(
                question,
                codeEditor.getCode(),
                suppressPracticeAutoSwitch,
                darkMode);

        if (update == null) {
            return;
        }

        if (update.shouldShowPracticePage()) {
            showPracticePage();
        }

        questionView.getEngine().loadContent(update.questionHtml());
        codeEditor.setCode(update.codeToLoad());
        resultsPanel.clear();
    }

    private void navigateQuestion(int direction) {
        runtime.sessionFlowCoordinator().navigateQuestion(direction, this::selectFirstQuestion,
                questionTree::selectQuestion);
    }

    private void executeCodeRun(boolean isSubmission) {
        Question currentQuestion = runtime.appController().getCurrentQuestion();
        if (currentQuestion == null) {
            showError("No Question Selected", "Please select a question from the sidebar.");
            return;
        }

        String code = codeEditor.getCode();
        if (code.trim().isEmpty()) {
            showError("Empty Code", "Please write some code before running tests.");
            return;
        }

        Task<RunResult> task = runtime.executionUseCase().createExecutionTask(currentQuestion, code);

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

        runtime.taskRunner().run(task);
    }

    private void handleSuccessfulSubmission() {
        runtime.appController().markCurrentSolved(codeEditor.getCode());

        Question currentQuestion = runtime.appController().getCurrentQuestion();
        questionTree.refreshQuestion(currentQuestion);
        updateProgress();

        Question nextQuestion = runtime.appController().getNextQuestion();
        runtime.submissionFeedbackCoordinator().showSuccessDialogAndHandleNext(
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
        Question currentQuestion = runtime.appController().getCurrentQuestion();
        if (currentQuestion != null) {
            codeEditor.setCode(currentQuestion.getStarterCode());
            resultsPanel.clear();
        }
    }

    private void showHint() {
        Question currentQuestion = runtime.appController().getCurrentQuestion();
        if (currentQuestion != null) {
            resultsPanel.showHint(currentQuestion.getHint());
        }
    }

    private void toggleDarkMode(boolean dark) {
        this.darkMode = dark;

        Scene scene = codeEditor.getScene();
        if (scene != null) {
            runtime.workspaceUiCoordinator().applyTheme(scene, dark);
        }

        Question currentQuestion = runtime.appController().getCurrentQuestion();
        if (currentQuestion != null) {
            String html = runtime.appController().getQuestionHtml(currentQuestion, darkMode);
            questionView.getEngine().loadContent(html);
        }
    }

    private void toggleAutoIndent(boolean enabled) {
        if (codeEditor != null) {
            codeEditor.setAutoIndentEnabled(enabled);
        }
    }

    private void toggleAutoBracketPairing(boolean enabled) {
        if (codeEditor != null) {
            codeEditor.setAutoBracketPairingEnabled(enabled);
        }
    }

    private void selectFirstQuestion() {
        Question first = runtime.sessionFlowCoordinator().getFirstQuestion();
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
        Task<UpdateCheckUseCase.UpdateCheckResult> task = runtime.updateCheckUseCase().createCheckTask(currentVersion);

        task.setOnSucceeded(e -> {
            UpdateCheckUseCase.UpdateCheckResult result = task.getValue();

            if (result.status() == UpdateCheckUseCase.Status.VERSION_UNAVAILABLE) {
                runtime.updateFeedbackCoordinator().showVersionUnavailable(getClass(), silent);
                return;
            }

            if (result.status() == UpdateCheckUseCase.Status.UPDATE_AVAILABLE) {
                runtime.updateFeedbackCoordinator().showUpdateAvailable(
                        getClass(),
                        result.currentVersion(),
                        result.latestVersion(),
                        url -> getHostServices().showDocument(url));
                return;
            }

            runtime.updateFeedbackCoordinator().showUpToDate(getClass(), silent, result.currentVersion());
        });

        task.setOnFailed(e -> runtime.updateFeedbackCoordinator().handleFailure(getClass(), silent));

        runtime.taskRunner().run(task);
    }

    private void showKeyboardShortcuts() {
        AppDialogs.showKeyboardShortcuts(getClass());
    }

    private void saveProgress() {
        runtime.sessionFlowCoordinator().persistCurrentCode(codeEditor.getCode());
    }

    private void toggleSidebar() {
        if (workspacePane == null) {
            return;
        }
        workspacePane.toggleSidebar();
    }

    @Override
    public void stop() {
        if (codeEditor != null && runtime != null) {
            runtime.sessionFlowCoordinator().persistCurrentCode(codeEditor.getCode());
        }
        shutdownRuntime();
    }

    private void shutdownRuntime() {
        if (runtime != null) {
            runtime.shutdownRuntime();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
