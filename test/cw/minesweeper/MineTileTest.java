package cw.minesweeper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ak581
 */
public class MineTileTest 
{
    MineTile mined = new MineTile();
    MineTile notMined = new MineTile();
    
    public MineTileTest() 
    {
    }
    
    @Test 
    public void testMineTile()
    {
        mined.mine();
        boolean m = mined.isMined();
        assertEquals(true, m);   
    }
    
    @Test
    public void testAddMeighbours()
    {
        int n = notMined.getNeighbours();
        assertEquals(0, n);
        notMined.addMinedNeighbours();
        assertEquals(1, notMined.getNeighbours());
    }
}
