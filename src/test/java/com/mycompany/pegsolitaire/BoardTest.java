package com.mycompany.pegsolitaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInitialization() {
        int[][] grid = board.getGrid();
        // Check corners are invalid (-1)
        assertEquals(-1, grid[0][0]);
        // Check starting peg (e.g., [2,0]) is 1
        assertEquals(1, grid[2][0]);
        // Check center is empty (0)
        assertEquals(0, grid[3][3]);
        // Check total initial pegs count
        assertEquals(32, board.pegs());
    }

    @Test
    public void testCopyConstructor() {
        Board copy = new Board(board);
        assertEquals(board.pegs(), copy.pegs());
        assertNotSame(board.getGrid(), copy.getGrid());
    }

    @Test
    public void testValidPosition() {
        assertTrue(board.validPosition(3, 3));
        assertFalse(board.validPosition(0, 0)); // Out of board bounds (-1)
        assertFalse(board.validPosition(-1, 2)); // Out of bounds
        assertFalse(board.validPosition(7, 7)); // Out of bounds
    }

    @Test
    public void testValidAndInvalidMoves() {
        // Initial setup: center is [3,3] (0), peg is at [3,1] (1), jumper peg at [3,5] -> wait, let's look at standard setup.
        // Valid initial move: [3,1] jumping over [3,2] to [3,3]
        assertTrue(board.validMove(3, 1, 3, 3));
        
        // Invalid move: destination is not empty or out of bounds
        assertFalse(board.validMove(3, 0, 3, 3)); // distance is 3, not 2
        assertFalse(board.validMove(0, 2, 0, 4)); // invalid positions/pegs
    }

    @Test
    public void testPerformMove() {
        // [3,1] jumps to [3,3], middle [3,2] gets cleared
        assertTrue(board.validMove(3, 1, 3, 3));
        board.move(3, 1, 3, 3);

        int[][] grid = board.getGrid();
        assertEquals(0, grid[3][1]); // source now empty
        assertEquals(0, grid[3][2]); // jumped peg removed
        assertEquals(1, grid[3][3]); // destination now has peg
        assertEquals(31, board.pegs());
    }

    @Test
    public void testGetValidMovesNotEmpty() {
        assertFalse(board.getValidMoves().isEmpty());
    }

    @Test
    public void testGameOverFalseInitially() {
        assertFalse(board.gameOver());
    }
}