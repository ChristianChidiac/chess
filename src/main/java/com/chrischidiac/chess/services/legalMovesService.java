package com.chrischidiac.chess.services;

import com.chrischidiac.chess.models.Piece;
import com.chrischidiac.chess.models.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class legalMovesService {

    private Piece[][] pieces;

    public legalMovesService(Piece[][] pieces) {
        this.pieces = pieces;
    }

    public List<Coordinates> getLegalPawnMoves(int row, int column) {
    
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;

    }

    public List<Coordinates> getLegalKnightMoves(int row, int column) {
     
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;
        
    }

    public List<Coordinates> getLegalBishopMoves(int row, int column) {
     
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;
        

    }

    public List<Coordinates> getLegalRookMoves(int row, int column) { 
     
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;
        
    }

    public List<Coordinates> getLegalQueenMoves(int row, int column) { 
     
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;
        

    }

    public List<Coordinates> getLegalKingMoves(int row, int column) { 
     
        List<Coordinates> legalMoves = new ArrayList<>();

        return legalMoves;
        
    }

    public List<Coordinates> getLegalMoves(String pieceName, int row, int column) {
        switch (pieceName) {
            case "Pawn": return getLegalPawnMoves(row, column);
            case "Knight": return getLegalKnightMoves(row, column);
            case "Bishop": return getLegalBishopMoves(row, column);
            case "Rook": return getLegalRookMoves(row, column);
            case "Queen": return getLegalQueenMoves(row, column);
            case "King": return getLegalKingMoves(row, column);
            default: throw new IllegalArgumentException("Invalid piece type: " + pieceName);
        }
    }
}
