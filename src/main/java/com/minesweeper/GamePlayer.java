package com.minesweeper;

import java.util.List;
import java.util.Scanner;

public class GamePlayer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter a board size: ");
        String size = scanner.nextLine();
        System.out.println("Please, enter a number of bombs: ");
        String bombs = scanner.nextLine();
        Board board = new Board(Integer.parseInt(size), Integer.parseInt(bombs));
        System.out.println("Board, internal state:");
        board.print();
        System.out.println("Board, game status:");
        board.showGameState();
        while (true) {
            System.out.println("Enter click coordinates, coma separated: ");
            String coords = scanner.nextLine();
            List<BoardCell> openCells = board.click(Integer.parseInt(coords.split(",")[0]),
                    Integer.parseInt(coords.split(",")[1]));
            System.out.print("Opened cells: ");
            openCells.forEach(c -> System.out.print("(" + c.getX() + "," + c.getY() + "):" + c.getAdjBombs() + "; "));
            System.out.println();
            board.showGameState();
        }
    }
}
