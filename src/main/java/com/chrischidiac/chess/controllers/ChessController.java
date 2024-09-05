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
    public ResponseEntity<Map<String, Object>> recordPieceSelection(@RequestBody Map<String, Integer> coordinates, HttpSession session) {

        //Add a successful status and the coordinates of the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("coordinates", coordinates);

        //Add the x and y coordinates of the selected piece to the session
        session.setAttribute("originalXCoordinate", coordinates.get("column"));
        session.setAttribute("originalYCoordinate", coordinates.get("row"));

    return ResponseEntity.ok(response);
    }

    @PostMapping("/movePiece")
    public ResponseEntity<Map<String, Object>> movePiece(@RequestBody Map<String, Integer> coordinates, Model model, HttpSession session) {

        //Add a successful status and the coordinates of the selected piece to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("coordinates", coordinates);

        //Get the original x and y coordinates of the square the piece was on
        int originalXCoordinate = (Integer) session.getAttribute("originalXCoordinate");
        int originalYCoordinate = (Integer) session.getAttribute("originalYCoordinate");

        //Get the new x and y coordinates of the square the piece is moving to
        int newXCoordinate = coordinates.get("column");
        int newYCoordinate = coordinates.get("row");

        //Get the difference between the new x and the original x coordinate 
        int xCoordinateMovement = newXCoordinate - originalXCoordinate;
        //Get the difference between the new y and the original y coordinate 
        int yCoordinateMovement = newYCoordinate - originalYCoordinate;

        //Move the piece to the new square
        pieces[newXCoordinate][newYCoordinate] =  pieces[originalXCoordinate][originalYCoordinate];
        //Empty the original square the piece was on
        pieces[originalXCoordinate][originalYCoordinate] = new Piece("Empty", 'X');

        //Add the original x and y coordinates and the new x and y coordinates to the response
        response.put("originalXCoordinate", originalXCoordinate);
        response.put("originalYCoordinate", originalYCoordinate);
        response.put("newXCoordinate", newXCoordinate);
        response.put("newYCoordinate", newYCoordinate);
        
        return ResponseEntity.ok(response);
    }


}
