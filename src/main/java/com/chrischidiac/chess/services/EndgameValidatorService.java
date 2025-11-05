package com.chrischidiac.chess.services;

import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.GameState;
import com.chrischidiac.chess.models.Piece;

import java.util.List;

//Validates whether or not the king is in checkmate or stalemate
public class EndgameValidatorService {

    private LegalMovesService legalMovesService;
    private CheckValidatorService checkValidatorService;
    private Piece[][] pieces;
    GameState gameState = new GameState();
    private static final int BOARD_SIZE = 8;

    public EndgameValidatorService(Piece[][] pieces, GameState gameState) {
        this.pieces = pieces;
        this.gameState = gameState;
        this.legalMovesService = new LegalMovesService(pieces, gameState);
        this.checkValidatorService = new CheckValidatorService(pieces, gameState);
    }

    public boolean hasAnyLegalMoves(char kingColour) {

            //iterate over the whole board to see if any of the player's pieces has a legal move
            for(int row = 0; row < BOARD_SIZE; row++) {
                for(int column = 0; column < BOARD_SIZE; column++) {
                    Piece piece = pieces[row][column];
                    if(piece != null && piece.getColour() == kingColour) {
                        //Get the legal moves of the currently selected piece
                        List<Coordinates> legalMoves = legalMovesService.getLegalMoves(piece.getName(), piece, row, column);
                        if(!legalMoves.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
            return false;
    }

     public boolean isKingInCheckmate(char kingColour) {
        return checkValidatorService.isKingInCheck(kingColour) && !hasAnyLegalMoves(kingColour);
    }

    public boolean isKingInStalemate(char kingColour) {
        return !checkValidatorService.isKingInCheck(kingColour) && !hasAnyLegalMoves(kingColour);
    }


}
