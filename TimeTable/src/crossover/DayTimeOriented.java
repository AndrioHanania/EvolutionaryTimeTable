package crossover;

import engine.Solution;
import engine.crossover.Crossover;
import timeTable.Grade;
import timeTable.TimeTable;

public class DayTimeOriented implements Crossover
{
    private TimeTable m_TimeTable;
    private int m_HoursToStudyInADay;
    private int m_DaysToStudyInAWeek;

    public DayTimeOriented(){m_TimeTable = new TimeTable();}

    public DayTimeOriented(TimeTable timeTable)
    {
        m_TimeTable = timeTable;
        m_HoursToStudyInADay = timeTable.getHour();
        m_DaysToStudyInAWeek = timeTable.getDay();
    }

    // new random instance or new random chromosome
    //to string




    //Methods
    @Override
    public Solution execute(Solution parent1, Solution parent2) {
        return null;
    }
}



