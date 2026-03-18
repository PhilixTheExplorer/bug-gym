package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.Category;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Collection;
import java.util.function.Consumer;

public final class MainMenuBarFactory {

    private MainMenuBarFactory() {
    }

    public record Actions(
            Runnable onSaveProgress,
            Runnable onExit,
            Runnable onClearCode,
            Runnable onResetToStarter,
            Consumer<Boolean> onToggleDarkMode,
            Consumer<Boolean> onToggleAutoIndent,
            Consumer<Boolean> onToggleAutoBracketPairing,
            Runnable onShowHome,
            Runnable onShowPractice,
            Runnable onToggleSidebar,
            Runnable onZoomIn,
            Runnable onZoomOut,
            Runnable onResetZoom,
            Runnable onRunTests,
            Runnable onSubmit,
            Consumer<Category> onOpenCategory,
            Runnable onShowHint,
            Runnable onShowKeyboardShortcuts,
            Runnable onCheckUpdates,
            Runnable onShowAbout) {
    }

    public static MenuBar create(Collection<Category> categories, boolean darkMode, Actions actions) {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("main-menu-bar");

        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Progress");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> actions.onSaveProgress().run());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(e -> actions.onExit().run());
        fileMenu.getItems().addAll(saveItem, new SeparatorMenuItem(), exitItem);

        Menu editMenu = new Menu("Edit");
        MenuItem clearCodeItem = new MenuItem("Clear Code");
        clearCodeItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        clearCodeItem.setOnAction(e -> actions.onClearCode().run());

        MenuItem resetCodeItem = new MenuItem("Reset to Starter");
        resetCodeItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        resetCodeItem.setOnAction(e -> actions.onResetToStarter().run());
        editMenu.getItems().addAll(clearCodeItem, resetCodeItem);

        Menu viewMenu = new Menu("View");
        CheckMenuItem darkModeItem = new CheckMenuItem("Dark Mode");
        darkModeItem.setSelected(darkMode);
        darkModeItem.setAccelerator(new KeyCodeCombination(KeyCode.D,
                KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN));
        darkModeItem.setOnAction(e -> actions.onToggleDarkMode().accept(darkModeItem.isSelected()));

        MenuItem toggleSidebarItem = new MenuItem("Toggle Sidebar");
        toggleSidebarItem.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        toggleSidebarItem.setOnAction(e -> actions.onToggleSidebar().run());

        MenuItem homeItem = new MenuItem("Home");
        homeItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN));
        homeItem.setOnAction(e -> actions.onShowHome().run());

        MenuItem practiceItem = new MenuItem("Practice Workspace");
        practiceItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN));
        practiceItem.setOnAction(e -> actions.onShowPractice().run());

        viewMenu.getItems().addAll(darkModeItem, new SeparatorMenuItem(), homeItem, practiceItem,
                new SeparatorMenuItem(), toggleSidebarItem);
        viewMenu.getItems().add(new SeparatorMenuItem());

        MenuItem zoomInItem = new MenuItem("Zoom In");
        zoomInItem.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN));
        zoomInItem.setOnAction(e -> actions.onZoomIn().run());

        MenuItem zoomOutItem = new MenuItem("Zoom Out");
        zoomOutItem.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN));
        zoomOutItem.setOnAction(e -> actions.onZoomOut().run());

        MenuItem resetZoomItem = new MenuItem("Reset Zoom");
        resetZoomItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.CONTROL_DOWN));
        resetZoomItem.setOnAction(e -> actions.onResetZoom().run());

        viewMenu.getItems().addAll(zoomInItem, zoomOutItem, resetZoomItem);

        Menu settingsMenu = new Menu("Settings");
        CheckMenuItem autoIndentItem = new CheckMenuItem("Auto Indent");
        autoIndentItem.setSelected(true);
        autoIndentItem.setOnAction(e -> actions.onToggleAutoIndent().accept(autoIndentItem.isSelected()));

        CheckMenuItem autoBracketPairingItem = new CheckMenuItem("Auto Bracket Pairing");
        autoBracketPairingItem.setSelected(true);
        autoBracketPairingItem.setOnAction(
                e -> actions.onToggleAutoBracketPairing().accept(autoBracketPairingItem.isSelected()));

        settingsMenu.getItems().addAll(autoIndentItem, autoBracketPairingItem);

        Menu runMenu = new Menu("Run");
        MenuItem runTestsItem = new MenuItem("Run Tests");
        runTestsItem.setAccelerator(new KeyCodeCombination(KeyCode.F5));
        runTestsItem.setOnAction(e -> actions.onRunTests().run());

        MenuItem submitItem = new MenuItem("Submit Solution");
        submitItem.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN));
        submitItem.setOnAction(e -> actions.onSubmit().run());
        runMenu.getItems().addAll(runTestsItem, submitItem);

        Menu categoriesMenu = new Menu("Categories");
        for (Category category : categories) {
            MenuItem catItem = new MenuItem(category.getDisplayName());
            catItem.setOnAction(e -> actions.onOpenCategory().accept(category));
            categoriesMenu.getItems().add(catItem);
        }

        Menu quickHomeMenu = new Menu("Home");
        quickHomeMenu.setOnShowing(e -> {
            actions.onShowHome().run();
            Platform.runLater(quickHomeMenu::hide);
        });

        Menu helpMenu = new Menu("Help");
        MenuItem showHintItem = new MenuItem("Show Hint");
        showHintItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        showHintItem.setOnAction(e -> actions.onShowHint().run());

        MenuItem keyboardShortcutsItem = new MenuItem("Keyboard Shortcuts");
        keyboardShortcutsItem.setAccelerator(new KeyCodeCombination(KeyCode.K,
                KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN));
        keyboardShortcutsItem.setOnAction(e -> actions.onShowKeyboardShortcuts().run());

        MenuItem checkUpdatesItem = new MenuItem("Check for Updates...");
        checkUpdatesItem.setOnAction(e -> actions.onCheckUpdates().run());

        MenuItem aboutItem = new MenuItem("About BugGym");
        aboutItem.setOnAction(e -> actions.onShowAbout().run());

        helpMenu.getItems().addAll(showHintItem, keyboardShortcutsItem, new SeparatorMenuItem(), checkUpdatesItem,
                aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, settingsMenu, runMenu, categoriesMenu, quickHomeMenu,
                helpMenu);
        return menuBar;
    }
}
