package engine;

public abstract class Solution
{
    //Members
    protected int m_Fitness;

    //Methods
    public int getFitness(){return m_Fitness;}

    public void setFitness(int fitness){m_Fitness = fitness;}

    public abstract void calculateFitness();

}
