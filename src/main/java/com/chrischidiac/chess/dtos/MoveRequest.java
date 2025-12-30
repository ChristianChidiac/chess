package com.chrischidiac.chess.dtos;

import java.util.List;
import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.Piece;

public class MoveRequest {
    private int row;
    private int column;
    private List<Coordinates> legalMoves;
    private Piece selectedPiece; 

    public MoveRequest() {} 


    public int getRow() { 
        return row; 
    }

    public void setRow(int row) { 
        this.row = row; 
    }

    public int getColumn() { 
        return column; 
    }
    public void setColumn(int column) { 
        this.column = column; 
    }

    public List<Coordinates> getLegalMoves() {
        return legalMoves; 
    
    }
    public void setLegalMoves(List<Coordinates> legalMoves) { 
        this.legalMoves = legalMoves; 
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }
}
