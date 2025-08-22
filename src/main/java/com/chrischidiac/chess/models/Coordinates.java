package com.chrischidiac.chess.models;

import java.io.*;

public class Coordinates implements Serializable {
 
        private int columnIndex;
        public int rowIndex;
        
    public Coordinates() {
    }
    
    public Coordinates(int columnIndex, int rowIndex) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }
    
    public int getcolumnIndex() {
        return columnIndex;
    }
    
    public void setcolumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }
    
    public int getrowIndex() {
        return rowIndex;
    }
    
    public void setrowIndex(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }
    
    
}
