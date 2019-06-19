package com.example.diceydice;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
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
        String[] splitFormulaByPlusMinus = formula.trim().split("[+-]", -1); //Split the formula by + and -, -1 limit forces inclusion of empty sections from excessive + or -
        for (String splitSection : splitFormulaByPlusMinus) {
            String[] splitFormulaByD = splitSection.trim().split("[dD]", -1); //Further split each section by whether there is a d or D, -1 limit provided to force inclusion of empty strings for subsequent length parsing (in the event of multiple ds)
            switch (splitFormulaByD.length) { //Each section should only be 2 numbers long (meaning the numbers can be multiplied) or 1 number long
                case 2 : {
                    for (String potentialNumber : splitFormulaByD) { //Confirm that each number is valid
                        try {
                            int number = Integer.parseInt(potentialNumber.trim());
                            if (number > Constants.MAX_DIE_SIZE) {
                                return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                            }
                        } catch (NumberFormatException e) {
                            return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                        }
                    }
                    totalDice += Integer.parseInt(splitFormulaByD[0].trim());  //If both numbers were valid, add the first number (which will be the number of dice) to totalDice
                    break;
                }
                case 1 : {
                    try { //Confirm that the number is valid
                        int number = Integer.parseInt(splitFormulaByD[0].trim());
                        if (number > Constants.MAX_DIE_SIZE) {
                            return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                        }
                    } catch (NumberFormatException e) {
                        return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
                    }
                    break;
                }
                default : return new Pair<>(false, context.getString(R.string.incorrectly_formatted_section));
            }
        }
        if (totalDice >= Constants.MAX_DICE_PER_ROLL) { //This is to prevent exceptionally large rolls that may lock down the app
            return new Pair<>(false, context.getString(R.string.too_many_dice));
        }
        return new Pair<>(true, context.getString(R.string.no_error));
    }

    /** Temporary helper method to provide a list of fake data to test RecyclerViews, etc.
     *
     * @return a List of DiceRolls
     */
    public static ArrayList<DiceRoll> getDiceRollFakeData(){
        ArrayList<DiceRoll> diceRolls = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            diceRolls.add(new DiceRoll("Standard Die Plus One", "1d6 + 1"));
        }
        return diceRolls;
    }

    /** Temporary helper method to provide a list of fake DiceResults data to test RecyclerViews, etc.
     *
     * @return a List of DiceResults
     */
    public static ArrayList<DiceResults> getDiceResultsFakeData(){
        ArrayList<DiceResults> diceResults = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            diceResults.add(new DiceResults("Standard Roll","1d6 + 1 = +(6) + (1)", 7));
        }
        return diceResults;
    }

    /** A helper method to read SharedPrefs and return the latest DiceResults from it
     *
     * @param context used to access SharedPrefs
     * @return the most recent DiceResults
     */
    public static DiceResults retrieveLatestDiceResults(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);

        String name = sharedPreferences.getString(context.getString(R.string.dice_results_name_key), "");
        String descrip = sharedPreferences.getString(context.getString(R.string.dice_results_descrip_key), "");
        int total = sharedPreferences.getInt(context.getString(R.string.dice_results_total_key), 0);
        long date = sharedPreferences.getLong(context.getString(R.string.dice_results_date_key), 0);

        return new DiceResults(name, descrip, total, date);
    }

    /**
     * A helper method for saving to the Firebase Realtime Database's history section
     * @param databaseReference to be saved to
     * @param userId to save under
     * @param diceResults to be saved
     */
    public static void saveToFirebaseHistory(DatabaseReference databaseReference, String userId, DiceResults diceResults){
        databaseReference.child(Constants.FIREBASE_DATABASE_HISTORY_PATH).child(userId).push().setValue(diceResults);
    }
}
