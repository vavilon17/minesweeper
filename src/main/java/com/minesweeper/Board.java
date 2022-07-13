package com.minesweeper;

import java.util.*;
import java.util.stream.Stream;

public class Board {

    private final int N;

    /**
     * This is an essential data structure which is designed the way it's convenient to check cells quickly
     * String key - is a convenient way to store coordinates of a cell as a single value, e.g. 0-0
     * BoardCell value - this is a class containing all necessary information about the cell and it's status
     */
    private final Map<String, BoardCell> cells;

    /**
     * We track here the cells that are already opened during the game
     */
    private final Set<String> boardOpenedCells = new HashSet<>();

    /**
     * Constructor which initializing the game board
     * @param N - board size
     * @param K - number of bombs need to be placed
     */
    public Board(int N, int K) {
        if (N <= 0 || K < 0 || K > N*N) {
            throw new IllegalArgumentException("Size of the board or number of bombs is incorrect");
        }
        this.N = N;
        this.cells = new HashMap<>(N*N);
        initBoard(K);
    }

    /**
     * Helper method, not included in requirements
     */
    public void print() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                String key = BoardCell.getKeyFromCoords(j, i);
                String c = cells.get(key).isBomb() ? " B" : " " + cells.get(key).getAdjBombs();
                System.out.print(c);
            }
            System.out.print("\n");
        }
    }

    /**
     * Helper method, not included in requirements
     */
    public void showGameState() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                String key = BoardCell.getKeyFromCoords(j, i);
                String c;
                if (boardOpenedCells.contains(key)) {
                    c = cells.get(key).isBomb() ? " B" : " " + cells.get(key).getAdjBombs();
                } else {
                    c = " X";
                }
                System.out.print(c);
            }
            System.out.print("\n");
        }
    }

    /**
     * Initializes the board which includes placing bombs along with the calculating states of all board cells
     * (adjacent bomb numbers)
     *
     * @param K - number of bombs
     *
     * Works with O(N^2) time complexity
     */
    private void initBoard(int K) {
        Set<String> bombKeys = BombGenerator.randomBombKeys(this.N, K);
        for (int  i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                String key = BoardCell.getKeyFromCoords(j, i);
                if (bombKeys.contains(key)) {
                    // placing a bomb
                    cells.put(key, new BoardCell(j, i,true, 0));
                } else {
                    // calculating adjacent bombs
                    int adjBombs = getAdjBombs(j, i, bombKeys);
                    cells.put(key, new BoardCell(j, i, false, adjBombs));
                }
            }
        }
    }

    /**
     * Imitates a click on a particular cell
     * Works with O(N^2) timing in the worst scenario
     *
     * @return a list of the opened cells
     */
    public List<BoardCell> click(int x, int y) {
        if (x < 0 || x >= N || y < 0 || y >= N) {
            throw new IllegalArgumentException("Click coordinates are incorrect");
        }
        String key = BoardCell.getKeyFromCoords(x, y);
        BoardCell cell = cells.get(key);
        List<BoardCell> openCells = new ArrayList<>();
        if (!cell.isOpen()) {
            if (cell.isBomb()) {
                // well, this is not actually an illegal state, but I decided to keep it fast-and-dirty since there
                // were no additional requirements for the GameOver-condition
                throw new IllegalStateException("Game over...");
            }
            // recursively open the cells with adjacent zero-bombs
            visitAndOpen(x, y, openCells);
        }
        return openCells;
    }

    /**
     * Calculates the number of the adjacent bombs around the cell
     * Always checks 8 locations around and since HasSet lookup is constant the overall complexity of the method is O(1)
     */
    private int getAdjBombs(int x, int y, Set<String> bombKeys) {
        return calcPositiveChecks(
                checkTopLeft(x, y, bombKeys),
                checkTop(x, y, bombKeys),
                checkTopRight(x, y, bombKeys),
                checkRight(x, y, bombKeys),
                checkBottomRight(x, y, bombKeys),
                checkBottom(x, y, bombKeys),
                checkBottomLeft(x, y, bombKeys),
                checkLeft(x, y, bombKeys)
        );
    }

    /**
     * Recursive call to check all the cells around zero-bombs one and open it
     * When visiting cells we ignore the ones already open for the performance optimization
     *
     * Works with O(N^2) timing in the worst scenario, if we need to visit all the board cells
     */
    private void visitAndOpen(int x, int y, List<BoardCell> openCells) {
        if (x >= 0 && x < N && y >= 0 && y < N) {
            String key = BoardCell.getKeyFromCoords(x, y);
            BoardCell cell = cells.get(key);
            if (!boardOpenedCells.contains(key) && !cell.isBomb()) {
                cell.setOpen(true);
                boardOpenedCells.add(key);
                openCells.add(cell);
                if (cell.getAdjBombs() == 0) {
                    visitAndOpen(x-1, y-1, openCells); // top left
                    visitAndOpen(x, y-1, openCells); // top
                    visitAndOpen(x+1, y-1, openCells); // top right
                    visitAndOpen(x+1, y, openCells); // right
                    visitAndOpen(x+1, y+1, openCells); // bottom right
                    visitAndOpen(x, y+1, openCells); // bottom
                    visitAndOpen(x-1, y+1, openCells); // bottom left
                    visitAndOpen(x-1, y, openCells); // left
                }
            }
        }
    }

    private boolean checkTopLeft(int x, int y, Set<String> bombKeys) {
        return x > 0 && y > 0 && bombKeys.contains(BoardCell.getKeyFromCoords(x-1, y-1));
    }

    private boolean checkTop(int x, int y, Set<String> bombKeys) {
        return y > 0 && bombKeys.contains(BoardCell.getKeyFromCoords(x, y-1));
    }

    private boolean checkTopRight(int x, int y, Set<String> bombKeys) {
        return x < N-1 && y > 0 && bombKeys.contains(BoardCell.getKeyFromCoords(x+1, y-1));
    }

    private boolean checkRight(int x, int y, Set<String> bombKeys) {
        return x < N-1 && bombKeys.contains(BoardCell.getKeyFromCoords(x+1, y));
    }

    private boolean checkBottomRight(int x, int y, Set<String> bombKeys) {
        return x < N-1 && y < N-1 && bombKeys.contains(BoardCell.getKeyFromCoords(x+1, y+1));
    }

    private boolean checkBottom(int x, int y, Set<String> bombKeys) {
        return y < N-1 && bombKeys.contains(BoardCell.getKeyFromCoords(x, y+1));
    }

    private boolean checkBottomLeft(int x, int y, Set<String> bombKeys) {
        return x > 0 && y < N-1 && bombKeys.contains(BoardCell.getKeyFromCoords(x-1, y+1));
    }

    private boolean checkLeft(int x, int y, Set<String> bombKeys) {
        return x > 0 && bombKeys.contains(BoardCell.getKeyFromCoords(x-1, y));
    }

    private int calcPositiveChecks(Boolean... checks) {
        // type cast is safe since we will for sure have int-fit value here
        return (int) Stream.of(checks).filter(c -> c).count();
    }

}