package com.chrischidiac.chess.controllers;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import com.chrischidiac.chess.models.Coordinates;
import com.chrischidiac.chess.models.Piece;
import com.chrischidiac.chess.services.legalMovesService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class ChessController {

    //2D array representing the chess board
    private Piece[][] pieces = new Piece[8][8];

    @GetMapping("/startGame")
    public String fillBoard(Model model, HttpSession session) {

        //Populate board with black pieces
        pieces[0][0] = new Piece("Rook", 'B');
        pieces[0][1] = new Piece("Knight", 'B');
        pieces[0][2] = new Piece("Bishop", 'B');
        pieces[0][3] = new Piece("Queen", 'B');
        pieces[0][4] = new Piece("King", 'B');
        pieces[0][5] = new Piece("Bishop", 'B');
        pieces[0][6] = new Piece("Knight", 'B');
        pieces[0][7] = new Piece("Rook", 'B');

        for(int i = 0; i < 8; i++)
            {
                //Populate board with black pawns
                pieces[1][i] = new Piece("Pawn", 'B');
            }

            for(int i = 0; i < 8; i++)
            {
                //Populate board with empty squares in the middle
                pieces[2][i] = new Piece("Empty", 'X');
                pieces[3][i] = new Piece("Empty", 'X');
                pieces[4][i] = new Piece("Empty", 'X');
                pieces[5][i] = new Piece("Empty", 'X');
            }
      
        for(int i = 0; i < 8; i++)
            {
                //Populate board with white pawns
                pieces[6][i] = new Piece("Pawn", 'W');
            }

        //Populate board with white pieces
        pieces[7][0] = new Piece("Rook", 'W');
        pieces[7][1] = new Piece("Knight", 'W');
        pieces[7][2] = new Piece("Bishop", 'W');
        pieces[7][3] = new Piece("Queen", 'W');
        pieces[7][4] = new Piece("King", 'W');
        pieces[7][5] = new Piece("Bishop", 'W');
        pieces[7][6] = new Piece("Knight", 'W');
        pieces[7][7] = new Piece("Rook", 'W');

        //Add the pieces array to the model for rendering
        model.addAttribute("pieces", pieces);
            
        return "game";
    }

    @PostMapping("/recordPieceSelection")
    public ResponseEntity<Map<String, Object>> recordPieceSelection(@RequestBody Map<String, Object> pieceData, HttpSession session) {

        //Add a successful status and the data of the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("pieceData", pieceData);

        //Add the rowIndex and columnIndex of the selected piece to the session
        session.setAttribute("originalRowIndex", pieceData.get("row"));
        session.setAttribute("originalColumnIndex", pieceData.get("column"));
        session.setAttribute("pieceName", pieceData.get("pieceName"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getLegalMoves")
    public ResponseEntity<Map<String, Object>> getLegalMoves(@RequestBody Map<String, Object> pieceData, HttpSession session) {

       legalMovesService legalMovesService = new legalMovesService(pieces);

       List<Coordinates> legalMoves = legalMovesService.getLegalMoves(
        String.valueOf(pieceData.get("pieceName")),
        (Integer) pieceData.get("row"),
        (Integer) pieceData.get("column")
        );

        //Add a successful status and the legalMoves for the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("legalMoves", legalMoves);

        return ResponseEntity.ok(response);
    }

  
    @PostMapping("/movePiece")
    public ResponseEntity<Map<String, Object>> movePiece(@RequestBody Map<String, Integer> coordinates, Model model, HttpSession session) {

        //Add a successful status and the coordinates of the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("coordinates", coordinates);

        //Get the original rowIndex and columnIndex coordinates of the square the piece was on
        int originalRowIndex = (Integer) session.getAttribute("originalRowIndex");
        int originalColumnIndex = (Integer) session.getAttribute("originalColumnIndex");

        //Get the new rowIndex and columnIndex coordinates of the square the piece is moving to
        int newRowIndex = coordinates.get("row");
        int newColumnIndex = coordinates.get("column");

        //Get the difference between the new rowIndex and the original rowIndex coordinate 
        int rowIndexMovement = newRowIndex - originalRowIndex;

        //Get the difference between the new columnIndex and the original columnIndex coordinate 
        int columnIndexMovement = newColumnIndex - originalColumnIndex;

        //Move the piece to the new square
        pieces[newRowIndex][newColumnIndex] =  pieces[originalRowIndex][originalColumnIndex];
        //Empty the original square the piece was on
        pieces[originalRowIndex][originalColumnIndex] = new Piece("Empty", 'X');

        //Add the original rowIndex and columnIndex coordinates and the new rowIndex and columnIndex coordinates to the response
        response.put("originalRowIndex", originalRowIndex);
        response.put("originalColumnIndex", originalColumnIndex);
        response.put("newColumnIndex", newColumnIndex);
        response.put("newRowIndex", newRowIndex);
        
        return ResponseEntity.ok(response);
    }


}
