package com.chrischidiac.chess.services;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.GameState;
import com.chrischidiac.chess.models.Piece;


public class LegalMovesService {


    private Piece[][] pieces;
    private GameState gameState;
    private static final int BOARD_SIZE = 8;


    public LegalMovesService(Piece[][] pieces, GameState gameState) {
        this.pieces = pieces;
        this.gameState = gameState;
    }

    //GetMoves function used for Bishops, Rooks, and Queens (Pawns, Knights, and Kings behave uniquely and therefore have separate move logic functions).
    public List<Coordinates> getPseudoLegalMoves(int[][] pieceDirections, int row, int column) {

        List<Coordinates> moves = new ArrayList<>();
        Piece selectedPiece = pieces[row][column];
        if (selectedPiece == null) {
            return moves;
        }
        
        char opposingPieceColour = (selectedPiece.getColour() == 'W') ? 'B' : 'W';
       
        for (int[] pieceDirection : pieceDirections) { 
            for (int i = 1; i < BOARD_SIZE; i++) {
                int targetRow = row + pieceDirection[0] * i;
                int targetColumn = column + pieceDirection[1] * i;

                if (!(targetRow >= 0 && targetRow < BOARD_SIZE && targetColumn >= 0 && targetColumn < BOARD_SIZE)) {
                    break;
                }
                else {
                    //Check if the square the piece is moving to is empty
                    Piece piece = pieces[targetRow][targetColumn];
                    if (piece == null) {
                        moves.add(new Coordinates(targetRow, targetColumn));
                    }
                    else {
                        //If the square the piece is moving to has an opponents piece on it, we add it as a legal move and then stop searching in that direction
                        if (piece.getColour() == opposingPieceColour) {
                        moves.add(new Coordinates(targetRow, targetColumn));
                    }
                    break;
                  }
                }
            }
        }
        return moves;
    }

        /*Returns all pseudo legal pawn moves, these are possible pawn moves in the current position that do not account for whether or not
        the king is in check after the moves are played.*/
       public List<Coordinates> getPseudoLegalPawnMoves(int row, int column) {
     
        List<Coordinates> moves = new ArrayList<>();
        Piece selectedPiece = pieces[row][column];
        if (selectedPiece == null) {
            return moves;
        }

        char opposingPieceColour = (selectedPiece.getColour() == 'W') ? 'B' : 'W';
        //White pieces move up the board in the negative direction (row - 1), black pieces move down the board in the positive direction (row + 1)
        int direction = (selectedPiece.getColour() == 'W') ? -1 : 1;
        int rowAhead = row + direction;

        if (rowAhead >= 0 && rowAhead < BOARD_SIZE) {
            if (pieces[rowAhead][column] == null) { 
                moves.add(new Coordinates(rowAhead, column));
                int twoSquaresAhead = row + 2 * direction;
                if (twoSquaresAhead >= 0 && twoSquaresAhead < BOARD_SIZE) {
                    //If the pawn hasn't moved yet this game, it can move 2 squares if no pieces are obstructing its path.
                    if (!selectedPiece.getHasMoved() && pieces[twoSquaresAhead][column] == null) {
                        moves.add(new Coordinates(twoSquaresAhead, column));
                    }
                }
            }

            //Pawn diagonal capture moves
            int[] diagonals = {1, -1}; //right and left
            for (int diagonal : diagonals) {
                int columnDiagonal = column + diagonal;
                if (columnDiagonal >= 0 && columnDiagonal < BOARD_SIZE) {
                    Piece diagonalPiece = pieces[rowAhead][columnDiagonal];
                    //Check if the diagonal has an opposing piece that can be captured
                    if (diagonalPiece != null && diagonalPiece.getColour() == opposingPieceColour) {
                        moves.add(new Coordinates(rowAhead, columnDiagonal));
                    }
                    Piece adjacentPiece = pieces[row][columnDiagonal];
                    //Check if en passant is possible
                    if(adjacentPiece != null && adjacentPiece.getName().equals("Pawn") && adjacentPiece.getColour() == opposingPieceColour) {
                        if(gameState.getLastMovedPieceName() != null 
                        && gameState.getLastMovedPieceName().equals("Pawn") 
                        && gameState.getLastMoveTo() != null
                        && gameState.getLastMoveTo().getRowIndex() == row 
                        && gameState.getLastMoveTo().getColumnIndex() == columnDiagonal
                        && gameState.getLastMoveFrom() != null
                        && Math.abs(gameState.getLastMoveFrom().getRowIndex() - gameState.getLastMoveTo().getRowIndex()) == 2) {
                            moves.add(new Coordinates(rowAhead, columnDiagonal));
                        }
                    }
                }
            }
        }

        return moves;
    }

