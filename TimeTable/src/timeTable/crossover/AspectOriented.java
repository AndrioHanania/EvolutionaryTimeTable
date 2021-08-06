package timeTable.crossover;

import engine.Solution;
import engine.crossover.Crossover;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

public class AspectOriented extends Crossover {




    public AspectOriented(int cuttingPoints)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
    }

    //Methods


    @Override
    public String toString() {
        return  "Name: AspectOriented" +  super.toString();
    }

    public Solution execute(Solution parent1, Solution parent2)
    {
        TimeTable TTableParent1 = (TimeTable) parent1;
        TimeTable TTableParent2 = (TimeTable) parent2;
        TTableParent1.getChromosomes().sort(TimeTableChromosome::compareWithTeacher);
        return CrossoverUtils.utilExecute(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
    }
}
