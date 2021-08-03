package timeTable.Mutation;

import engine.Solution;
import engine.mutation.Mutation;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

public class Flipping extends Mutation
{
    //Members
    private int m_MaxTupples;
    private char m_Component;

    //Constructors
    public Flipping(int maxTupples, char component, double probability)
    {
        m_MaxTupples = maxTupples;
        m_Component = component;
       m_Probability = probability;
    }

    //Methods
    @Override
    public void execute(Solution solution) {
        if (m_Random.nextDouble() < m_Probability) {
            TimeTable timeTable = (TimeTable) solution;
            int numOfChromosomeToChange = m_Random.nextInt(m_MaxTupples) + 1;
            for (int i = 0; i < numOfChromosomeToChange; i++) {
                int randomPlace = m_Random.nextInt(timeTable.getChromosomes().size());
                TimeTableChromosome timeTableChromosome = timeTable.getChromosomes().get(randomPlace);
                switch (m_Component) {
                    case 'D':
                        timeTableChromosome.setDay(m_Random.nextInt(timeTable.getDay()) + 1);
                        break;
                    case 'T':
                        timeTableChromosome.setTeacher(timeTable.getTeachers().get(m_Random.nextInt(timeTable.getTeachers().size())));
                        break;
                    case 'S':
                        timeTableChromosome.setSubject(timeTable.getSubjects().get(m_Random.nextInt(timeTable.getSubjects().size())));
                        break;
                    case 'C':
                        timeTableChromosome.setGrade(timeTable.getGrades().get(m_Random.nextInt(timeTable.getGrades().size())));
                        break;
                    case 'H':
                        timeTableChromosome.setHour(m_Random.nextInt(timeTable.getHour()) + 1);
                        break;
                    default:
                        //////////////////////////////////
                        break;
                }
                ;
                timeTable.getChromosomes().set(randomPlace, timeTableChromosome);
            }
        }
    }
}
