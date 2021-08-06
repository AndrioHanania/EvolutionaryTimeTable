package timeTable.chromosome;

import timeTable.Grade;
import timeTable.Subject;
import timeTable.Teacher;

public class TimeTableChromosome implements Chromosome {
    //Members
    private int m_Day;
    private int m_Hour;
    private Grade m_Class;
    private Teacher m_Teacher;
    private Subject m_Subject;

    public TimeTableChromosome() {
    }

    @Override
    public String toString() {
        return "Day: " + m_Day +
                ", Hour: " + m_Hour +
                ", Grade: " + m_Class +
                ", Teacher: " + m_Teacher +
                ", Subject: " + m_Subject;
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

    public static int compareWithGrade(TimeTableChromosome timeTableChromosome1, TimeTableChromosome timeTableChromosome2) {
        return Integer.compare(timeTableChromosome1.getGrade().getIdNumber(), timeTableChromosome2.getGrade().getIdNumber());
    }
}
