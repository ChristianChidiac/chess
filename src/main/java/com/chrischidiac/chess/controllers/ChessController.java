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
    
    private Piece[][] pieces = new Piece[8][8];
    private List<Coordinates> moveCoordinates = new ArrayList<>();

    
    @GetMapping("/startGame")
    public String fillBoard(Model model, HttpSession session) {

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
                pieces[1][i] = new Piece("Pawn", 'B');
            }

            for(int i = 0; i < 8; i++)
            {
                pieces[2][i] = new Piece("Empty", 'X');
                pieces[3][i] = new Piece("Empty", 'X');
                pieces[4][i] = new Piece("Empty", 'X');
                pieces[5][i] = new Piece("Empty", 'X');
            }
      
        for(int i = 0; i < 8; i++)
            {
                pieces[6][i] = new Piece("Pawn", 'W');
            }

        pieces[7][0] = new Piece("Rook", 'W');
        pieces[7][1] = new Piece("Knight", 'W');
        pieces[7][2] = new Piece("Bishop", 'W');
        pieces[7][3] = new Piece("Queen", 'W');
        pieces[7][4] = new Piece("King", 'W');
        pieces[7][5] = new Piece("Bishop", 'W');
        pieces[7][6] = new Piece("Knight", 'W');
        pieces[7][7] = new Piece("Rook", 'W');

        session.setAttribute("pieces", pieces);
        model.addAttribute("pieces", pieces);
            
        return "game";
    }

    @PostMapping("/recordPieceClick")
    public ResponseEntity<Map<String, Object>> recordPieceClick(@RequestBody Map<String, Integer> coordinates, Model model, HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("coordinates", coordinates);

        final Coordinates newCoordinates = new Coordinates(coordinates.get("column"), coordinates.get("row"));

        if(moveCoordinates.size() == 1) {
             //Check if the original coordinates and the new coordinates are the same, and if so, the piece doesn't move
            if(newCoordinates.getX() == moveCoordinates.get(0).getX() && newCoordinates.getY() == moveCoordinates.get(0).getY())
            {
                moveCoordinates.clear();
                return ResponseEntity.ok(response);
            }
        }

        moveCoordinates.add(newCoordinates);

        if(moveCoordinates.size() == 2)
        {
            movePiece(response);
            moveCoordinates.clear();
        }

    return ResponseEntity.ok(response);
    }
    
    public void movePiece(Map<String, Object> response)
    {
        int originalX = moveCoordinates.get(0).getX();
        int originalY = moveCoordinates.get(0).getY();
        int newX = moveCoordinates.get(1).getX();
        int newY = moveCoordinates.get(1).getY();

        int xCoordinateMovement = newX - originalX;
        int yCoordinateMovement = newY - originalY;

        pieces[newX][newY] =  pieces[originalX][originalY];
        pieces[originalX][originalY] = new Piece("Empty", 'X');

        response.put("originalX", originalX);
        response.put("originalY", originalY);
        response.put("newX", newX);
        response.put("newY", newY);
    }

}
