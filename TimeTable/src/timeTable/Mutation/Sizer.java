package timeTable.Mutation;

import engine.Solution;
import engine.mutation.Mutation;
import timeTable.TimeTable;
import timeTable.TimeTableChromosome;

public class Sizer implements Mutation
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
    public void execute(Solution solution) {
        TimeTable timeTable = (TimeTable) solution;
        int numOfChromosomeToMutation = m_Random.nextInt(m_TotalTupples)+1;

        if(m_TotalTupples > 0)
        {
            for(int i=0;i<numOfChromosomeToMutation;i++)
            {
                timeTable.getChromosomes().add((TimeTableChromosome) timeTable.newRandomChromosome());
            }
        }
        else
        {
            for(int i=0;i<numOfChromosomeToMutation;i++)
            { timeTable.getChromosomes().remove(timeTable.getChromosomes().size()-1);}
        }
    }
}
