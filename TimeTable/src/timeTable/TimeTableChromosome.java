package timeTable;

import engine.Chromosome;

public class TimeTableChromosome extends Chromosome {
    //Members
    private int m_Day;
    private int m_Hour;
    private Class m_Class;
    private Teacher m_Teacher;
    private Subject m_Subject;



    public TimeTableChromosome(int day, int hour, Class clazz, Teacher teacher, Subject subject)
    {
        m_Day = day;
        m_Hour = hour;
        m_Class = clazz;
        m_Teacher = teacher;
       m_Subject = subject;
    }
}

