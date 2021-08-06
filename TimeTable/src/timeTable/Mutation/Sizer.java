package timeTable.Mutation;

import engine.Solution;
import engine.mutation.Mutation;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

public class Sizer extends Mutation
{
    //Members
    private int m_TotalTupples;


    //Constructors
    public Sizer(int totalTupples, double probability)
    {
        m_TotalTupples = totalTupples;
        m_Probability = probability;
    }

    //Methods
    @Override
    public String toString() {
        return "Name: Sizer" + super.toString();
    }


    @Override
    public void execute(Solution solution)
    {
        if (m_Random.nextDouble() < m_Probability) {
            TimeTable timeTable = (TimeTable) solution;
            int numOfChromosomeToMutation = m_Random.nextInt(m_TotalTupples) + 1;

            if (m_TotalTupples > 0) {
                for (int i = 0; i < numOfChromosomeToMutation; i++) {
                    timeTable.getChromosomes().add((TimeTableChromosome) timeTable.newRandomChromosome());
                }
            } else {
                for (int i = 0; i < numOfChromosomeToMutation; i++) {
                    timeTable.getChromosomes().remove(timeTable.getChromosomes().size() - 1);
                }
            }
        }
    }
}
