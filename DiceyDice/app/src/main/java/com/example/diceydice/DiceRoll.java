package com.example.diceydice;

import android.support.v4.util.Pair;

import java.util.ArrayList;

public class DiceRoll {

    private String mName;
    private String mFormula;

    public DiceRoll(String formula) {
        mName = "";
        mFormula = formula;
    }

    public DiceRoll(String name, String formula) {
        mName = name;
        mFormula = formula;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getFormula() {
        return mFormula;
    }

    public void setFormula(String mFormula) {
        this.mFormula = mFormula;
    }

    /** A method that randomizes numbers based off of the DiceRoll's formula
     *
     * @return A pair, with the first value being a string containing each individual roll, and the second being the total.
     */
    public Pair<String, Integer> roll(){
        //TODO Adjust to handle negative values correctly
        StringBuilder compiledRolls = new StringBuilder("");
        int total = 0;
        ArrayList<String[]> splitFormulaByD =  new ArrayList<>();
        ArrayList<String> plussesAndMinuses = extractPlussesAndMinuses();

        String[] splitFormulaByPlusMinus = mFormula.trim().split("[+-]"); //Split formula based on + and -
        for (String section : splitFormulaByPlusMinus) {
            String trimmedSection = section.trim();
            splitFormulaByD.add(trimmedSection.split("[dD]")); //Add each trimmed section to the ArrayList, splitting it by d or D if applicable
        }

        for (int index = 0; index < splitFormulaByD.size(); index++) {
            String[] splitRoll = splitFormulaByD.get(index);
            boolean positive = plussesAndMinuses.get(index).equals("+"); //Cross reference with plussesAndMinuses to determine if positive or negative

            if (splitRoll.length == 3) { //If the size is 3, it was delineated by d or D, and thus we know that the first value is the numberOfDice, and the third value is the dieSize
                int numberOfDice = Integer.parseInt(splitRoll[0]);
                int dieSize = Integer.parseInt(splitRoll[2]);
                Pair<String, Integer> rolledValues = Utils.calculateDice(numberOfDice, dieSize); //Use the utils method to calculate a Pair with the individual values, and the total, and append/total these

                if (positive) { //Add or subtract accordingly
                    compiledRolls.append(rolledValues.first).append(" +");
                    total += rolledValues.second;
                } else { //Negative
                    compiledRolls.append(rolledValues.first).append(" -");
                    total -= rolledValues.second;
                }
            }

            if (splitRoll.length == 1){ //If the length is one, simply append the number and add or subtract accordingly
                String number = splitRoll[0];
                if (positive) {
                    compiledRolls.append("+(").append(number).append(")");
                    total += Integer.parseInt(number);
                } else { //Negative
                    compiledRolls.append("-(").append(number).append(")");
                    total -= Integer.parseInt(number);
                }
            }
        }
        return new Pair<>(compiledRolls.toString(), total);
    }

    /** A helper method to create an ArrayList containing all the plusses and minuses in the formula, for later cross-referencing
     *
     * @return an ArrayList of all the plusses and minuses in the formula
     */
    private ArrayList<String> extractPlussesAndMinuses(){
        ArrayList<String> extractedPlussesAndMinuses = new ArrayList<>();
        extractedPlussesAndMinuses.add("+"); //First value in formula must always be positive
        for (int index = 0; index < mFormula.length(); index++) {
            String currentCharacter = String.valueOf(mFormula.charAt(index));
            if (currentCharacter.equals("+") || currentCharacter.equals("-")) { //If a character in the formula is + or -, append it to the list
                extractedPlussesAndMinuses.add(currentCharacter);
            }
        }
        return extractedPlussesAndMinuses;
    }
}
