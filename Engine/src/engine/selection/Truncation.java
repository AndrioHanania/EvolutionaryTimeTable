package engine.selection;

import engine.Solution;
import engine.Population;
import java.util.*;


public class Truncation implements Selection
{
    //Members
    private int m_TopPercent;
    //private String m_Configuration;

    //Constructors
    public Truncation(int topPercent)
    {
        m_TopPercent = topPercent;
    }

    //Methods
    @Override
    public Population execute(Population currentGeneration)
    {
        currentGeneration.sort(Comparator.comparingInt(Solution::getFitness));
        Population newGeneration = new Population();
        int numOfNewGeneration = (m_TopPercent * currentGeneration.size()) / 100;
        for(int i=0;i<numOfNewGeneration;i++)
        {
            newGeneration.add(currentGeneration.get(i));
        }
        return newGeneration;
    }
}
