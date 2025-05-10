package com.adama_ui.style;

public class AppTheme {
    public static final String GREEN = "#00A651";
    public static final String DARK = "#2B2B2B";
    public static final String GRAY = "#7A7373";
    public static final String WHITE = "#FFFFFF";
    public static final String LIGHT = "#F0F0F0";

    public static final String FONT_FAMILY = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";

    public static String getThemePath(boolean darkMode) {
        return darkMode ? "/style/dark-theme.css" : "/style/light-theme.css";
    }
}
