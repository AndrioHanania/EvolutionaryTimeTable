package engine;

public abstract class Solution
{
    //Members
    protected double m_Fitness = 0;

    //Methods
    public double getFitness(){return m_Fitness;}

    public void setFitness(int fitness){m_Fitness = fitness;}

    public abstract void calculateFitness();

}
