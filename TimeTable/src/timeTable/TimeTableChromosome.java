package timeTable;

import engine.Chromosome;

public class TimeTableChromosome extends Chromosome {
    //Members
    private int m_Day;
    private int m_Hour;
    private Grade m_Class;
    private Teacher m_Teacher;
    private Subject m_Subject;



    public TimeTableChromosome(int day, int hour, Grade clazz, Teacher teacher, Subject subject)
    {
        m_Day = day;
        m_Hour = hour;
        m_Class = clazz;
        m_Teacher = teacher;
       m_Subject = subject;
    }

    public void setDay(int day) {m_Day = day;}

    public void setTeacher(Teacher teacher) {m_Teacher = teacher;}

    public void setSubjet(Subject subjet) {m_Subject=subjet;}

    public void setGrade(Grade grade) { m_Class = grade;
    }

    public  void setHour( int hour){ m_Hour = hour;}
}

