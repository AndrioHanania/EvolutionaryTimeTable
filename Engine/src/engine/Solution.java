package engine;

public abstract class Solution
{
    //Members
    private int m_Fitness;

    //Methods
    public int getFitness(){return m_Fitness;}

    public abstract void calculateFitness();

}
