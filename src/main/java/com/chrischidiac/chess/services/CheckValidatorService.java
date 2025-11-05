package com.chrischidiac.chess.services;

import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.GameState;
import com.chrischidiac.chess.models.Piece;

import java.util.ArrayList;
import java.util.List;

public class CheckValidatorService {

    private LegalMovesService legalMovesService;
    private Piece[][] pieces;
    GameState gameState = new GameState();
    private static final int BOARD_SIZE = 8;

    public CheckValidatorService(Piece[][] pieces, GameState gameState) {
        this.pieces = pieces;
        this.gameState = gameState;
        this.legalMovesService = new LegalMovesService(pieces, gameState);
    }


    public boolean isKingInCheck(char kingColour) {
        Coordinates kingPosition = findKing(kingColour);
        if(kingPosition == null) {
            throw new IllegalStateException(kingColour + " King not on the board");
        }
        
        char opposingPieceColour = (kingColour == 'W') ? 'B' : 'W';
            
             //iterate over the whole board to find all opposing pieces
             for(int row = 0; row < BOARD_SIZE; row++) {
                for(int column = 0; column < BOARD_SIZE; column++) {
                    Piece piece = pieces[row][column];
             
                    if(piece!=null) {
                        if(piece.getName().equals("King")) {
                            List<Coordinates> legalMoves = legalMovesService.getKingAttackSquares(row, column);
                            if(legalMoves.contains(kingPosition)) {
                                return true;
                            }
                        }
                        else if(piece.getColour() == opposingPieceColour) {
                            /*Get the pseudo legal moves of the opposing piece to see if that piece is putting our king in check. We get the pseudo legal moves because
                            it doesn't matter whether or not the possible move is putting our king in check
                            */
                            List<Coordinates> legalMoves = legalMovesService.getPseudoLegalMoves(piece.getName(), row, column);
                            if(legalMoves.contains(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
             }
              return false;
    }

    public Coordinates findKing(char kingColour) {
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int column = 0; column < BOARD_SIZE; column++) {
                Piece piece = pieces[row][column];
                if(piece != null && piece.getName().equals("King") && piece.getColour() == kingColour) {
                    return new Coordinates(row, column);
                }
            }
        }
        return null;
    }
}