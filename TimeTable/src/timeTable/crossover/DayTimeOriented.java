package timeTable.crossover;

import engine.Solution;
import engine.crossover.Crossover;
import timeTable.TimeTable;

import java.util.List;

public class DayTimeOriented extends Crossover
{
    //Constructors
    public DayTimeOriented(int cuttingPoints)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
    }

    @Override
    public String toString() {
        return  "Name: DayTimeOriented " + " " +  super.toString();
    }

        @Override
        public List<Solution> execute(Solution parent1, Solution parent2) {
            TimeTable TTableParent1 = (TimeTable) parent1;
            TimeTable TTableParent2 = (TimeTable) parent2;
            return CrossoverUtils.DayTimeOriented(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
    }
}



