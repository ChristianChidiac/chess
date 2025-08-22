package com.chrischidiac.chess.models;

import java.io.*;

public class Coordinates implements Serializable {
        
    public int rowIndex;
    private int columnIndex;
        
    public Coordinates() {
    }
    
    public Coordinates(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }
    
    public int getrowIndex() {
        return rowIndex;
    }
    
    public void setrowIndex(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }
    
    public int getcolumnIndex() {
        return columnIndex;
    }
    
    public void setcolumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }
    
    
}
