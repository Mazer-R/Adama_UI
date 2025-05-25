package com.adama_ui.style;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.function.Function;

public class GenericCellFactory<T> implements Callback<ListView<T>, ListCell<T>> {
    private final Function<T, Node> renderer;

    public GenericCellFactory(Function<T, Node> renderer) {
        this.renderer = renderer;
    }

    @Override
    public ListCell<T> call(ListView<T> listView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : renderer.apply(item));
            }
        };
    }
}
