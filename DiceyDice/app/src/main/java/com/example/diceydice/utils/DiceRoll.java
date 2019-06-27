package com.example.diceydice.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiceRoll implements Parcelable {

    private String mName;
    private String mFormula;

    public DiceRoll(){} //No argument constructor for deserializing from Firebase

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

    public void setName(String name) {
        mName = name;
    }

    public String getFormula() {
        return mFormula;
    }

    public void setFormula(String formula) {
        mFormula = formula;
    }

    /** A method that randomizes numbers based off of the DiceRoll's formula.
     *
     * @param context used for saving the results
     * @return A pair, with the first value being a string containing each individual roll, and the second being the total.
     */
    public DiceResults roll(Context context){
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

            if (splitRoll.length == 2) { //If the size is 2, it was delineated by d or D, and thus we know that the first value is the numberOfDice, and the second value is the dieSize
                int numberOfDice = Integer.parseInt(splitRoll[0].trim());
                int dieSize = Integer.parseInt(splitRoll[1].trim());
                Pair<String, Integer> rolledValues = Utils.calculateDice(numberOfDice, dieSize); //Use the utils method to calculate a Pair with the individual values, and the total, and append/total these

                if (positive) { //Add or subtract accordingly
                    compiledRolls.append(" +").append(rolledValues.first);
                    total += rolledValues.second;
                } else { //Negative
                    compiledRolls.append(" -").append(rolledValues.first);
                    total -= rolledValues.second;
                }
            }

            if (splitRoll.length == 1){ //If the length is one, simply append the number and add or subtract accordingly
                String number = splitRoll[0].trim();
                if (positive) {
                    compiledRolls.append("+(").append(number).append(")");
                    total += Integer.parseInt(number);
                } else { //Negative
                    compiledRolls.append("-(").append(number).append(")");
                    total -= Integer.parseInt(number);
                }
            }
        }

        //Create a description of the formula, along with the compiled rolls
        String descrip = mFormula + "=\n\n" + compiledRolls.toString();

        //Create a new DiceResults object and save it
        DiceResults diceResults = new DiceResults(mName, descrip, total);

        diceResults.saveToSharedPreferences(context);

        return diceResults;
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

    /**
     * A helper method to save a DiceRoll to Firebase Realtime Database's Favorites section
     * @param databaseReference to save to
     * @param userID to save under
     */
    public void saveNewToFirebaseFavorites(DatabaseReference databaseReference, String userID){
        databaseReference.child(Constants.FIREBASE_DATABASE_FAVORITES_PATH).child(userID).push().setValue(DiceRoll.this);
    }

    /**
     * A helper method to edit a DiceRoll in the Firebase Realtime Database's Favorites section
     */
    public void editSavedFirebaseFavorite(DatabaseReference databaseReference, String userID, final String previousName, final String previousFormula){
        DatabaseReference userFavorites = databaseReference.child(Constants.FIREBASE_DATABASE_FAVORITES_PATH).child(userID);
        Query queryRef = userFavorites.orderByChild(Constants.FIREBASE_DATABASE_FAVORITES_FORMULA_PATH).equalTo(previousFormula); //Query the database for first entry that has matching formula
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot diceRollSnapshot : dataSnapshot.getChildren()) { //Iterate through all returned values
                    String name = diceRollSnapshot.child(Constants.FIREBASE_DATABASE_FAVORITES_NAME_PATH).getValue().toString(); //Check the name of each
                    if (name.equals(previousName)) {//For the first result where name and formula match, edit
                        DatabaseReference diceRollRef = diceRollSnapshot.getRef();
                        diceRollRef.child(Constants.FIREBASE_DATABASE_FAVORITES_NAME_PATH).setValue(mName);
                        diceRollRef.child(Constants.FIREBASE_DATABASE_FAVORITES_FORMULA_PATH).setValue(mFormula);
                        break; //To prevent further edits in the event of multiple matching entries
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * A helper method to delete this DiceRoll from Firebase Realtime Database's Favorites section
     */
    public void deleteDiceRoll(DatabaseReference databaseReference, String userID){
        DatabaseReference userFavorites = databaseReference.child(Constants.FIREBASE_DATABASE_FAVORITES_PATH).child(userID);
        Query queryRef = userFavorites.orderByChild(Constants.FIREBASE_DATABASE_FAVORITES_FORMULA_PATH).equalTo(mFormula); //Query the database for first entry that has matching formula
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot diceRollSnapshot : dataSnapshot.getChildren()) { //Iterate through all returned values
                    String name = diceRollSnapshot.child(Constants.FIREBASE_DATABASE_FAVORITES_NAME_PATH).getValue().toString(); //Check the name of each
                    if (name.equals(mName)) {//For the first result where name and formula match, edit
                        DatabaseReference diceRollRef = diceRollSnapshot.getRef();
                        diceRollRef.removeValue();
                        break; //To prevent further edits in the event of multiple matching entries
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Parcelable logic
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mFormula);
    }

    protected DiceRoll(Parcel in) {
        this.mName = in.readString();
        this.mFormula = in.readString();
    }

    public static final Parcelable.Creator<DiceRoll> CREATOR = new Parcelable.Creator<DiceRoll>() {
        @Override
        public DiceRoll createFromParcel(Parcel source) {
            return new DiceRoll(source);
        }

        @Override
        public DiceRoll[] newArray(int size) {
            return new DiceRoll[size];
        }
    };
}
