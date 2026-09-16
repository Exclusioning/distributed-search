package com.mycompany.pegsolitaire;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Evaluator implements Serializable{
   int depth;
   int bestScore = 0;
   ForkJoinPool pool = new ForkJoinPool();
   
   public void setDepth(int d){
       this.depth = Math.max(1, d);
   }
   
   public void threads(int threads){
       if(threads < 1){
           threads = 1;
       }
       pool = new ForkJoinPool(threads);
   }
   
   public int evaluate(Board boardGrid){
       int remainingPegs = boardGrid.pegs();
       int move = boardGrid.getValidMoves().size();
       int center = (boardGrid.getGrid()[3][3] == 1) ? 5 : 0;
       return (move * 10) - (remainingPegs * 2) + center;
   }
   
   public int searchBoard(Board board, int depth){
       if(depth <= 0 || board.gameOver()){
           return evaluate(board);
       }
       
       List<Move> moves = board.getValidMoves();
       if(moves.isEmpty()){
           return evaluate(board);
       }
       
       int bestScore = Integer.MIN_VALUE;
       
       for(Move m : moves){
           Board temp = new Board(board);
           temp.move(m.fromX, m.fromY, m.toX, m.toY);
           int score = searchBoard(temp, depth-1);
           if(score > bestScore){
               bestScore = score;
           }
       }
       
       return bestScore;
   }
   
   public Move bestMove(Board board){
       System.out.println("Evaluating...");
       System.out.println("Depth: " + depth);
       System.out.println("Thread Count: " + pool.getParallelism());
       System.out.println("Move Count: " + board.getValidMoves().size() + "\n");
       
       List<Move> moves = board.getValidMoves();
       if(moves.isEmpty()){
           return null;
       }
       
       Move best = pool.submit(() ->
               moves.parallelStream()
                    .map(m -> {
                        Board temp = new Board(board);
                        temp.move(m.fromX, m.fromY, m.toX, m.toY);
                        int score = searchBoard(temp, depth-1);
                        return new Object[]{m, score};
                    })
                    .max((a, b2) -> Integer.compare((int)a[1], (int)b2[1]))
                    .map(o -> (Move)o[0])
                    .orElse(null)
       ).join();
       
       System.out.println("Best Move: " + best);
       return best;
   }
}
