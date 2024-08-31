package com.chrischidiac.chess.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import com.chrischidiac.chess.models.Piece;

@Controller
public class ChessController {
    
    private Piece[][] pieces = new Piece[8][8];
    
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
}
