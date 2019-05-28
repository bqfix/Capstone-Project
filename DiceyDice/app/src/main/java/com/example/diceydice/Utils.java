package com.example.diceydice;

import android.support.v4.util.Pair;

import java.util.Random;

public final class Utils {

    private Utils() {
    } //Private Constructor to prevent instantiation

    /** A helper method to return randomized rolls given a number of dice and their value
     *
     * @param numberOfDice
     * @param dieSize
     * @return a Pair including a String that details each roll, and an Integer that holds the total
     */
    public static Pair<String, Integer> calculateDice(int numberOfDice, int dieSize) {
        Random randomizer = new Random();
        StringBuilder compiledRolls = new StringBuilder("");
        int total = 0;

        if (numberOfDice == 0 || dieSize == 0)
            return new Pair<>(compiledRolls.toString(), total); //Return early if answer will be 0

        int firstRoll = randomizer.nextInt(dieSize) + 1;  //Add and append the first value, and add to total
        compiledRolls.append("(").append(firstRoll);
        total += firstRoll;

        for (int currentDieNumber = 2; currentDieNumber <= numberOfDice; currentDieNumber ++){ //Iterate through a number of times equal to the remaining dice, repeating the append and adding to total
            int roll = randomizer.nextInt(dieSize) + 1;
            compiledRolls.append(" + ").append(roll);
            total += roll;
        }

        compiledRolls.append(")"); //Append closing bracket to compiledRolls and return
        return new Pair<>(compiledRolls.toString(), total);
    }
}
