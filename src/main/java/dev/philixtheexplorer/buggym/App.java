package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.service.CodeRunner;
import dev.philixtheexplorer.buggym.service.ProgressManager;
import dev.philixtheexplorer.buggym.service.QuestionLoader;
import dev.philixtheexplorer.buggym.ui.CodeEditor;
import dev.philixtheexplorer.buggym.ui.QuestionTreeView;
import dev.philixtheexplorer.buggym.ui.ResultsPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * Bug Gym - A mini coding practice platform for Java beginners.
 */
public class App extends Application {

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 600;
    private static final double SIDEBAR_WIDTH = 180;

    private QuestionLoader questionLoader;
    private CodeRunner codeRunner;
    private ProgressManager progressManager;

    private QuestionTreeView questionTree;
    private WebView questionView;
    private CodeEditor codeEditor;
    private ResultsPanel resultsPanel;
    private Label progressLabel;

    private Question currentQuestion;
    private boolean darkMode = true;
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    private Stage primaryStage;
    private double lastDividerPosition = 0.5;

    @Override
    public void start(Stage stage) {
        // Load custom fonts
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 14);

        this.primaryStage = stage;
        // Initialize services
        questionLoader = new QuestionLoader();
        codeRunner = new CodeRunner();
        progressManager = new ProgressManager();

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
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Left sidebar with question tree
        sidebarContainer = createSidebar();
        root.setLeft(sidebarContainer);

        // Center area with split panes
        root.setCenter(createCenterArea());

