package com.chrischidiac.chess.models;

import java.io.*;

public class Piece implements Serializable  {
    private String name;
    public char colour;
    
public Piece() {
}

public Piece(String name, char colour) {
    this.name = name;
    this.colour = colour;
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

}




