package timeTable.crossover;

import engine.Solution;
import timeTable.Rules.Rule;
import timeTable.Rules.RuleUtils;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.*;

public class CrossoverUtils
{

    private static List<Integer> m_CuttingPointsLocations;
    private static boolean m_CopyFromFirstParent;

    public static List<Solution> DayTimeOriented(TimeTable tTableParent1, TimeTable tTableParent2,int numOfCuttingPoints){

        List<Solution> solutions=new ArrayList<>();
        m_CopyFromFirstParent = true;
        m_CuttingPointsLocations = new ArrayList<>(numOfCuttingPoints);
        randomizeCrossingPoints(tTableParent1,tTableParent2, numOfCuttingPoints);
        //unlimited generality for tTableParent1
        TimeTable firstChild = createChild(tTableParent1);
        TimeTable secondChild = createChild(tTableParent1);

        int indexCuttingPoints=0;
        int index=0;

        for (int i=1;i<=tTableParent1.getDay();i++)
        {
            for (int j=1;j<=tTableParent1.getHour();j++)
            {
                for (int k=1;k<=tTableParent1.getGrades().size();k++)
                {
                    for (int l=1;l<=tTableParent1.getTeachers().size();l++)
                    {
                        for (int m=1;m<=tTableParent1.getSubjects().size();m++)
                        {
                            index++;
                            TimeTableChromosome timeTableChromosome = new TimeTableChromosome(i,j,k,l,m,tTableParent1);
                            if(m_CopyFromFirstParent)
                            {
                                if (tTableParent1.getChromosomes().contains(timeTableChromosome))
                                {
                                    firstChild.getChromosomes().add(timeTableChromosome);
                                }
                                if (tTableParent2.getChromosomes().contains(timeTableChromosome))
                                {
                                    secondChild.getChromosomes().add(timeTableChromosome);
                                }
                            }
                            else
                            {
                                if (tTableParent2.getChromosomes().contains(timeTableChromosome))
                                {
                                    firstChild.getChromosomes().add(timeTableChromosome);
                                }
                                if (tTableParent1.getChromosomes().contains(timeTableChromosome))
                                {
                                    secondChild.getChromosomes().add(timeTableChromosome);
                                }
                            }
                            if (indexCuttingPoints != m_CuttingPointsLocations.size())
                            {
                                if (index>= m_CuttingPointsLocations.get(indexCuttingPoints))
                                {
                                    indexCuttingPoints++;
                                    m_CopyFromFirstParent=!m_CopyFromFirstParent;

                                }
                            }
                        }
                    }
                }
            }
        }
        solutions.add(firstChild);
        solutions.add(secondChild);
        return solutions;
    }

    public static List<Solution> AspectOriented(TimeTable tTableParent1, TimeTable tTableParent2, int numOfCuttingPoints){
        List<Solution> solutions=new ArrayList<>();
        m_CopyFromFirstParent = true;
        m_CuttingPointsLocations = new ArrayList<>(numOfCuttingPoints);

        //unlimited generality for tTableParent1
        TimeTable firstChild = createChild(tTableParent1);
        TimeTable secondChild = createChild(tTableParent1);

        int indexCuttingPoints=0;
        int index=0;

        for (int i=1;i<=tTableParent1.getTeachers().size();i++)
        {
            randomizeCrossingPoints(tTableParent1,tTableParent2, numOfCuttingPoints);

            for (int j=1;j<=tTableParent1.getHour();j++)
            {
                for (int k=1;k<=tTableParent1.getGrades().size();k++)
                {
                    for (int l=1;l<=tTableParent1.getDay();l++)
                    {
                        for (int m=1;m<=tTableParent1.getSubjects().size();m++)
                        {
                            index++;
                            TimeTableChromosome timeTableChromosome = new TimeTableChromosome(i,j,k,l,m,tTableParent1);
                            if(m_CopyFromFirstParent)
                            {
                                if (tTableParent1.getChromosomes().contains(timeTableChromosome))
                                {
                                    firstChild.getChromosomes().add(timeTableChromosome);
                                }
                                if (tTableParent2.getChromosomes().contains(timeTableChromosome))
                                {
                                    secondChild.getChromosomes().add(timeTableChromosome);
                                }
                            }
                            else
                            {
                                if (tTableParent2.getChromosomes().contains(timeTableChromosome))
                                {
                                    firstChild.getChromosomes().add(timeTableChromosome);
                                }
                                if (tTableParent1.getChromosomes().contains(timeTableChromosome))
                                {
                                    secondChild.getChromosomes().add(timeTableChromosome);
                                }
                            }
                            if (indexCuttingPoints != m_CuttingPointsLocations.size())
                            {
                                if (index>= m_CuttingPointsLocations.get(indexCuttingPoints))
                                {
                                    indexCuttingPoints++;
                                    m_CopyFromFirstParent=!m_CopyFromFirstParent;

                                }
                            }
                        }
                    }
                }
            }
        }
        solutions.add(firstChild);
        solutions.add(secondChild);
        return solutions;
    }

    private static void randomizeCrossingPoints(TimeTable tTableParent1, TimeTable tTableParent2, int numOfCuttingPoints) {
        Random random = new Random();
        m_CuttingPointsLocations = new ArrayList<>(numOfCuttingPoints);
        //unlimited generality for tTableParent1
        int D=tTableParent1.getDay();
        int H=tTableParent1.getHour();
        int T=tTableParent1.getTeachers().size();
        int G=tTableParent1.getGrades().size();
        int S=tTableParent1.getSubjects().size();
        for(int i=0; i < numOfCuttingPoints ; i++)
        {
            m_CuttingPointsLocations.add(random.nextInt(D * H * T * G * S));
        }
        Collections.sort(m_CuttingPointsLocations);
    }


    private static TimeTable createChild(TimeTable father)
    {
        TimeTable child = new TimeTable();
        child.setHourStudyForDay(father.getHour());
        child.setTeachers(father.getTeachers());
        child.setDaysForStudy(father.getDay());
        child.setGrades(father.getGrades());
        child.setSubjects(father.getSubjects());
        child.setHardRulesWeight(father.getHardRulesWeight());
        child.setSumMinRequirementsGrade(father.getSumMinRequirementsGrade());
        for(Rule rule : father.getRules())
        {
            child.getRules().add(RuleUtils.CreateRule(rule));
        }
        return child;
    }
}