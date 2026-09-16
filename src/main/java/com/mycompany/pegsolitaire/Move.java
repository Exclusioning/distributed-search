package com.mycompany.pegsolitaire;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Move implements Serializable{
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;
    
    public Move(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    @Override
    public String toString() {
        return "Move from (" + fromX + "," + fromY + ") to (" + toX + "," + toY + ")";
    }
    
}
