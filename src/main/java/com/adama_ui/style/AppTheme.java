package com.adama_ui.style;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

    private static boolean darkMode = prefs.getBoolean(THEME_KEY, true); // por defecto: oscuro

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
        prefs.putBoolean(THEME_KEY, dark); // 🔐 guardamos el valor
    }

    public static String getThemePath() {
        return darkMode ? "/style/dark-theme.css" : "/style/light-theme.css";
    }

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(AppTheme.class.getResource(getThemePath()).toExternalForm());
    }

    public static void applyThemeTo(Node node) {
        if (node == null) return;

        if (node.getScene() != null) {
            // ✅ Aplica el CSS global a la escena SIN borrarlo antes
            var stylesheets = node.getScene().getStylesheets();
            String theme = AppTheme.class.getResource(getThemePath()).toExternalForm();
            if (!stylesheets.contains(theme)) {
                stylesheets.add(theme);
            }
        } else if (node instanceof Parent parent) {
            // Para nodos independientes aún no añadidos a escena
            parent.getStylesheets().clear();
            parent.getStylesheets().add(AppTheme.class.getResource(getThemePath()).toExternalForm());
        }
    }

}
