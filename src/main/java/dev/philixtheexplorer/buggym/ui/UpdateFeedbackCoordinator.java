package dev.philixtheexplorer.buggym.ui;

import java.util.function.Consumer;

/**
 * Handles update-check result presentation for the UI.
 */
public class UpdateFeedbackCoordinator {

    public void showVersionUnavailable(
            Class<?> resourceOwner,
            boolean silent
    ) {
        if (!silent) {
            AppDialogs.showInfo(resourceOwner, "Update Check", "Could not retrieve version info.");
        }
    }

    public void showUpdateAvailable(
            Class<?> resourceOwner,
            String currentVersion,
            String latestVersion,
            Consumer<String> openUrl
    ) {
        AppDialogs.showUpdateAvailable(resourceOwner, currentVersion, latestVersion, openUrl);
    }

    public void showUpToDate(Class<?> resourceOwner, boolean silent, String currentVersion) {
        if (!silent) {
            AppDialogs.showInfo(resourceOwner, "Up to Date",
                    "You are running the latest version (v" + currentVersion + ").");
        }
    }

    public void handleFailure(Class<?> resourceOwner, boolean silent) {
        if (!silent) {
            AppDialogs.showInfo(resourceOwner, "Update Check", "Could not check for updates.");
        }
    }
}
