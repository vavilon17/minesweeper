package com.minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The class is used for randomly and uniformly generating K bomb locations across the board with size N
 */
public class BombGenerator {

    /**
     *
     * @param N - board size
     * @param K - number of bombs needed to be placed
     * @return set of the location keys for the bombs. Location key is formed as a string "x-y"
     *
     * The logic inside generates unique random locations of the bombs.
     * Uniform distribution of the randomness is guaranteed by Random class.
     * To make locations unique: for each step we first form an array of all board locations and then randomly choose an
     * element out of the array, swap the retrieved location key with the currently last one and shift left upper bound
     * of our random range - thus we keep already retrieved locations out of the range available for random generator
     * and at the same time we guarantee all locations are equally probably for the bombs
     *
     * Works with O(N^2) time complexity
     */
    public static Set<String> randomBombKeys(int N, int K) {
        String[] keys = new String[N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                keys[i*N + j] = i + BoardCell.KEY_DELIMITER + j;
            }
        }
        Random random = new Random();
        int currentUpperBound = N*N;
        Set<String> bombIndices = new HashSet<>(K);
        for (int i = 0; i < K; i++) {
            int randomValue = random.nextInt(currentUpperBound);
            bombIndices.add(keys[randomValue]);
            currentUpperBound--;
            swap(keys, randomValue, currentUpperBound);
        }
        return bombIndices;
    }

    private static void swap(String[] arr, int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
