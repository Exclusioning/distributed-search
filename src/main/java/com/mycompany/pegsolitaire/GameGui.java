package com.mycompany.pegsolitaire;


import com.mycompany.pegsolitaire.GameSwing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class GameGui {
    public Board boardGrid;
    public Evaluator evaluate;
    public GameSwing gui;
    
    public int selectedX = -1;
    public int selectedY = -1;
    
    public ServerDistribution server1;
    public ServerDistribution server2;
    public boolean useDistribution = false;
    
    public GameGui(){
        boardGrid = new Board();
        evaluate = new Evaluator();
        server1 = new ServerDistribution("cs.oswego.edu", 26700);
        server2 = new ServerDistribution("cs.oswego.edu", 26701);
    }
    
    public void setGui(GameSwing gui){
        this.gui = gui;
    }
    
    public void startGame(GameSwing swing){
        this.gui = swing;
        gui.boardUpdater();
        gui.scoreUpdater(evaluate.evaluate(boardGrid));
    }
    
    public Move suggestedMove() throws IOException{
        int depth = gui.getDepth();
        int threads = gui.getThreads();
        System.out.println("Move Requested From Client...");
        System.out.println("Depth: " + gui.getDepth());
        System.out.println("Threads: " + gui.getThreads());
        System.out.println("Distributed Mode: " + useDistribution + "\n");

        evaluate.setDepth(depth);
        evaluate.threads(threads);
        
        if(useDistribution){
            List<Move> moves = boardGrid.getValidMoves();
            if(moves.isEmpty()){
                return null;
            }

            int mid = moves.size()/2;
            List<Move> firstHalf = new ArrayList<>(moves.subList(0, mid));
            List<Move> secondHalf = new ArrayList<>(moves.subList(mid, moves.size()));

            Object[] r1 = server1.bestMove(boardGrid, depth, threads, firstHalf);
            Move m1 = (Move)r1[0];
            int server1Score = (Integer)r1[1];

            Object[] r2 = server2.bestMove(boardGrid, depth, threads, secondHalf);
            Move m2 = (Move)r2[0];
            int server2Score = (Integer)r2[1];

            if(server1Score > server2Score){
                System.out.println("Server 1 had the best Move..");
                return m1;
            } else{
                System.out.println("Server 2 had the best Move..");
                return m2;
            }
        } else {
            return evaluate.bestMove(boardGrid);
        }
    }
    
    public Board getBoard(){
        return boardGrid;
    }
    
    public void cellClick(int x, int y){
        if(selectedX == -1 && selectedY == -1){
            if(boardGrid.getGrid()[x][y] == 1){
                selectedX = x;
                selectedY = y;
                gui.highlightCell(x,y);
            }
            
            return;
        }
        
        if(boardGrid.validMove(selectedX, selectedY, x, y)){
            
            boardGrid.move(selectedX, selectedY, x, y);
            
            selectedX = -1;
            selectedY = -1;
            
            gui.boardUpdater();
            
            if(boardGrid.gameOver()){
                gui.gameOverMSG("Game Over! Pegs Left: " + boardGrid.pegs());
            }
                int score = evaluate.evaluate(boardGrid);
                gui.scoreUpdater(score);
        } else {
            selectedX = -1;
            selectedY = -1;
            gui.clearHighlight();
        }
    }
    
}
