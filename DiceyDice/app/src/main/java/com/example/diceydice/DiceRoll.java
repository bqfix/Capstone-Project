package com.example.diceydice;

import android.support.v4.util.Pair;

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
        //TODO Implement logic to roll Dice based off of the formula and return each roll, and the total.
        return new Pair<>("1+1", 2);
    }
}
