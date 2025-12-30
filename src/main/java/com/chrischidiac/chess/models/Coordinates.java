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
    
    public int getRowIndex() {
        return rowIndex;
    }
    
    public void setRowIndex(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public void setColumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates other = (Coordinates) obj;
        return this.rowIndex == other.rowIndex && this.columnIndex == other.columnIndex;
    }

    @Override
    public int hashCode() {
        return 31 * rowIndex + columnIndex;
    }
    
}
