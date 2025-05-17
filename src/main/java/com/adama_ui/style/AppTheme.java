package com.adama_ui.style;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;

import java.util.prefs.Preferences;

public class AppTheme {

    public static final String GREEN = "#00A651";
    public static final String DARK = "#2B2B2B";
    public static final String GRAY = "#7A7373";
    public static final String WHITE = "#FFFFFF";
    public static final String LIGHT = "#F0F0F0";

    public static final String FONT_FAMILY = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";

    private static final Preferences prefs = Preferences.userRoot().node("com.adama_ui.theme");
    private static final String THEME_KEY = "darkMode";

    @Getter
    private static boolean darkMode = prefs.getBoolean(THEME_KEY, true); // por defecto: oscuro

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
        prefs.putBoolean(THEME_KEY, dark); // guardar estado en preferencias
    }

    public static String getThemePath() {
        return darkMode ? "/style/dark-theme.css" : "/style/light-theme.css";
    }

    public static void applyTheme(Scene scene) {
        String theme = AppTheme.class.getResource(getThemePath()).toExternalForm();
        scene.getStylesheets().removeIf(s -> s.contains("dark-theme.css") || s.contains("light-theme.css"));
        if (!scene.getStylesheets().contains(theme)) {
            scene.getStylesheets().add(theme);
        }
    }

    public static void applyThemeTo(Node node) {
        if (node == null) return;

        if (node.getScene() != null) {
            applyTheme(node.getScene());
        } else if (node instanceof Parent parent) {
            String theme = AppTheme.class.getResource(getThemePath()).toExternalForm();
            parent.getStylesheets().removeIf(s -> s.contains("dark-theme.css") || s.contains("light-theme.css"));
            if (!parent.getStylesheets().contains(theme)) {
                parent.getStylesheets().add(theme);
            }
        }
    }
}