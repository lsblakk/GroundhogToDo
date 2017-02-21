package com.lukasblakk.groundhogtodo.models;

/**
 * Created by lukas on 1/16/17.
 */

public class Item {
    public String text;
    public String dueDate;

    public Item() {
        this.text = "";
        this.dueDate = "today";
    }

    public Item(String text) {
        this.text = text;
        this.dueDate = "today";
    }

    public Item(String text, String dueDate) {
        this.text = text;
        this.dueDate = dueDate;
    }

}