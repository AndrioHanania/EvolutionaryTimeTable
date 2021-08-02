package timeTable.crossover;

import engine.Solution;
import timeTable.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrossoverUtils {

    private int m_NumOfCuttingPoints;
    private static List<Integer> m_CuttingPointsLocations;
    private static boolean m_ToCross;
    private static boolean m_StartWithFirstParent = true;

    public static Solution utilExecute(TimeTable tTableParent1, TimeTable tTableParent2,int numOfCuttingPoints)
    {
        m_ToCross = m_StartWithFirstParent;
        m_CuttingPointsLocations = new ArrayList<>(numOfCuttingPoints);
        CrossoverUtils.randomizeCrossingPoints(tTableParent1,tTableParent2, numOfCuttingPoints);

        TimeTable childTimeTable = new TimeTable();
        childTimeTable.newRandomInstance();

        int previousCuttingPoint=0;
        for (int i =0; i<numOfCuttingPoints;i++)
        {
            if (m_ToCross)
            {
                for(int j = previousCuttingPoint; j< m_CuttingPointsLocations.get(i); j++) {
                    childTimeTable.getChromosomes().add(tTableParent1.getChromosomes().get(j));
                }
            }
            else {
                for (int j = previousCuttingPoint; j < m_CuttingPointsLocations.get(i); j++) {
                    childTimeTable.getChromosomes().add(tTableParent2.getChromosomes().get(j));
                }
            }
            previousCuttingPoint = m_CuttingPointsLocations.get(i);
            m_ToCross=!m_ToCross;
        }

        m_StartWithFirstParent = !m_StartWithFirstParent;
        return  childTimeTable;
    }


    private static void randomizeCrossingPoints(TimeTable tTableParent1, TimeTable tTableParent2, int numOfCuttingPoints) {
        Random random = new Random();
        int randomCuttingPoint;
        m_CuttingPointsLocations = new ArrayList<>(numOfCuttingPoints);
        for(int i=0; i < numOfCuttingPoints ; i++)
        {
            m_CuttingPointsLocations.add(random.nextInt(tTableParent1.getChromosomes().size()));
            //כרגע עושים לפי הראשון אבל אפשר מקס\מין בין שני ההורים
        }


    }
}
