package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.ui.MainMenuBarFactory;
import dev.philixtheexplorer.buggym.ui.MainWorkspacePane;
import javafx.application.Platform;
import javafx.scene.control.MenuBar;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Assembles UI interaction wiring for menu and workspace components.
 */
final class AppInteractionFactory {

    private AppInteractionFactory() {
    }

    static MenuBar createMenuBar(Collection<Category> categories, boolean darkMode, Callbacks callbacks) {
        return MainMenuBarFactory.create(categories, darkMode,
                new MainMenuBarFactory.Actions(
                        callbacks.onSaveProgress(),
                        Platform::exit,
                        callbacks.onClearCode(),
                        callbacks.onResetToStarter(),
                        callbacks.onToggleDarkMode(),
                        callbacks.onToggleAutoIndent(),
                        callbacks.onToggleAutoBracketPairing(),
                        callbacks.onShowHome(),
                        callbacks.onShowPractice(),
                        callbacks.onToggleSidebar(),
                        callbacks.onZoomIn(),
                        callbacks.onZoomOut(),
                        callbacks.onResetZoom(),
                        callbacks.onRunTests(),
                        callbacks.onSubmitSolution(),
                        callbacks.onOpenCategoryFromMenu(),
                        callbacks.onShowHint(),
                        callbacks.onShowKeyboardShortcuts(),
                        callbacks.onCheckForUpdates(),
                        callbacks.onShowAbout()));
    }

    static MainWorkspacePane createWorkspacePane(Collection<Category> categories, Callbacks callbacks) {
        return new MainWorkspacePane(
                categories,
                callbacks.onQuestionSelected(),
                callbacks.onNavigateQuestion(),
                callbacks.onRunTests(),
                callbacks.onSubmitSolution(),
                callbacks.onClearCode(),
                callbacks.onResetToStarter(),
                callbacks.onShowHint());
    }

    record Callbacks(
            Runnable onSaveProgress,
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
            Runnable onSubmitSolution,
            Consumer<Category> onOpenCategoryFromMenu,
            Runnable onShowHint,
            Runnable onShowKeyboardShortcuts,
            Runnable onCheckForUpdates,
            Runnable onShowAbout,
            Consumer<Question> onQuestionSelected,
            Consumer<Integer> onNavigateQuestion) {
    }
}
