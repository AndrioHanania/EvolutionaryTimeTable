package engine.selection;

import engine.Solution;
import engine.Population;
import java.util.*;

public class Truncation extends Selection
{
    //Members
    private int m_TopPercent;

    //Constructors
    public Truncation(int topPercent)
    {
        m_TopPercent = topPercent;
    }

    //Methods
    @Override
    public String toString() {
        return "Name: Truncation, " + "Top percent: " + m_TopPercent;
    }

    public void setTopPercent(int topPercent){m_TopPercent=topPercent;}

    @Override
    public Population execute(Population currentGeneration)
    {
        currentGeneration.sort(Comparator.comparingDouble(Solution::getFitness));
        Population newGeneration = new Population();
        int numOfNewGeneration = (m_TopPercent * currentGeneration.size()) / 100;
        for(int i=0,j=currentGeneration.size()-1;i<numOfNewGeneration;i++,j--)
        {
            newGeneration.add(currentGeneration.get(j));
        }
        return newGeneration;
    }
}
