package timeTable.crossover;

import engine.Solution;
import engine.crossover.Crossover;
import generated.ETTCrossover;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.Comparator;


public class DayTimeOriented extends Crossover
{




    public DayTimeOriented(int cuttingPoints)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
    }

    public DayTimeOriented(ETTCrossover ettCrossover){
        m_NumOfCuttingPoints = ettCrossover.getCuttingPoints();
    }


    @Override
    public String toString() {
        return  "Name: DayTimeOriented" +  super.toString();
    }


        @Override
        public Solution execute(Solution parent1, Solution parent2) {
            TimeTable TTableParent1 = (TimeTable) parent1;
            TimeTable TTableParent2 = (TimeTable) parent2;
            TTableParent1.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getDay));
            TTableParent1.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getHour));
            return CrossoverUtils.utilExecute(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
    }
    // choose number between 1 and num of crossing points (from ETTCrossover) - we shall call it numOfCrossingPoint
    //

}



