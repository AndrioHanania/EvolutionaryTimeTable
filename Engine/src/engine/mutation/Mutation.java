package engine.mutation;

import engine.Solution;

import java.util.Random;

public abstract class Mutation
{
    //Members
    protected double m_Probability;
    protected Random m_Random = new Random();
    protected String m_Configuration;

    //Methods
    public abstract void execute(Solution item);

    public void setConfiguration(String configuration){m_Configuration = configuration;}
}
