package com.chrischidiac.chess.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.GameState;
import com.chrischidiac.chess.models.Piece;
import com.chrischidiac.chess.services.*;
import com.chrischidiac.chess.dtos.MoveRequest;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChessController {

    //2D array representing the 8x8 chess board
    private Piece[][] pieces = new Piece[8][8];
    GameState gameState = new GameState();
    private static final int BOARD_SIZE = 8;
    
    @Autowired
    private ChessServiceFactory serviceFactory;

    @GetMapping("/")
    public String root(Model model, HttpSession session) {
        return fillBoard(model, session);
    }

    @GetMapping("/startGame")
    public String fillBoard(Model model, HttpSession session) {

        if (session.getAttribute("pieces") == null) {
            initializeBoard(model, session);
        }
        //Add the pieces array to the model for rendering
        model.addAttribute("pieces", session.getAttribute("pieces"));
        model.addAttribute("gameState", session.getAttribute("gameState"));

        return "game";
    }

    private void initializeBoard(Model model, HttpSession session) {

        //Populate board with black pieces
        pieces[0][0] = new Piece("Rook", 'B', false);
        pieces[0][1] = new Piece("Knight", 'B', false);
        pieces[0][2] = new Piece("Bishop", 'B', false);
        pieces[0][3] = new Piece("Queen", 'B', false);
        pieces[0][4] = new Piece("King", 'B', false);
        pieces[0][5] = new Piece("Bishop", 'B', false);
        pieces[0][6] = new Piece("Knight", 'B', false);
        pieces[0][7] = new Piece("Rook", 'B', false);

        for(int i = 0; i < BOARD_SIZE; i++)
            {
                //Populate board with black pawns
                pieces[1][i] = new Piece("Pawn", 'B', false);
            }

      
        for(int i = 0; i < BOARD_SIZE; i++)
            {
                //Populate board with white pawns
                pieces[6][i] = new Piece("Pawn", 'W', false);
            }

        //Populate board with white pieces
        pieces[7][0] = new Piece("Rook", 'W', false);
        pieces[7][1] = new Piece("Knight", 'W', false);
        pieces[7][2] = new Piece("Bishop", 'W', false);
        pieces[7][3] = new Piece("Queen", 'W', false);
        pieces[7][4] = new Piece("King", 'W', false);
        pieces[7][5] = new Piece("Bishop", 'W', false);
        pieces[7][6] = new Piece("Knight", 'W', false);
        pieces[7][7] = new Piece("Rook", 'W', false);

        //Add the pieces array and gameState to the session
        session.setAttribute("pieces", pieces);
        session.setAttribute("gameState", new GameState());
        
        //Add the pieces array to the model for rendering
        model.addAttribute("pieces",  session.getAttribute("pieces"));
        model.addAttribute("gameState",  session.getAttribute("gameState"));

        return;
    }

    @PostMapping("/recordPieceSelection")
    public ResponseEntity<Map<String, Object>> recordPieceSelection(@RequestBody Map<String, Object> pieceData, HttpSession session) {

        Piece[][] pieces = (Piece[][]) session.getAttribute("pieces");
        GameState gameState = (GameState) session.getAttribute("gameState");


        //Add a successful status and the data of the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("pieceData", pieceData);

        //Add the rowIndex and columnIndex of the selected piece to the session
        session.setAttribute("originalRowIndex", pieceData.get("row"));
        session.setAttribute("originalColumnIndex", pieceData.get("column"));
        session.setAttribute("pieceName", pieceData.get("pieceName"));
        session.setAttribute("pieces", pieces);
        session.setAttribute("gameState", gameState);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getLegalMoves")
    public ResponseEntity<Map<String, Object>> getLegalMoves(@RequestBody MoveRequest moveRequest, HttpSession session) {

        Piece[][] pieces = (Piece[][]) session.getAttribute("pieces");
        GameState gameState = (GameState) session.getAttribute("gameState");

        Piece selectedPiece = moveRequest.getSelectedPiece();
        if (selectedPiece == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "No piece on the selected square");
            return ResponseEntity.badRequest().body(response);
        }
        String pieceName = selectedPiece.getName();
        int row = moveRequest.getRow();
        int column = moveRequest.getColumn();

       LegalMovesService legalMovesService = serviceFactory.createLegalMovesService(pieces, gameState);

       List<Coordinates> legalMoves = legalMovesService.getLegalMoves(pieceName, selectedPiece, row, column);

        //Add a successful status and the legalMoves for the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("legalMoves", legalMoves);

        session.setAttribute("pieces", pieces);
        session.setAttribute("gameState", gameState);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/movePiece")
    public ResponseEntity<Map<String, Object>> movePiece(@RequestBody MoveRequest moveRequest, Model model, HttpSession session) {

        Piece[][] pieces = (Piece[][]) session.getAttribute("pieces");
        GameState gameState = (GameState) session.getAttribute("gameState");

        //Get the new rowIndex and columnIndex coordinates of the square the piece is moving to
        int newRowIndex = moveRequest.getRow();
        int newColumnIndex = moveRequest.getColumn();

        Map<String, Object> response = new HashMap<>();
        List<Coordinates> legalMoves =  moveRequest.getLegalMoves();
        //Check if its a legal move
        if(legalMoves == null || !(legalMoves.contains(new Coordinates(newRowIndex, newColumnIndex)))) {
            response.put("status", "error");
            response.put("message", "Invalid move");
            return ResponseEntity.badRequest().body(response);
        }

        //Get the original rowIndex and columnIndex coordinates of the square the piece was on
        int originalRowIndex = (Integer) session.getAttribute("originalRowIndex");
        int originalColumnIndex = (Integer) session.getAttribute("originalColumnIndex");

        Piece selectedPiece = pieces[originalRowIndex][originalColumnIndex];
        
        if(selectedPiece == null) {
            response.put("status", "error");
            response.put("message", "No piece on the selected square");
            return ResponseEntity.badRequest().body(response);
        }

        selectedPiece.setHasMoved(true);

        //Check for en passant
        if(selectedPiece.getName().equals("Pawn") 
        && Math.abs(newColumnIndex - originalColumnIndex) == 1 //Check for diagonal move
        && pieces[newRowIndex][newColumnIndex] == null) { //If the pawn made a diagonal move to an empty square, it means it captured with en passant
            int capturedPawnRow = originalRowIndex;
            int capturedPawnColumn = newColumnIndex;
            Piece capturedPawn = pieces[capturedPawnRow][capturedPawnColumn];

            if(capturedPawn != null && capturedPawn.getName().equals("Pawn") 
            && capturedPawn.getColour() != selectedPiece.getColour()) {
                //Empty the square the captured pawn was on
                pieces[capturedPawnRow][capturedPawnColumn] = null;
                response.put("capturedPawnRowIndex", capturedPawnRow);
                response.put("capturedPawnColumnIndex", capturedPawnColumn);
            }
        }

        //Move the piece to the new square
        pieces[newRowIndex][newColumnIndex] =  selectedPiece;
        //Empty the original square the piece was on by setting it to null
        pieces[originalRowIndex][originalColumnIndex] = null;

        //We must check to see if the move the player made is a castling move, as this requires us to also move the rook. 
        //We check this by checking if the king moved 2 squares to the left or right, as this is only possible when castling
        if(selectedPiece.getName().equals("King") && Math.abs(newColumnIndex - originalColumnIndex) == 2) {
                
            int rookOriginalColumn = (newColumnIndex > originalColumnIndex) ? BOARD_SIZE - 1 : 0;
            int rookNewColumn = (newColumnIndex > originalColumnIndex) ? newColumnIndex - 1 : newColumnIndex + 1;

            //Move the rook next to the king
            pieces[newRowIndex][rookNewColumn] = pieces[newRowIndex][rookOriginalColumn];
            pieces[newRowIndex][rookOriginalColumn] = null;

            response.put("rookOriginalColumn", rookOriginalColumn);
            response.put("rookNewColumn", rookNewColumn);
        }

        //Check for pawn promotion
        if(selectedPiece.getName().equals("Pawn") && (newRowIndex == BOARD_SIZE - 1 || newRowIndex == 0)) {
            response.put("isPawnPromoting", true);
        }

        gameState.setLastMovedPieceName(selectedPiece.getName());
        gameState.setLastMoveFrom(new Coordinates(originalRowIndex, originalColumnIndex));
        gameState.setLastMoveTo(new Coordinates(newRowIndex, newColumnIndex));
    
        //Add the original rowIndex and columnIndex coordinates and the new rowIndex and columnIndex coordinates to the response
        response.put("originalRowIndex", originalRowIndex);
        response.put("originalColumnIndex", originalColumnIndex);
        response.put("newColumnIndex", newColumnIndex);
        response.put("newRowIndex", newRowIndex);
         //Add a successful status and the legal moves to the response
        response.put("status", "success");
        response.put("legalMoves", legalMoves);

        session.setAttribute("pieces", pieces);
        session.setAttribute("gameState", gameState);
        
        return ResponseEntity.ok(response);
    }

    //Validate whether or not the king is in checkmate or stalemate
    @PostMapping("/endgameValidation")
    public ResponseEntity<Map<String, Object>> endgameValidation(@RequestBody  Map<String, String> body, HttpSession session) {

        Piece[][] pieces = (Piece[][]) session.getAttribute("pieces");
        GameState gameState = (GameState) session.getAttribute("gameState");

       EndgameValidatorService endgameValidatorService = serviceFactory.createEndgameValidatorService(pieces, gameState);
       char kingColour = body.get("pieceColour").charAt(0);

        boolean isKingInCheckmate = endgameValidatorService.isKingInCheckmate(kingColour);
        boolean isKingInStalemate = endgameValidatorService.isKingInStalemate(kingColour);
        System.out.println(isKingInStalemate);
   
        //Add a successful status and whether or not it is checkmate or stalemate to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("isKingInCheckmate", isKingInCheckmate);
        response.put("isKingInStalemate", isKingInStalemate);

        session.setAttribute("pieces", pieces);
        session.setAttribute("gameState", gameState);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/newGame")
    public ResponseEntity<Map<String, Object>> newGame(Model model, HttpSession session) {
        initializeBoard(model, session);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/agreeToDraw")
    public ResponseEntity<Map<String,Object>> agreeDraw(HttpSession session) {
        
        Map<String,Object> response = new HashMap<>();
        response.put("status", "success");
      
        return ResponseEntity.ok(response);
    }
}