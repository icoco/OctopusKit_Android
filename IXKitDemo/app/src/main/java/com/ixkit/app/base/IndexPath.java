package com.ixkit.app.base;

/**
 * Created by icoco on 8/21/16.
 */
public class IndexPath {

    private int mSection;
    private int mRow;


    public IndexPath(int section, int row){
        mSection = section;
        mRow = row;
    }

    public int getSection() {
        return mSection;
    }

    public int getRow(){
        return mRow;
    }

    public String toString(){
        String result = "section=[" + mSection + "]"
                + ",row=[" + mRow + "]";
        return result;
    }
}