    /*Returns all pseudo legal knight moves, these are possible knight moves in the current position that do not account for whether or not
    the king is in check after the moves are played.*/
    public List<Coordinates> getPseudoLegalKnightMoves(int row, int column) {

        List<Coordinates> moves = new ArrayList<>();
        Piece selectedPiece = pieces[row][column];

        if (selectedPiece == null) {
            return moves;
        }

        char opposingPieceColour = (selectedPiece.getColour() == 'W') ? 'B' : 'W';
       
        //These are the 8 possible permutations of knight moves in chess
       int[][] possibleKnightMoves = {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : possibleKnightMoves) {
           
            int targetRow = row + move[0];
            int targetColumn = column + move[1];

            //Check if the row and column are within bounds
            if (targetRow >= 0 && targetRow < BOARD_SIZE && targetColumn >= 0 && targetColumn < BOARD_SIZE) {
                Piece piece = pieces[targetRow][targetColumn];
                //Ensure the square the knight is moving to is either empty or has their opponent's piece on it
                if (piece == null || piece.getColour() == opposingPieceColour) {
                    moves.add(new Coordinates(targetRow, targetColumn));
                }
            }
        }
        return moves;
    }

    /*Returns all pseudo legal bishop moves, these are possible bishop moves in the current position that do not account for whether or not
    the king is in check after the moves are played.*/
    public List<Coordinates> getPseudoLegalBishopMoves(int row, int column) {
     
        //These are the 4 possible directions a bishop can go in chess
        int[][] bishopDirections = {
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1},
        };

