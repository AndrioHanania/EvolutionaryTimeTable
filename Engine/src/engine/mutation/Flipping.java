package engine.mutation;

import engine.Solution;

import java.util.Random;

public class Flipping<T extends Solution> implements Mutation<T>
{
    //Members
    private int m_MaxTupples;
    private char m_Component;

    //Constructors
    public Flipping(int maxTupples, char component)
    {
        m_MaxTupples = maxTupples;
        m_Component = component;
    }

    //Methods
    @Override
    public void execute(T item) {
        int tupple = m_Random.nextInt(m_MaxTupples);


    }
}
