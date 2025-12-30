package com.chrischidiac.chess.services;

import org.springframework.stereotype.Component;
import com.chrischidiac.chess.models.GameState;
import com.chrischidiac.chess.models.Piece;

@Component
public class ChessServiceFactory {
    
    public LegalMovesService createLegalMovesService(Piece[][] pieces, GameState gameState) {
        return new LegalMovesService(pieces, gameState);
    }
    
    public EndgameValidatorService createEndgameValidatorService(Piece[][] pieces, GameState gameState) {
        return new EndgameValidatorService(pieces, gameState);
    }
    
    public CheckValidatorService createCheckValidatorService(Piece[][] pieces, GameState gameState) {
        return new CheckValidatorService(pieces, gameState);
    }
}