        return getPseudoLegalMoves(bishopDirections, row, column);
    }

    /*Returns all pseudo legal rook moves, these are possible rook moves in the current position that do not account for whether or not
    the king is in check after the moves are played.*/
    public List<Coordinates> getPseudoLegalRookMoves(int row, int column) {
     
        //These are the 4 possible directions a rook can go in chess
       int[][] rookDirections = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},
        };

        return getPseudoLegalMoves(rookDirections, row, column); 
    }

    /*Returns all pseudo legal queen moves, these are possible queen moves in the current position that do not account for whether or not
    the king is in check after the moves are played.*/
    public List<Coordinates> getPseudoLegalQueenMoves(int row, int column) {
     
        //These are the 8 possible directions a rook can go in chess
        int[][] queenDirections = {
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1},
        };

       return getPseudoLegalMoves(queenDirections, row, column); 
    }

    /*Returns all pseudo legal king moves, these are possible king moves in the current position that do not account for whether or not
    the king is in check after the moves are played.*/
    public List<Coordinates> getPseudoLegalKingMoves(int row, int column) {
     
        List<Coordinates> moves = new ArrayList<>();
        Piece selectedPiece = pieces[row][column];

        if (selectedPiece == null) {
            return moves;
        }
        
        char kingColour = selectedPiece.getColour();
        char opposingPieceColour = (kingColour == 'W') ? 'B' : 'W';
        //These are the 8 possible directions the king can go in chess
        int[][] kingDirections = {
        {1, 0}, {1, 1}, {1, -1}, {0, 1}, {0, -1}, {-1, 0}, {-1, 1}, {-1, -1},
        };
       
        for (int[] kingDirection : kingDirections) {
           
                int targetRow = row + kingDirection[0];
                int targetColumn = column + kingDirection[1];

                //If the row and column are not within bounds, stop searching in this direction
                if (targetRow >= 0 && targetRow < BOARD_SIZE && targetColumn >= 0 && targetColumn < BOARD_SIZE) {
                    //Ensure the square the king is moving to is either empty or has their opponent's piece on it
                    Piece piece = pieces[targetRow][targetColumn];
                    if (piece == null || piece.getColour() == opposingPieceColour) {
                        moves.add(new Coordinates(targetRow, targetColumn));
                    }
                }
        }

        CheckValidatorService checkValidatorService = new CheckValidatorService(pieces, gameState);

        //Check if the current player can castle
        if(!selectedPiece.getHasMoved() && !checkValidatorService.isKingInCheck(kingColour)) {
            if(canCastle(kingColour, true, row, column)) {
                moves.add(new Coordinates(row, column + 2));
            }
            if(canCastle(kingColour, false, row, column)) {
                moves.add(new Coordinates(row, column - 2));
            }
        }
        return moves;
    }

    //Returns the squares the king attacks, used for detecting the possible moves from the opposing king without checking for castling. 
    public List<Coordinates> getKingAttackSquares(int row, int column) {
       
        List<Coordinates> moves = new ArrayList<>();
        Piece selectedPiece = pieces[row][column];

        if (selectedPiece == null) {
            return moves;
        }
        
        char kingColour = selectedPiece.getColour();
        char opposingPieceColour = (kingColour == 'W') ? 'B' : 'W';
        //These are the 8 possible directions the king can go in chess
        int[][] kingDirections = {
        {1, 0}, {1, 1}, {1, -1}, {0, 1}, {0, -1}, {-1, 0}, {-1, 1}, {-1, -1},
        };
       
        for (int[] kingDirection : kingDirections) {
           
                int targetRow = row + kingDirection[0];
                int targetColumn = column + kingDirection[1];

                //If the row and column are not within bounds, stop searching in this direction
                if (targetRow >= 0 && targetRow < BOARD_SIZE && targetColumn >= 0 && targetColumn < BOARD_SIZE) {
                    //Ensure the square the king is moving to is either empty or has their opponent's piece on it
                    Piece piece = pieces[targetRow][targetColumn];
                    if (piece == null || piece.getColour() == opposingPieceColour) {
                        moves.add(new Coordinates(targetRow, targetColumn));
                    }
                }
        }
        return moves;
    }


    private boolean canCastle(char kingColour, boolean kingSide, int row, int column) {

        int rookColumn = kingSide ? BOARD_SIZE - 1 : 0;
        Piece rook = pieces[row][rookColumn];
       
        //Rook has to exist, be the same colour as the king, and must not have moved yet
        if(rook == null || !rook.getName().equals("Rook") || rook.getColour() != kingColour || rook.getHasMoved()) {
            return false;
        }

        CheckValidatorService checkValidatorService = new CheckValidatorService(pieces, gameState);
        
        int direction = kingSide ? 1 : -1;
        int kingDestinationSquare = column + 2 * direction;
        
        //We iterate through the squares between the king and the rook, as well as the starting square of the king to ensure none of those squares are being attacked.
        for (int i = column + direction; i != rookColumn; i+= direction) { 
            //Check the intermediate squares between the king and the rook to see if they are empty
            if(pieces[row][i] != null) {
                return false;
            }
                //While castling in chess, the squares the king moves through must not be under attack, meaning the king can't castle through a check.
                //Therefore we must validate whether or not the king would be in check on the 2 squares next to him in the direction of the rook.

                //Check the 2 squares next to the king to see whether or not the king would be in check on these squares
                if(i == column + direction || i == kingDestinationSquare) {
                //Save the current king state
                Piece originalKingState = pieces[row][column];
                //Temporarily move the king to this intermediate square
                pieces[row][i] = originalKingState;
                pieces[row][column] = null;

                //Since the player can't castle through a check, we return false
                if(checkValidatorService.isKingInCheck(kingColour)) {
                    return false;
                }
                //Revert the board to its original state
                pieces[row][column] = originalKingState;
                pieces[row][i] = null;
                }
        }
        return true;
    }


    //Simulate the move to see if your king is in check after this move is played. If so, this is an illegal move.
    public List<Coordinates> validateLegalMoves(List<Coordinates> legalMoves, Piece selectedPiece, int row, int column) {

        CheckValidatorService checkValidatorService = new CheckValidatorService(pieces, gameState);
        char pieceColour = selectedPiece.getColour();
        Iterator<Coordinates> iterator = legalMoves.iterator();

        //Use an iterator to move through the legalMoves list and remove any moves that would put our king in check
        while(iterator.hasNext()) {
            Coordinates legalMove = iterator.next();
            int targetRow = legalMove.getRowIndex();
            int targetColumn = legalMove.getColumnIndex();

            //Save the current state
            Piece originalPieceState = pieces[targetRow][targetColumn];
            //Temporarily make the move
            pieces[targetRow][targetColumn] = selectedPiece;
            pieces[row][column] = null;

            //If the move puts our king in check, we remove it from the list
            if(checkValidatorService.isKingInCheck(pieceColour)) {
                iterator.remove();
            }

            //Revert the board to its origianl state
            pieces[row][column] = selectedPiece;
            pieces[targetRow][targetColumn] = originalPieceState;
        }
        return legalMoves;
    }

    public List<Coordinates> getLegalMoves(String pieceName, Piece selectedPiece, int row, int column) {
        switch (pieceName) {
            case "Pawn": return validateLegalMoves(getPseudoLegalPawnMoves(row, column), selectedPiece, row, column);
            case "Knight": return validateLegalMoves(getPseudoLegalKnightMoves(row, column), selectedPiece, row, column);
            case "Bishop": return validateLegalMoves(getPseudoLegalBishopMoves(row, column), selectedPiece, row, column);
            case "Rook": return validateLegalMoves(getPseudoLegalRookMoves(row, column), selectedPiece, row, column);
            case "Queen": return validateLegalMoves(getPseudoLegalQueenMoves(row, column), selectedPiece, row, column);
            case "King": return validateLegalMoves(getPseudoLegalKingMoves(row, column), selectedPiece, row, column);
            default: throw new IllegalArgumentException("Invalid piece type: " + pieceName);
        }
    }

    //Returns all moves for a piece, regardless of whether or not that move puts your own king in check
     public List<Coordinates> getPseudoLegalMoves(String pieceName, int row, int column) {
        switch (pieceName) {
            case "Pawn": return getPseudoLegalPawnMoves(row, column);
            case "Knight": return getPseudoLegalKnightMoves(row, column);
            case "Bishop": return getPseudoLegalBishopMoves(row, column);
            case "Rook": return getPseudoLegalRookMoves(row, column);
            case "Queen": return getPseudoLegalQueenMoves(row, column);
            case "King": return getPseudoLegalKingMoves(row, column);
            default: throw new IllegalArgumentException("Invalid piece type: " + pieceName);
        }
    }

}


