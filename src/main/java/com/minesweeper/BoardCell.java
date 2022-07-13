package com.minesweeper;

public class BoardCell {

    public static final String KEY_DELIMITER = "-";

    private final int x;
    private final int y;
    /**
     * Represents a cell state - if it's already open or not yet
     */
    private boolean isOpen = false;
    private final boolean isBomb;
    /**
     * Keeps an information about the number of bombs for all adjacent cells around the current one. 0 for bombs
     */
    private final int adjBombs;

    public BoardCell(int x, int y, boolean isBomb, int adjBombs) {
        this.x = x;
        this.y = y;
        this.isBomb = isBomb;
        this.adjBombs = adjBombs;
    }

    /**
     * Helper method to provide a key out of the pair of coordinates
     */
    public static String getKeyFromCoords(int x, int y) {
        return x + KEY_DELIMITER + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public int getAdjBombs() {
        return adjBombs;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
