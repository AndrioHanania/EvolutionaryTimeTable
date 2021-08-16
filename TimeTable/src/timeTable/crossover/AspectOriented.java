package timeTable.crossover;

import engine.Solution;
import engine.crossover.Crossover;
import timeTable.TimeTable;

import java.util.List;

public class AspectOriented extends Crossover {

    //Constructors
    public AspectOriented(int cuttingPoints)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
    }

    //Methods
    @Override
    public String toString() {
        return  "Name: AspectOriented, " +  super.toString();
    }

    public List<Solution> execute(Solution parent1, Solution parent2)
    {
        TimeTable TTableParent1 = (TimeTable) parent1;
        TimeTable TTableParent2 = (TimeTable) parent2;
        return CrossoverUtils.AspectOriented(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
    }
}
