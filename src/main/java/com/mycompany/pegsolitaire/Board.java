package com.mycompany.pegsolitaire;


import com.mycompany.pegsolitaire.Move;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Board implements Serializable{
    int boardGrid[][];
    int sizeOfGrid = 7;
    
    public Board(){
        boardGrid = new int[sizeOfGrid][sizeOfGrid];
        initializeBoard();
    }
    
    public Board(Board other){
        this.sizeOfGrid = other.sizeOfGrid;
        this.boardGrid = new int[sizeOfGrid][sizeOfGrid];
        
        for(int x = 0; x < sizeOfGrid; x++){
            for(int y = 0; y < sizeOfGrid; y++){
                this.boardGrid[x][y] = other.boardGrid[x][y];
            }
        }
    }
    
    public int[][] getGrid(){
        return boardGrid;
    }
    
    public void initializeBoard(){
        for(int x = 0; x < sizeOfGrid; x++){
            for(int y = 0; y < sizeOfGrid; y++){
                boardGrid[x][y] = -1;
            }
        }
        
        int[][] validPositions = {
        {2,0}, {2,1}, {2,2}, {2,3}, {2,4}, {2,5}, {2,6},
        {3,0}, {3,1}, {3,2}, {3,3}, {3,4}, {3,5}, {3,6},
        {4,0}, {4,1}, {4,2}, {4,3}, {4,4}, {4,5}, {4,6},
        {0,2}, {0,3}, {0,4},
        {1,2}, {1,3}, {1,4},
        {5,2}, {5,3}, {5,4},
        {6,2}, {6,3}, {6,4}
        };
        
        for(int[] pos : validPositions){
            boardGrid[pos[0]][pos[1]] = 1;
        }
        
        boardGrid[3][3] = 0;
    }
    
    public boolean validPosition(int x, int y){
        if((x < 0 || x >= sizeOfGrid) || y < 0 || y >= sizeOfGrid){
            return false;
        } else {
            return boardGrid[x][y] != -1;
        }
    }
    
    public boolean validMove (int fromX, int fromY, int toX, int toY){
        
        if (!validPosition(fromX, fromY) || !validPosition(toX, toY)){
            return false;
        }
        
        if(boardGrid[fromX][fromY] != 1){
            return false;
        }
        
        if(boardGrid[toX][toY] != 0){
            return false;
        }
        
        int distanceX = toX - fromX;
        int distanceY = toY - fromY;
        
        if(!((Math.abs(distanceX) == 2 && distanceY == 0) || (Math.abs(distanceY) == 2 && distanceX == 0))){
            return false;
        }
        
        int middleX = (fromX + toX) / 2;
        int middleY = (fromY + toY) / 2;
        
        if(boardGrid[middleX][middleY] != 1){
            return false;
        }
        return true;
    }
    
    public void move(int fromX, int fromY, int toX, int toY){
        if(!validMove(fromX, fromY, toX, toY)){
            return;
        }
        
        int middleX = (fromX + toX) / 2;
        int middleY = (fromY + toY) / 2;
        
        boardGrid[fromX][fromY] = 0;
        boardGrid[middleX][middleY] = 0;
        boardGrid[toX][toY] = 1;
    }
    
    public List<Move> getValidMoves(){
        List<Move> moves = new ArrayList<>();
        
        for(int x = 0; x < sizeOfGrid; x++){
            for(int y = 0; y < sizeOfGrid; y++){
                
                if(boardGrid[x][y] == 1){
                    
                    if(validMove(x,y, x+2, y)){
                        moves.add(new Move(x,y, x+2, y));
                    }
                    
                    if(validMove(x,y, x-2, y)){
                        moves.add(new Move(x,y, x-2, y));
                    }
                    
                    if(validMove(x,y, x, y+2)){
                        moves.add(new Move(x,y, x, y+2));
                    }
                    
                    if(validMove(x,y, x, y-2)){
                        moves.add(new Move(x,y, x, y-2));
                    }
                }
            }
        }
        return moves;
    }
    
    public int pegs(){
        int counter = 0;
        for(int x = 0; x < sizeOfGrid; x++){
            for(int y = 0; y < sizeOfGrid; y++){
                if(boardGrid[x][y] == 1){
                    counter++;
                }
            }
        }
        return counter;
    }
    
    public boolean gameOver(){
        return getValidMoves().isEmpty();
    }
}
