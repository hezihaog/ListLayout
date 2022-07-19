package com.cosleep.listlayout.model;

public class NumberModel {
    private Number value;

    public NumberModel(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NumberModel{" +
                "value=" + value +
                '}';
    }
}