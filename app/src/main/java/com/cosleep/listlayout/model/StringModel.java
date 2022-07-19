package com.cosleep.listlayout.model;

public class StringModel {
    private String value;

    public StringModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringModel{" +
                "value='" + value + '\'' +
                '}';
    }
}