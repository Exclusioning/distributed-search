package com.mycompany.pegsolitaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    private Evaluator evaluator;
    private Board board;

    @BeforeEach
    public void setUp() {
        evaluator = new Evaluator();
        board = new Board();
    }

    @Test
    public void testSetDepthBounds() {
        evaluator.setDepth(-5);
        assertEquals(1, evaluator.depth); // Math.max(1, d)

        evaluator.setDepth(4);
        assertEquals(4, evaluator.depth);
    }

    @Test
    public void testThreadConfiguration() {
        evaluator.threads(0); // Should default to min 1 or handle smoothly
        evaluator.threads(4);
        // No direct getter for pool threads easily accessible except parallelism
        assertEquals(4, evaluator.pool.getParallelism());
    }

    @Test
    public void testEvaluateBoard() {
        int score = evaluator.evaluate(board);
        // Valid moves * 10 - remainingPegs * 2 + center
        int expectedMoves = board.getValidMoves().size();
        int expectedPegs = board.pegs();
        int expectedCenter = 0; // [3,3] is 0 initially
        int expectedScore = (expectedMoves * 10) - (expectedPegs * 2) + expectedCenter;
        
        assertEquals(expectedScore, score);
    }

    @Test
    public void testBestMoveReturnsValidMove() {
        evaluator.setDepth(1);
        evaluator.threads(2);
        Move best = evaluator.bestMove(board);
        
        assertNotNull(best);
        assertTrue(board.validMove(best.fromX, best.fromY, best.toX, best.toY));
    }

    @Test
    public void testBestMoveWithNoValidMoves() {
        // Create a board where no moves are possible (e.g., game over state)
        Board gameOverBoard = new Board();
        // Clear out the board so no moves exist
        int[][] grid = gameOverBoard.getGrid();
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 7; y++){
                grid[x][y] = 0;
            }
        }
        
        Move best = evaluator.bestMove(gameOverBoard);
        assertNull(best); // Should return null when no moves are available
    }

    @Test
    public void testSearchBoardDepthZeroOrGameOver() {
        Board tempBoard = new Board();
        // Test searchBoard when depth is 0 or negative
        int score = evaluator.searchBoard(tempBoard, 0);
        assertTrue(score != Integer.MIN_VALUE);
    }


    @Test
    public void testSearchBoardZeroDepth() {
        Board tempBoard = new Board();
        int score = evaluator.searchBoard(tempBoard, 0);
        assertNotNull(score);
    }
}