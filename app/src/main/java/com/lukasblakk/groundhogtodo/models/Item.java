package com.lukasblakk.groundhogtodo.models;

/**
 * Created by lukas on 1/16/17.
 */

public class Item {
    public String text;
    public String dueDate;
    public String priority;
    public String repeat;

    public Item() {
        this.text = "";
        this.dueDate = "";
        this.priority = "";
        this.repeat = "";
    }

    public Item(String text) {
        this.text = text;
        this.dueDate = "";
        this.priority = "";
        this.repeat = "";
    }

    public Item(String text, String dueDate, String priority, String repeat) {
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
        this.repeat = repeat;
    }

}