package com.chrischidiac.chess.models;

import java.io.Serializable;

public class GameState implements Serializable {

    private String lastMovedPieceName;
    private Coordinates lastMoveFrom;
    private Coordinates lastMoveTo;

    public GameState() {
        this.lastMovedPieceName = null;
        this.lastMoveFrom = null;
        this.lastMoveTo = null;
    }

    // Getters and Setters
    public String getLastMovedPieceName() {
        return lastMovedPieceName;
    }

    public void setLastMovedPieceName(String lastMovedPieceName) {
        this.lastMovedPieceName = lastMovedPieceName;
    }

    public Coordinates getLastMoveFrom() {
        return lastMoveFrom;
    }

    public void setLastMoveFrom(Coordinates lastMoveFrom) {
        this.lastMoveFrom = lastMoveFrom;
    }

    public Coordinates getLastMoveTo() {
        return lastMoveTo;
    }

    public void setLastMoveTo(Coordinates lastMoveTo) {
        this.lastMoveTo = lastMoveTo;
    }

}



