package com.chrischidiac.chess.models;

import java.io.*;

public class Piece implements Serializable  {
    private String name;
    private char colour;
    private boolean hasMoved;
    
public Piece() {
}

public Piece(String name, char colour, boolean hasMoved) {
    this.name = name;
    this.colour = colour;
    this.hasMoved = hasMoved;
}

public String getName() {
    return name;
}

public void setName(String name)
{
    this.name = name;
}

public char getColour() {
    return colour;
}

public void setColour(char colour)
{
    this.colour = colour;
}

public boolean getHasMoved() {
    return hasMoved;
}

public void setHasMoved(boolean hasMoved)
{
    this.hasMoved = hasMoved;
}

}




