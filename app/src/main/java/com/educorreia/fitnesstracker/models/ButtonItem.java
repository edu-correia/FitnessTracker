package com.educorreia.fitnesstracker.models;

public class ButtonItem {
    private int title;
    private int id;
    private int drawableId;
    private int color;

    public ButtonItem(int id, int title, int drawableId, int color) {
        this.title = title;
        this.id = id;
        this.drawableId = drawableId;
        this.color = color;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
