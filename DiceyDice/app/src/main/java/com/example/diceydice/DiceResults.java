package com.example.diceydice;

import java.util.Date;

public class DiceResults {
    private String mName;
    private String mDescrip;
    private int mTotal;
    private Date mDateCreated;

    public DiceResults(String name, String descrip, int total) {
        mName = name;
        mDescrip = descrip;
        mTotal = total;
        mDateCreated = new Date();
    }

    public DiceResults(String name, String descrip, int total, Date date){
        mName = name;
        mDescrip = descrip;
        mTotal = total;
        mDateCreated = date;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescrip() {
        return mDescrip;
    }

    public void setDescrip(String descrip) {
        mDescrip = descrip;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public Date getDateCreated(){
        return mDateCreated;
    }
}
