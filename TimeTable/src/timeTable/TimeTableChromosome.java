package timeTable;

import engine.Chromosome;

import java.util.Comparator;

public class TimeTableChromosome implements Chromosome {
    //Members
    private int m_Day;
    private int m_Hour;
    private Grade m_Class;
    private Teacher m_Teacher;
    private Subject m_Subject;

    public TimeTableChromosome() {
    }


    public TimeTableChromosome(int day, int hour, Grade clazz, Teacher teacher, Subject subject) {
        m_Day = day;
        m_Hour = hour;
        m_Class = clazz;
        m_Teacher = teacher;
        m_Subject = subject;
    }

    public void setDay(int day) {
        m_Day = day;
    }

    public void setTeacher(Teacher teacher) {
        m_Teacher = teacher;
    }

    public void setSubject(Subject subjet) {
        m_Subject = subjet;
    }

    public void setGrade(Grade grade) {
        m_Class = grade;
    }

    public void setHour(int hour) {
        m_Hour = hour;
    }

    public int getDay() {
        return m_Day;
    }

    public int getHour() {
        return m_Hour;
    }

    public Grade getGrade() {
        return m_Class;
    }

    public Teacher getTeacher() {
        return m_Teacher;
    }

    public Subject getSubject() {
        return m_Subject;
    }

    public static int compareWithTeacher(TimeTableChromosome timeTableChromosome1, TimeTableChromosome timeTableChromosome2) {
        return Integer.compare(timeTableChromosome1.getTeacher().getIdNumber(), timeTableChromosome2.getTeacher().getIdNumber());
    }
}
