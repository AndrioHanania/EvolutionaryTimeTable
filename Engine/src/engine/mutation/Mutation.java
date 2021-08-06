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

    public String getConfiguration(){return  m_Configuration;}

    @Override
    public String toString() {
        return "Probability: " + m_Probability +
                ", Configuration: " + m_Configuration ;
    }
}
