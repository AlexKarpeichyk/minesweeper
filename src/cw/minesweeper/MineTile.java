/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw.minesweeper;

/**
 *
 * @author ak581
 */
public class MineTile 
{
    private boolean mined, revealed, marked = false;
    private int minedNeighbours = 0;

    public MineTile() 
    {
    }

    public boolean isMined() 
    {
        return mined;
    }

    public boolean isRevealed() 
    {
        return revealed;
    }

    public boolean isMarked() 
    {
        return marked;
    }

    public int getNeighbours() 
    {
        return minedNeighbours;
    }

    public void mine() {
        this.mined = true;
    }

    public void reveal() {
        this.revealed = true;
    }

    public void mark() {
        this.marked = !marked;
    }

    public void addMinedNeighbours() {
        minedNeighbours++;
    }
}
