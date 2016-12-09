package com.compass.ashrith.swipeablerecyclerview.model;

/**
 * Created by compass on 24/11/16.
 */

public class ItemModel {
    String text;
    public boolean isItem;
    public boolean isUndo;
    public boolean isUndoIgnore;

    public boolean isUndoIgnore() {
        return isUndoIgnore;
    }

    public void setUndoIgnore(boolean undoIgnore) {
        isUndoIgnore = undoIgnore;
    }

    public boolean isItem() {
        return isItem;
    }

    public void setItem(boolean item) {
        isItem = item;
    }

    public boolean isUndo() {
        return isUndo;
    }

    public void setUndo(boolean undo) {
        isUndo = undo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
