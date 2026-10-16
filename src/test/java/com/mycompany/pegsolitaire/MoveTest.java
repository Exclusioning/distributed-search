package com.mycompany.pegsolitaire;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void testMoveCreationAndToString() {
        Move move = new Move(2, 3, 4, 3);
        assertEquals(2, move.fromX);
        assertEquals(3, move.fromY);
        assertEquals(4, move.toX);
        assertEquals(3, move.toY);
        
        String expectedString = "Move from (2,3) to (4,3)";
        assertEquals(expectedString, move.toString());
    }
}