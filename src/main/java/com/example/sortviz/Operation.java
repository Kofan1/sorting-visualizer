package com.example.sortviz;


public record Operation(Type type, int i, int j, int value) {
    public enum Type {SWAP, SET, HILITE, CLEAR_HILITE}


    public static Operation swap(int i, int j) {
        return new Operation(Type.SWAP, i, j, 0);
    }

    public static Operation set(int i, int value) {
        return new Operation(Type.SET, i, -1, value);
    }

    public static Operation hilite(int i, int j) {
        return new Operation(Type.HILITE, i, j, 0);
    }

    public static Operation clearHilite() {
        return new Operation(Type.CLEAR_HILITE, -1, -1, 0);
    }
}