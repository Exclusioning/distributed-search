package com.mycompany.pegsolitaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class GameGuiTest {

    private GameGui gameGui;
    private GameSwing mockGui;

    @BeforeEach
    public void setUp() {
        gameGui = new GameGui();
        // Create a mock GameSwing to prevent NullPointerExceptions and headless window errors
        mockGui = Mockito.mock(GameSwing.class);
        gameGui.setGui(mockGui);
    }

    @Test
    public void testInitialState() {
        assertNotNull(gameGui.getBoard());
        assertEquals(-1, gameGui.selectedX);
        assertEquals(-1, gameGui.selectedY);
        assertFalse(gameGui.useDistribution);
    }

    @Test
    public void testCellClickSelectionAndMove() {
        // Click on a valid peg at (3, 1) to select it
        gameGui.cellClick(3, 1);
        assertEquals(3, gameGui.selectedX);
        assertEquals(1, gameGui.selectedY);

        // Click on destination (3, 3) which is valid initially
        gameGui.cellClick(3, 3);
        
        // After a successful move, selection resets to -1, -1
        assertEquals(-1, gameGui.selectedX);
        assertEquals(-1, gameGui.selectedY);
        
        // Verify peg moved successfully on internal board
        assertEquals(1, gameGui.getBoard().getGrid()[3][3]);
    }
    
    @Test
    public void testCellClickInvalidSelectionReset() {
        // Click an invalid/empty cell first (3, 3)
        gameGui.cellClick(3, 3);
        assertEquals(-1, gameGui.selectedX);
        
        // Select a valid peg first
        gameGui.cellClick(3, 1);
        assertEquals(3, gameGui.selectedX);
        
        // Click an invalid target destination
        gameGui.cellClick(0, 0);
        
        // Selection should clear out
        assertEquals(-1, gameGui.selectedX);
        assertEquals(-1, gameGui.selectedY);
    }

    @Test
    public void testDistributionToggle() {
        assertFalse(gameGui.useDistribution);
        gameGui.useDistribution = true;
        assertTrue(gameGui.useDistribution);
    }


    @Test
    public void testGetBoardAndGui() {
        assertNotNull(gameGui.getBoard());
        GameSwing dummySwing = new GameSwing(gameGui);
        gameGui.setGui(dummySwing);
        assertEquals(dummySwing, gameGui.gui);
    }
}