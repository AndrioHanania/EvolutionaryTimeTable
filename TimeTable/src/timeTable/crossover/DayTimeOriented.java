package timeTable.crossover;

import engine.Chromosome;
import engine.Solution;
import engine.crossover.Crossover;
import generated.ETTCrossover;
import timeTable.Grade;
import timeTable.TimeTable;
import timeTable.TimeTableChromosome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class DayTimeOriented implements Crossover
{

        private int m_NumOfCuttingPoints;


    public DayTimeOriented(int cuttingPoints)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
    }

    public DayTimeOriented(ETTCrossover ettCrossover){
        m_NumOfCuttingPoints = ettCrossover.getCuttingPoints();
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