        return root;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("main-menu-bar");

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Progress");
        saveItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.S,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> saveProgress());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.Q,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(e -> Platform.exit());

        fileMenu.getItems().addAll(saveItem, new SeparatorMenuItem(), exitItem);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        MenuItem clearCodeItem = new MenuItem("Clear Code");
        clearCodeItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.L,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        clearCodeItem.setOnAction(e -> clearCode());

        MenuItem resetCodeItem = new MenuItem("Reset to Starter");
        resetCodeItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.R,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        resetCodeItem.setOnAction(e -> resetToStarter());

        editMenu.getItems().addAll(clearCodeItem, resetCodeItem);

        // View Menu
        Menu viewMenu = new Menu("View");
        CheckMenuItem darkModeItem = new CheckMenuItem("Dark Mode");
        darkModeItem.setSelected(darkMode);
        darkModeItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.D,
                javafx.scene.input.KeyCombination.CONTROL_DOWN, javafx.scene.input.KeyCombination.SHIFT_DOWN));
        darkModeItem.setOnAction(e -> toggleDarkMode(darkModeItem.isSelected()));

        MenuItem toggleSidebarItem = new MenuItem("Toggle Sidebar");
        toggleSidebarItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.B,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        toggleSidebarItem.setOnAction(e -> toggleSidebar());

        viewMenu.getItems().addAll(darkModeItem, new SeparatorMenuItem(), toggleSidebarItem);

        viewMenu.getItems().add(new SeparatorMenuItem());

        MenuItem zoomInItem = new MenuItem("Zoom In");
        zoomInItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.EQUALS,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        zoomInItem.setOnAction(e -> {
            if (codeEditor != null)
                codeEditor.increaseFontSize();
        });

        MenuItem zoomOutItem = new MenuItem("Zoom Out");
        zoomOutItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.MINUS,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        zoomOutItem.setOnAction(e -> {
            if (codeEditor != null)
                codeEditor.decreaseFontSize();
        });

        MenuItem resetZoomItem = new MenuItem("Reset Zoom");
        resetZoomItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.DIGIT0,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        resetZoomItem.setOnAction(e -> {
            if (codeEditor != null)
                codeEditor.resetFontSize();
        });

        viewMenu.getItems().addAll(zoomInItem, zoomOutItem, resetZoomItem);

        // Run Menu
        Menu runMenu = new Menu("Run");
        MenuItem runTestsItem = new MenuItem("Run Tests");
        runTestsItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.F5));
        runTestsItem.setOnAction(e -> runTests());

        MenuItem submitItem = new MenuItem("Submit Solution");
        submitItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.ENTER,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        submitItem.setOnAction(e -> submitSolution());

        runMenu.getItems().addAll(runTestsItem, submitItem);

        // Categories Menu
        Menu categoriesMenu = new Menu("Categories");
        for (var category : questionLoader.getCategories()) {
            MenuItem catItem = new MenuItem(category.getDisplayName());
            catItem.setOnAction(e -> {
                if (!category.getQuestions().isEmpty()) {
                    questionTree.selectQuestion(category.getQuestions().get(0));
                }
            });
            categoriesMenu.getItems().add(catItem);
        }

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem showHintItem = new MenuItem("Show Hint");
        showHintItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.H,
                javafx.scene.input.KeyCombination.CONTROL_DOWN));
        showHintItem.setOnAction(e -> showHint());

        MenuItem keyboardShortcutsItem = new MenuItem("Keyboard Shortcuts");
        keyboardShortcutsItem.setAccelerator(new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.K,
                javafx.scene.input.KeyCombination.CONTROL_DOWN, javafx.scene.input.KeyCombination.SHIFT_DOWN));
        keyboardShortcutsItem.setOnAction(e -> showKeyboardShortcuts());

        MenuItem checkUpdatesItem = new MenuItem("Check for Updates...");
        checkUpdatesItem.setOnAction(e -> checkForUpdates(false));

        MenuItem aboutItem = new MenuItem("About BugGym");
        aboutItem.setOnAction(e -> showAbout());

        helpMenu.getItems().addAll(showHintItem, keyboardShortcutsItem, new SeparatorMenuItem(), checkUpdatesItem,
                aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, runMenu, categoriesMenu, helpMenu);
        return menuBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(SIDEBAR_WIDTH);
        sidebar.setPadding(new Insets(10));
        sidebar.getStyleClass().add("sidebar");

        Label sidebarTitle = new Label("📚 Questions");
        sidebarTitle.getStyleClass().add("sidebar-title");

        questionTree = new QuestionTreeView();
        questionTree.setCategories(questionLoader.getCategories());
        questionTree.setOnQuestionSelected(this::onQuestionSelected);
        VBox.setVgrow(questionTree, Priority.ALWAYS);

        // Progress label
        progressLabel = new Label();
        progressLabel.getStyleClass().add("progress-label");
        updateProgress();

        sidebar.getChildren().addAll(sidebarTitle, questionTree, progressLabel);

        return sidebar;
    }

    private void updateProgress() {
        if (progressLabel == null || questionLoader == null)
            return;
        long totalQuestions = questionLoader.getQuestions().size();
        long solvedQuestions = questionLoader.getQuestions().stream()
                .filter(Question::isSolved).count();
        progressLabel.setText("Progress: %d/%d solved".formatted(solvedQuestions, totalQuestions));
    }

    private SplitPane createCenterArea() {
        // Main split: Editor (Center) vs Info (Right)
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);
        mainSplit.setDividerPositions(0.5); // Equal split initially

        // --- Middle Column: Code Editor ---
        VBox editorColumn = new VBox(10);
        editorColumn.setPadding(new Insets(10));
        editorColumn.getStyleClass().add("code-container");

        Label editorLabel = new Label("💻 Your Solution:");
        editorLabel.getStyleClass().add("section-label");

        codeEditor = new CodeEditor();
        VBox.setVgrow(codeEditor, Priority.ALWAYS);

        // Button bar
        HBox buttonBar = createButtonBar();

        editorColumn.getChildren().addAll(editorLabel, codeEditor, buttonBar);

        // --- Right Column: Question & Results ---
        SplitPane rightColumn = new SplitPane();
        rightColumn.setOrientation(Orientation.VERTICAL);
        rightColumn.setDividerPositions(0.5);

        // Top: Question
        questionView = new WebView();
        questionView.getStyleClass().add("question-view");

        // Navigation bar
        Button prevBtn = new Button("← Prev");
        prevBtn.getStyleClass().add("action-button");
        prevBtn.setOnAction(e -> navigateQuestion(-1));

        Label questionNavLabel = new Label("📖 Question");
        questionNavLabel.getStyleClass().add("section-label");

        Button nextBtn = new Button("Next →");
        nextBtn.getStyleClass().add("action-button");
        nextBtn.setOnAction(e -> navigateQuestion(1));

        Region navSpacer1 = new Region();
        Region navSpacer2 = new Region();
        HBox.setHgrow(navSpacer1, Priority.ALWAYS);
        HBox.setHgrow(navSpacer2, Priority.ALWAYS);

        HBox navBar = new HBox(10, prevBtn, navSpacer1, questionNavLabel, navSpacer2, nextBtn);
        navBar.setAlignment(javafx.geometry.Pos.CENTER);
        navBar.setPadding(new Insets(5, 10, 5, 10));

        VBox questionContainer = new VBox(navBar, questionView);
        questionContainer.getStyleClass().add("question-container");
        VBox.setVgrow(questionView, Priority.ALWAYS);

        // Bottom: Results
        Label resultsLabel = new Label("📊 Results:");
        resultsLabel.getStyleClass().add("section-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button toggleBtn = new Button("▼");
        toggleBtn.getStyleClass().add("small-button");
        toggleBtn.setTooltip(new Tooltip("Minimize"));

        HBox resultsHeader = new HBox(10, resultsLabel, spacer, toggleBtn);
        resultsHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        resultsPanel = new ResultsPanel();
        VBox.setVgrow(resultsPanel, Priority.ALWAYS);

        VBox resultsBox = new VBox(5, resultsHeader, resultsPanel);
        resultsBox.setPadding(new Insets(10));
        VBox.setVgrow(resultsPanel, Priority.ALWAYS);
        resultsBox.getStyleClass().add("results-box");

        rightColumn.getItems().addAll(questionContainer, resultsBox);

        toggleBtn.setOnAction(e -> {
            boolean isExpanded = resultsPanel.isVisible();
            if (isExpanded) {
                // Collapse
                if (rightColumn.getDividerPositions().length > 0) {
                    lastDividerPosition = rightColumn.getDividerPositions()[0];
                }
                resultsPanel.setVisible(false);
                resultsPanel.setManaged(false);
                toggleBtn.setText("▲");
                toggleBtn.setTooltip(new Tooltip("Expand"));
                rightColumn.setDividerPositions(1.0);
            } else {
                // Expand
                resultsPanel.setVisible(true);
                resultsPanel.setManaged(true);
                toggleBtn.setText("▼");
                toggleBtn.setTooltip(new Tooltip("Minimize"));
                rightColumn.setDividerPositions(lastDividerPosition);
            }
        });

        // Assemble Main Split
        mainSplit.getItems().addAll(editorColumn, rightColumn);

        return mainSplit;
    }

    private HBox createButtonBar() {
        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        buttonBar.getStyleClass().add("button-bar");

        Button runButton = new Button("Run Tests");
        runButton.getStyleClass().addAll("action-button", "run-button");
        runButton.setOnAction(e -> runTests());

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().addAll("action-button", "submit-button");
        submitButton.setOnAction(e -> submitSolution());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("action-button");
        clearButton.setOnAction(e -> clearCode());

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("action-button");
        resetButton.setOnAction(e -> resetToStarter());

        Button hintButton = new Button("Hint");
        hintButton.getStyleClass().add("action-button");
        hintButton.setOnAction(e -> showHint());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBar.getChildren().addAll(runButton, submitButton, spacer, clearButton, resetButton, hintButton);

        return buttonBar;
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

    private Alert createStyledAlert(Alert.AlertType type, String title, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));

        return alert;
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

        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, "Congratulations!", "🎉 All tests passed!");
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
        for (var category : categories) {
            if (!category.getQuestions().isEmpty()) {
                Question first = category.getQuestions().get(0);
                questionTree.selectQuestion(first);
                break;
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = createStyledAlert(Alert.AlertType.ERROR, title, null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAbout() {
        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, "About BugGym", "BugGym: Love, Java & Bugs");

        String version = getAppVersion();

        Text t1 = new Text("""
                BugGym: A playground for Java beginners.
                Built with patience, curiosity,
                and an unreasonable amount of love.

                This app was crafted for someone special to make learning Java
                feel less scary, more fun, and a little bit magical.

                Inspired by love, shared with the community.

                Expect bugs.
                Fix them together.
                Learn something new every day.

                Version: %s

                Source: """.formatted(version));

        Hyperlink sourceLink = new Hyperlink("https://github.com/PhilixTheExplorer/bug-gym");
        sourceLink.setOnAction(e -> getHostServices().showDocument(sourceLink.getText()));

        Text t2 = new Text("""


                License: GPLv3

                Built with JavaFX 21,
                coffee, and a heart that never stops compiling
                """);

        TextFlow flow = new TextFlow(t1, sourceLink, t2);
        alert.getDialogPane().setContent(flow);

        alert.showAndWait();
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
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.github.com/repos/PhilixTheExplorer/bug-gym/releases/latest"))
                        .header("Accept", "application/vnd.github+json")
                        .GET().build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    String body = response.body();
                    int idx = body.indexOf("\"tag_name\":");
                    if (idx != -1) {
                        int start = body.indexOf('"', idx + 11) + 1;
                        int end = body.indexOf('"', start);
                        return body.substring(start, end).replaceFirst("^v", "");
                    }
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            String latest = task.getValue();
            String current = getAppVersion();
            if (latest == null) {
                if (!silent)
                    showInfo("Update Check", "Could not retrieve version info.");
            } else if ("dev".equals(current) || compareVersions(current, latest) < 0) {
                Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, "Update Available",
                        "A new version is available: v" + latest);

                Text t1 = new Text("You have v" + current + ".\nVisit ");
                Hyperlink link = new Hyperlink("https://github.com/PhilixTheExplorer/bug-gym/releases");
                link.setOnAction(evt -> getHostServices().showDocument(link.getText()));
                Text t2 = new Text(" to download.");

                TextFlow flow = new TextFlow(t1, link, t2);
                alert.getDialogPane().setContent(flow);

                alert.showAndWait();
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

    private int compareVersions(String v1, String v2) {
        String[] a = v1.split("\\."), b = v2.split("\\.");
        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int n1 = 0;
            int n2 = 0;
            try {
                n1 = i < a.length ? Integer.parseInt(a[i].replaceAll("\\D.*", "")) : 0;
            } catch (NumberFormatException ignored) {
            }
            try {
                n2 = i < b.length ? Integer.parseInt(b[i].replaceAll("\\D.*", "")) : 0;
            } catch (NumberFormatException ignored) {
            }

            if (n1 != n2)
                return Integer.compare(n1, n2);
        }
        return 0;
    }

    private void showInfo(String title, String message) {
        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, title, null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showKeyboardShortcuts() {
        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, "Keyboard Shortcuts", "⌨ Keyboard Shortcuts");

        alert.setContentText("""
                File:
                • Ctrl+S          Save Progress
                • Ctrl+Q          Exit

                Edit:
                • Ctrl+L          Clear Code
                • Ctrl+R          Reset to Starter

                View:
                • Ctrl+Shift+D    Toggle Dark Mode
                • Ctrl+B          Toggle Sidebar
                • Ctrl+=          Zoom In
                • Ctrl+-          Zoom Out
                • Ctrl+0          Reset Zoom

                Run:
                • F5              Run Tests
                • Ctrl+Enter      Submit Solution

                Help:
                • Ctrl+H          Show Hint
                • Ctrl+Shift+K    Keyboard Shortcuts
                """);
        alert.showAndWait();
    }

    private void saveProgress() {
        if (currentQuestion != null) {
            currentQuestion.setUserCode(codeEditor.getCode());
            progressManager.saveProgress(currentQuestion);
        }
    }

    private VBox sidebarContainer;
    private boolean sidebarVisible = true;

    private void toggleSidebar() {
        if (sidebarContainer != null) {
            sidebarVisible = !sidebarVisible;
            sidebarContainer.setVisible(sidebarVisible);
            sidebarContainer.setManaged(sidebarVisible);
        }
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
