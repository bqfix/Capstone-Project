package com.example.diceydice;

import android.content.Context;
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

    /** A helper method that checks if a given formula will be valid, should be called before creating or editing DiceRolls' formulas
     *
     * @param context used to access string resources
     * @param formula to be checked
     * @return a Pair containing a Boolean of whether the formula is valid or not, and an error message to be used if it is not.
     */
    public static Pair<Boolean, String> isValidDiceRoll(Context context, String formula){
        if (!formula.matches("[0123456789dD +-]+")){ //Check that the formula contains exclusively numbers, d, D, or +/-
            return new Pair<>(false, context.getString(R.string.invalid_characters));
        }

        int totalDice = 0;
        String[] splitFormulaByPlusMinus = formula.trim().split("[+-]"); //Split the formula by + and -
        for (String splitSection : splitFormulaByPlusMinus) {
            String[] splitFormulaByD = splitSection.trim().split("[dD]", -1); //Further split each section by whether there is a d or D, -1 limit provided to force inclusion of empty strings for subsequent length parsing (in the event of multiple ds)
            switch (splitFormulaByD.length) { //Each section should only be 2 numbers long (meaning the numbers can be multiplied) or 1 number long
                case 2 : {
                    for (String potentialNumber : splitFormulaByD) { //Confirm that each number is valid
                        try {
                            Integer.parseInt(potentialNumber.trim());
                        } catch (NumberFormatException e) {
                            return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                        }
                    }
                    totalDice += Integer.parseInt(splitFormulaByD[0].trim());  //If both numbers were valid, add the first number (which will be the number of dice) to totalDice
                    break;
                }
                case 1 : {
                    try { //Confirm that the number is valid
                        Integer.parseInt(splitFormulaByD[0].trim());
                    } catch (NumberFormatException e) {
                        return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                    }
                    break;
                }
                default : return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
            }
        }
        if (totalDice >= 1000) { //This is to prevent exceptionally large rolls that may lock down the app
            return new Pair<>(false, context.getString(R.string.too_many_dice));
        }
        return new Pair<>(true, context.getString(R.string.no_error));
    }
}
