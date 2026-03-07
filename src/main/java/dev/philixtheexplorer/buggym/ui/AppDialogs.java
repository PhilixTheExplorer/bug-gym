package dev.philixtheexplorer.buggym.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Common dialog builders and preconfigured app dialogs.
 */
public final class AppDialogs {

    private AppDialogs() {
    }

    public static Alert createStyledAlert(Class<?> resourceOwner, Alert.AlertType type, String title, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        ImageView icon = new ImageView(new Image(resourceOwner.getResourceAsStream("/icons/bug-gym.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(resourceOwner.getResourceAsStream("/icons/bug-gym.png")));

        return alert;
    }

    public static void showInfo(Class<?> resourceOwner, String title, String message) {
        Alert alert = createStyledAlert(resourceOwner, Alert.AlertType.INFORMATION, title, null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showError(Class<?> resourceOwner, String title, String message) {
        Alert alert = createStyledAlert(resourceOwner, Alert.AlertType.ERROR, title, null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showKeyboardShortcuts(Class<?> resourceOwner) {
        Alert alert = createStyledAlert(resourceOwner, Alert.AlertType.INFORMATION, "Keyboard Shortcuts", "⌨ Keyboard Shortcuts");

        alert.setContentText("""
                File:
                • Ctrl+S          Save Progress
                • Ctrl+Q          Exit

                Edit:
                • Ctrl+L          Clear Code
                • Ctrl+R          Reset to Starter

                View:
                • Ctrl+Shift+D    Toggle Dark Mode
                • Ctrl+1          Home
                • Ctrl+2          Practice Workspace
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

    public static void showAbout(Class<?> resourceOwner, String version, Consumer<String> openUrl) {
        Alert alert = createStyledAlert(resourceOwner, Alert.AlertType.INFORMATION, "About BugGym", "BugGym: Love, Java & Bugs");

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
        sourceLink.setOnAction(e -> openUrl.accept(sourceLink.getText()));

        Text t2 = new Text("""


                License: GPLv3

                Built with JavaFX 21,
                coffee, and a heart that never stops compiling
                """);

        TextFlow flow = new TextFlow(t1, sourceLink, t2);
        alert.getDialogPane().setContent(flow);

        alert.showAndWait();
    }

    public static void showUpdateAvailable(
            Class<?> resourceOwner,
            String currentVersion,
            String latestVersion,
            Consumer<String> openUrl
    ) {
        Alert alert = createStyledAlert(resourceOwner, Alert.AlertType.INFORMATION, "Update Available",
                "A new version is available: v" + latestVersion);

        Text t1 = new Text("You have v" + currentVersion + ".\nVisit ");
        Hyperlink link = new Hyperlink("https://github.com/PhilixTheExplorer/bug-gym/releases");
        link.setOnAction(evt -> openUrl.accept(link.getText()));
        Text t2 = new Text(" to download.");

        TextFlow flow = new TextFlow(t1, link, t2);
        alert.getDialogPane().setContent(flow);

        alert.showAndWait();
    }
}
