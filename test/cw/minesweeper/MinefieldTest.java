package cw.minesweeper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ak581
 */
public class MinefieldTest {
    // Mine the top row, then the corner
    private final String topCentre = "1*1\n111\n000\n";
    private final String thenBottomRight = "1*1\n122\n01*\n";
    // Do it again, but with a clean minefield
    private final String cleanTopRight = "01*\n011\n000\n";
    private final String thenBottomLeft = "01*\n121\n*10\n";
            
   
    @Test
    public void testInsertion() {
        // This is the minimum set of tests to ensure correctness of
        // the updating of adjacent tiles.
        // Thee are many other more complete approaches
        Minefield mf = new Minefield(3, 3, 1);
        mf.mineTile(0, 1); 
        assertEquals(topCentre, mf.toString());
        mf.mineTile(2, 2);
        assertEquals(thenBottomRight,mf.toString());
        Minefield mf1 = new Minefield(3, 3, 1);
        mf1.mineTile(0, 2);
        assertEquals(cleanTopRight,mf1.toString());
        mf1.mineTile(2,0);
        assertEquals(thenBottomLeft,mf1.toString());
        
    }

    @Test
    public void testPopulate() {
        // This is a one off test.  One might argue that this is a random 
        // method, so should be called multiple times.  However, given the
        // mine occupancy used, there are multiple collisions with very
        // high probability
        Minefield mf = new Minefield(10, 10, 25);
        mf.populate();
        String s = mf.toString();
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '*') {
                counter++;
            }
        }
        assertEquals(25, counter);
        System.out.print(s);
    }
    
    @Test 
    public void testMarkingUnmarking()
    {
        Minefield mf = new Minefield(4, 4, 3);
        mf.markTile(1,1);
        String marked = "▋ ▋ ▋ ▋ \n▋ F ▋ ▋ \n▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ \n";
        String unMarked = "▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ \n";
        assertTrue(mf.isMarked(1,1));
        //assertEquals(marked, mf.currentGrid());
        mf.markTile(1, 1);
        assertFalse(mf.isMarked(1,1));
        //assertEquals(unMarked, mf.currentGrid());
    }
    
    @Test
    public void testStep()
    {
        Minefield mf1 = new Minefield(5, 5, 10);
        mf1.mineTile(0, 4);
        mf1.mineTile(3, 4);
        mf1.mineTile(4, 1);
        String hasNeighbours = "▋ ▋ ▋ 1 ▋ \n▋ ▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ ▋ \n▋ ▋ ▋ ▋ ▋ \n";
        String noNeighbours = "0 0 0 1 ▋ \n0 0 0 1 ▋ \n0 0 0 1 ▋ \n1 1 1 1 ▋ \n▋ ▋ ▋ ▋ ▋ \n";
        mf1.step(0, 3);
        //assertEquals(hasNeighbours, mf1.currentGrid());
        mf1.step(0, 0);
        //assertEquals(noNeighbours, mf1.currentGrid());    
        boolean noFail = mf1.step(0, 4);        
        //assertEquals(false, noFail);            
    }
    
    @Test
    public void testVictory()
    {
        Minefield mf = new Minefield(5, 5, 3);
        mf.mineTile(0, 4);
        mf.mineTile(2, 2);
        mf.mineTile(3, 0);
        mf.markTile(0, 4);
        mf.markTile(2, 2);
        mf.markTile(3, 0);
        boolean win = mf.areAllMinesRevealed();
        assertTrue(win);        
    }
}
