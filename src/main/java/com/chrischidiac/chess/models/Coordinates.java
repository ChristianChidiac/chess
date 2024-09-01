package com.chrischidiac.chess.models;

import java.io.*;

public class Coordinates implements Serializable {
 
        private int x;
        public int y;
        
    public Coordinates() {
    }
    
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    
}
