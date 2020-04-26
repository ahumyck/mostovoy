package com.mostovoy_company.controllers;

public enum GridSize {
    SIZE_10x10(10),
    SIZE_30x30(30),
    SIZE_50x50(50),
    SIZE_100x100(100);

    private final int value;

    GridSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}