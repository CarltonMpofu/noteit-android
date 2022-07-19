package com.clbmdev.noteit;

import android.graphics.Color;

public class Note
{
    private int id;
    private String title;
    private String note;
    private int color;
    private int backgroundColor;

    public Note(String note, int color, int backgroundColor) {
        this.id = id;
        this.note = note;
        this.color = color;
        this.backgroundColor = backgroundColor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getID()
    {
        return id;
    }

    public void setID(int id)
    {
        this.id = id;
    }
}
