package engine.mutation;

import engine.Solution;

public class Sizer<T extends Solution/* &  Problem*/> implements Mutation<T>
{
    //Members
    private int m_TotalTupples;

    //Constructors
    public Sizer(int totalTupples)
    {
        m_TotalTupples = totalTupples;
    }

    //Methods
    @Override
    public void execute(T item) {
        int numOfChromosomeToMutation = m_Random.nextInt(m_TotalTupples)+1;

        if(m_TotalTupples > 0)
        {

        }
        else
        {
          //  for()
        }

    }
}
